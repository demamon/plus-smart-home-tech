package ru.yandex.practicum.analyzer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.service.hub.HubEventService;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {

    private static final int COMMIT_BATCH_SIZE = 10;
    private static final Duration POLL_TIMEOUT = Duration.ofSeconds(4);

    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private final KafkaConsumer<String, HubEventAvro> kafkaHubEventConsumer;
    private final String hubTopic;
    private final HubEventService hubEventService;

    public HubEventProcessor(KafkaConsumer<String, HubEventAvro> kafkaHubEventConsumer,
                             @Qualifier("hubTopic") String hubTopic,
                             HubEventService hubEventService) {
        this.kafkaHubEventConsumer = kafkaHubEventConsumer;
        this.hubTopic = hubTopic;
        this.hubEventService = hubEventService;
    }

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaHubEventConsumer::wakeup));
        try {
            kafkaHubEventConsumer.subscribe(List.of(hubTopic));
            log.info("Started Hub Event Processor for topic: {}", hubTopic);

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = kafkaHubEventConsumer.poll(POLL_TIMEOUT);
                processRecords(records);
            }
        } catch (WakeupException e) {
            log.info("Kafka consumer wakeup received, shutting down...");
        } catch (Exception e) {
            log.error("Error during processing hub events", e);
        } finally {
            shutdown();
        }
    }

    private void processRecords(ConsumerRecords<String, HubEventAvro> records) {
        if (records.isEmpty()) {
            return;
        }

        int processedCount = 0;
        for (ConsumerRecord<String, HubEventAvro> record : records) {
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

    private void updateOffsets(ConsumerRecord<String, HubEventAvro> record) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );
    }

    private void commitOffsetsAsync() {
        if (!currentOffsets.isEmpty()) {
            kafkaHubEventConsumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Error during offset commit for {} partitions", offsets.size(), exception);
                } else {
                    log.debug("Successfully committed offsets for {} partitions", offsets.size());
                }
            });
        }
    }

    private void handleRecord(ConsumerRecord<String, HubEventAvro> record) {
        hubEventService.handle(record.value());
    }

    private void shutdown() {
        try {
            if (!currentOffsets.isEmpty()) {
                kafkaHubEventConsumer.commitSync(currentOffsets);
                log.info("Committed {} offsets during shutdown", currentOffsets.size());
            }
        } catch (Exception e) {
            log.warn("Error during final offset commit", e);
        } finally {
            log.info("Closing hub consumer");
            kafkaHubEventConsumer.close();
        }
    }
}
