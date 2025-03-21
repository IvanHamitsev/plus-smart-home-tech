package ru.yandex.practicum.service;

import com.google.protobuf.Timestamp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionMessageProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.time.Instant;

@Slf4j
@Component
@AllArgsConstructor
public class SimpleSnapshotProcess implements SnapshotProcess {

    final ScenarioRepository scenarioRepository;

    @Override
    public void pushSnapshot(SensorsSnapshotAvro event,
                             HubRouterControllerGrpc.HubRouterControllerBlockingStub grpcClient) {

        var statesMap = event.getSensorsState();
        var scenariosList = scenarioRepository.findByHubId(event.getHubId());
        log.info("Обработка снапшота для хаба {}", event.getHubId());

        boolean scenarioFits;

        for (var scenario : scenariosList) {
            // предполагаем, что сценарий подходит
            scenarioFits = true;
            for (Condition condition : scenario.getConditions()) {
                var sensorsId = condition.getType().cast(
                        statesMap.get(condition.getConditionSensor().getId())
                );
                // предполагаем, что условие не соблюдается
                boolean conditionFits = false;
                for (Integer id : sensorsId) {
                    // достаточно одного совпадения
                    if (condition.getOperation().perform(condition.getConditionValue(), id)) {
                        conditionFits = true;
                        break;
                    }
                }
                // если хотябы одно условие не выполнено - сценарий не подходит
                if (!conditionFits) {
                    scenarioFits = false;
                    break;
                }
            }

            if (scenarioFits) {
                for (var action : scenario.getActions()) {
                    var request = getRequest(event, scenario, action);
                    log.info("Отправляю сообщение {} сенсору {}", action.getId(), action.getActionSensor().getId());
                    grpcClient.sendDeviceActionMessage(request);
                }
            } else {
                log.info("Сценарий {} не подошёл. Хаб {}", scenario.getName(), scenario.getHubId());
            }
        }
    }

    private static DeviceActionMessageProto getRequest(SensorsSnapshotAvro sensorsSnapshotAvro, Scenario scenario, Action action) {
        var deviceAction = DeviceActionProto.newBuilder()
                .setSensorId(action.getActionSensor().getId())
                .setType(ActionTypeProto.valueOf(action.getType().name()))
                .setValue(action.getActionValue())
                .build();

        Timestamp time = Timestamp.newBuilder()
                .setSeconds(Instant.now().getEpochSecond())
                .setNanos(Instant.now().getNano())
                .build();

        return DeviceActionMessageProto.newBuilder()
                .setHubId(sensorsSnapshotAvro.getHubId())
                .setScenarioName(scenario.getName())
                .setAction(deviceAction)
                .setTimestamp(time)
                .build();
    }
}
