package ru.yandex.practicum.collector.kafka;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    private final KafkaTopicsConfig topicsConfig;

    public KafkaProducerConfig(KafkaTopicsConfig topicsConfig) {
        this.topicsConfig = topicsConfig;
    }

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.acks}")
    private String acks;

    @Value("${kafka.retries}")
    private int retries;

    @Bean
    public KafkaEventProducer<SpecificRecordBase> kafkaEventProducer() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, acks);
        configProps.put(ProducerConfig.RETRIES_CONFIG, retries);

        return new KafkaEventProducer<>(
                configProps,
                new VoidSerializer(),
                new GeneralAvroSerializer(),
                topicsConfig);
    }
}
