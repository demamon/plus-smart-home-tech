package ru.yandex.practicum.aggregator.consumer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SensorEventConsumerRunner {

    private final SensorEventHandler eventHandler;
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private static final Duration POLL_TIMEOUT = Duration.ofMillis(1000);
    @Value("${kafka.consumer.topic.sensor-events}")
    private String subscribeTopic;

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            consumer.subscribe(List.of(subscribeTopic));
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(POLL_TIMEOUT);
                eventHandler.handle(records);
            }
        } catch (WakeupException ignored) {

        } finally {
            eventHandler.shutdown();
        }
    }
}
