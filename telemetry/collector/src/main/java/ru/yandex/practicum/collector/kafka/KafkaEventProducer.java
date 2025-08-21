package ru.yandex.practicum.collector.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.Future;

@Service
public class KafkaEventProducer<V> {

    private final KafkaProducer<Void, V> producer;
    private final KafkaTopicsConfig topicsConfig;

    public KafkaEventProducer(
            Map<String, Object> producerConfigs,
            Serializer<Void> keySerializer,
            Serializer<V> valueSerializer,
            KafkaTopicsConfig topicsConfig
    ) {
        this.producer = new KafkaProducer<>(producerConfigs, keySerializer, valueSerializer);
        this.topicsConfig = topicsConfig;
    }

    public Future<RecordMetadata> sendToHubTopic(V value) {
        return producer.send(new ProducerRecord<>(topicsConfig.getHubEvents(), null, value));
    }

    public Future<RecordMetadata> sendToSensorTopic(V value) {
        return producer.send(new ProducerRecord<>(topicsConfig.getSensorEvents(), null, value));
    }

}
