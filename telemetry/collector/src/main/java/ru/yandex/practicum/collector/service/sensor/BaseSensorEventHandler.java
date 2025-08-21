package ru.yandex.practicum.collector.service.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.collector.kafka.KafkaEventProducer;

import ru.yandex.practicum.collector.service.SensorEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@RequiredArgsConstructor
@Slf4j
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {
    protected final KafkaEventProducer<SensorEventAvro> producer;

    protected abstract T mapToAvro(SensorEventProto event);

    @Override
    public abstract SensorEventProto.PayloadCase getMessageType();

    @Override
    public void handle(SensorEventProto event) {
        if (!event.getPayloadCase().equals(getMessageType())) {
            throw new IllegalArgumentException(String.format("Неизвестный тип события: %s", event.getPayloadCase()));
        }

        T payload = mapToAvro(event);

        Instant timestamp = event.hasTimestamp()
                ? Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos())
                : Instant.now();

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(timestamp)
                .setPayload(payload)
                .build();

        log.info("Сообщение для отправки: {}", eventAvro);
        producer.sendToSensorTopic(eventAvro);
    }
}