package ru.yandex.practicum.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AggregationService {

    private final Map<String, SensorsSnapshotAvro> snapshotsByHubId = new ConcurrentHashMap<>();

    public  Optional<SensorsSnapshotAvro> aggregateEvent(SensorEventAvro event) {
        log.info("Processing event for hub: {}, sensor: {}", event.getHubId(), event.getId());

        String hubId = event.getHubId();
        String sensorId = event.getId();

        SensorsSnapshotAvro hubSnapshot = snapshotsByHubId.computeIfAbsent(hubId, hubIdKey -> {
            SensorsSnapshotAvro newSnapshot = new SensorsSnapshotAvro();
            newSnapshot.setHubId(hubIdKey);
            newSnapshot.setTimestamp(event.getTimestamp());
            newSnapshot.setSensorsState(new HashMap<>());
            return newSnapshot;
        });

        synchronized (hubSnapshot) {
            Map<String, SensorStateAvro> sensorsState = hubSnapshot.getSensorsState();
            SensorStateAvro oldState = sensorsState.get(sensorId);

            if (oldState != null) {
                boolean isOlderTimestamp = event.getTimestamp().isBefore(oldState.getTimestamp());
                boolean isSameData = event.getPayload().equals(oldState.getData());

                log.info("Comparison - isOlderTimestamp: {}, isSameData: {}", isOlderTimestamp, isSameData);

                if (isOlderTimestamp || isSameData) {
                    return Optional.empty();
                }
            }

            SensorStateAvro updatedSensorState = new SensorStateAvro();
            updatedSensorState.setTimestamp(event.getTimestamp());
            updatedSensorState.setData(event.getPayload());

            sensorsState.put(sensorId, updatedSensorState);
            hubSnapshot.setTimestamp(event.getTimestamp());

            log.info("State changed, creating snapshot");
            return Optional.of(hubSnapshot);
        }
    }
}
