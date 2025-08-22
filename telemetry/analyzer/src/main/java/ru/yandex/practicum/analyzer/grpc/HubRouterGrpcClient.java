package ru.yandex.practicum.analyzer.grpc;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.Action;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubRouterGrpcClient {

    @GrpcClient("hub-router")
    private HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterStub;

    public void sendToHubRouter(String sensorId, Action action, String hubId,
                                String scenarioName, Instant snapshotTimestamp) {
        try {
            Instant now = Instant.now();

            Timestamp timestampProto = Timestamp.newBuilder()
                    .setSeconds(now.getEpochSecond())
                    .setNanos(now.getNano())
                    .build();

            DeviceActionProto actionProto = DeviceActionProto.newBuilder()
                    .setSensorId(sensorId)
                    .setType(ActionTypeProto.valueOf(action.getType().name()))
                    .setValue(action.getValue() != null ? action.getValue() : 0)
                    .build();

            DeviceActionRequest request = DeviceActionRequest.newBuilder()
                    .setHubId(hubId)
                    .setScenarioName(scenarioName)
                    .setAction(actionProto)
                    .setTimestampMs(timestampProto)
                    .build();

            Empty response = hubRouterStub.handleDeviceAction(request);
            log.info("Action sent: hub_id={}, scenario={}, sensor_id={}, type={}, value={}",
                    hubId, scenarioName, sensorId, action.getType(), action.getValue());
        } catch (Exception e) {
            log.error("gRPC error while sending action for sensor {}", sensorId, e);
        }
    }
}
