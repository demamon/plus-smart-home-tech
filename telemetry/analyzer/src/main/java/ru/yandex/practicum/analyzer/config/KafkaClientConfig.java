package ru.yandex.practicum.analyzer.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaClientConfig {

    private final KafkaPropertiesConfig kafkaProperties;

    @Bean
    public KafkaConsumer<String, HubEventAvro> createHubEventConsumer() {
        Properties props = new Properties();
        props.putAll(kafkaProperties.getHubConsumer());

        log.info("Created Hub Event Consumer with group: {}",
                kafkaProperties.getHubConsumer().get("group.id"));
        return new KafkaConsumer<>(props);
    }

    @Bean
    public KafkaConsumer<String, SensorsSnapshotAvro> createSensorsSnapshotConsumer() {
        Properties props = new Properties();
        props.putAll(kafkaProperties.getSnapshotConsumer());

        log.info("Created Sensors Snapshot Consumer with group: {}",
                kafkaProperties.getSnapshotConsumer().get("group.id"));
        return new KafkaConsumer<>(props);
    }

    @Bean
    @Qualifier("hubTopic")
    public String getHubTopic(@Value("${kafka.topic.hub}") String hubTopic) {
        log.info("Configured hub topic: {}", hubTopic);
        return hubTopic;
    }

    @Bean
    @Qualifier("snapshotTopic")
    public String getSnapshotTopic(@Value("${kafka.topic.snapshot}") String snapshotTopic) {
        log.info("Configured snapshot topic: {}", snapshotTopic);
        return snapshotTopic;
    }
}
