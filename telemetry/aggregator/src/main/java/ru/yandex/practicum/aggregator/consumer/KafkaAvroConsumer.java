package ru.yandex.practicum.aggregator.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.aggregator.config.KafkaPropertiesConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class KafkaAvroConsumer {

    private final Consumer<String, SensorEventAvro> consumer;

    public KafkaAvroConsumer(KafkaPropertiesConfig kafkaProperties,
                             @Value("${aggregator.kafka.consumer.topic.sensor-events}") String topic) {
        Properties config = new Properties();

        config.putAll(kafkaProperties.getConsumer());

        this.consumer = new KafkaConsumer<>(config);
        this.consumer.subscribe(List.of(topic));

        Thread thread = new Thread(this::consumeMessages);
        thread.setDaemon(true);
        thread.start();
    }

    private void consumeMessages() {
        try {
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    log.info("Received message from partition {}, offset {}:\n{}\n",
                            record.partition(), record.offset(), record.value());
                }
            }
        } catch (Exception e) {
            log.error("Error while consuming messages", e);
        } finally {
            consumer.close();
        }
    }
}
