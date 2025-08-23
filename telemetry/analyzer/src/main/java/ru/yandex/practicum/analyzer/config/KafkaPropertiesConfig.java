package ru.yandex.practicum.analyzer.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "analyzer.kafka")
public class KafkaPropertiesConfig {
    Map<String, String> hubConsumer;
    Map<String, String> snapshotConsumer;
}
