package ru.yandex.practicum.analyzer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.service.snapshot.SnapshotService;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SnapshotProcessor implements Runnable {

    private static final int COMMIT_BATCH_SIZE = 10;
    private static final Duration POLL_TIMEOUT = Duration.ofSeconds(4);

    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private final KafkaConsumer<String, SensorsSnapshotAvro> kafkaSensorsSnapshotConsumer;
    private final String snapshotTopic;
    private final SnapshotService snapshotService;

    public SnapshotProcessor(KafkaConsumer<String, SensorsSnapshotAvro> kafkaSensorsSnapshotConsumer,
                             @Qualifier("snapshotTopic") String snapshotTopic,
                             SnapshotService snapshotService) {
        this.kafkaSensorsSnapshotConsumer = kafkaSensorsSnapshotConsumer;
        this.snapshotTopic = snapshotTopic;
        this.snapshotService = snapshotService;
    }

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaSensorsSnapshotConsumer::wakeup));
        try {
            kafkaSensorsSnapshotConsumer.subscribe(List.of(snapshotTopic));
            log.info("Started Snapshot Processor for topic: {}", snapshotTopic);

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = kafkaSensorsSnapshotConsumer.poll(POLL_TIMEOUT);
                processRecords(records);
            }
        } catch (WakeupException e) {
            log.info("Kafka consumer wakeup received, shutting down...");
        } catch (Exception e) {
            log.error("Error during snapshot processing", e);
        } finally {
            shutdown();
        }
    }

    private void processRecords(ConsumerRecords<String, SensorsSnapshotAvro> records) {
        if (records.isEmpty()) {
            return;
        }

        int processedCount = 0;
        for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
            try {
                handleRecord(record);
                updateOffsets(record);
                processedCount++;

                if (processedCount % COMMIT_BATCH_SIZE == 0) {
                    commitOffsetsAsync();
                }
            } catch (Exception e) {
                log.error("Error processing record from topic {} partition {} offset {}",
                        record.topic(), record.partition(), record.offset(), e);
            }
        }

        commitOffsetsAsync(); // Коммит оставшихся офсетов
    }

    private void updateOffsets(ConsumerRecord<String, SensorsSnapshotAvro> record) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );
    }

    private void commitOffsetsAsync() {
        if (!currentOffsets.isEmpty()) {
            kafkaSensorsSnapshotConsumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Error during offset commit for {} partitions", offsets.size(), exception);
                } else {
                    log.debug("Successfully committed offsets for {} partitions", offsets.size());
                }
            });
        }
    }

    private void handleRecord(ConsumerRecord<String, SensorsSnapshotAvro> record) {
        snapshotService.handleSnapshot(record.value());
    }

    private void shutdown() {
        try {
            if (!currentOffsets.isEmpty()) {
                kafkaSensorsSnapshotConsumer.commitSync(currentOffsets);
                log.info("Committed {} offsets during shutdown", currentOffsets.size());
            }
        } catch (Exception e) {
            log.warn("Error during final offset commit", e);
        } finally {
            log.info("Closing snapshot consumer");
            kafkaSensorsSnapshotConsumer.close();
        }
    }
}
