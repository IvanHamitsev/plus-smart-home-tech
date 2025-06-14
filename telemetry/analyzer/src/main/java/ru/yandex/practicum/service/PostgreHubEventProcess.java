package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.model.ActionType;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.repository.SensorRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class PostgreHubEventProcess implements HubEventProcess {

    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;

    @Override
    public void pushHubEvent(HubEventAvro event) {
        // добавить/удалить/изменить в базе информацию об элементе
        if (null == event) {
            return;
        }
        String hubId = event.getHubId();

        var payload = event.getPayload();

        switch (payload) {
            case DeviceAddedEventAvro deviceAddedEventAvro -> {
                var existingSensor = sensorRepository.findByIdAndHubId(deviceAddedEventAvro.getId(), hubId);
                if (existingSensor.isEmpty()) {
                    sensorRepository.save(new Sensor(deviceAddedEventAvro.getId(), hubId));
                }
            }
            case DeviceRemovedEventAvro deviceRemovedEventAvro ->
                    sensorRepository.deleteById(deviceRemovedEventAvro.getId());
            case ScenarioRemovedEventAvro scenarioRemovedEventAvro ->
                    scenarioRepository.deleteByHubIdAndName(hubId, scenarioRemovedEventAvro.getName());
            case ScenarioAddedEventAvro scenarioAddedEventAvro -> {
                var existingScenario = scenarioRepository.findByHubIdAndName(hubId, scenarioAddedEventAvro.getName());
                if (existingScenario.isEmpty()) {
                    scenarioRepository.save(getScenario(hubId, scenarioAddedEventAvro));
                } else {
                    existingScenario.get().setConditions(getConditions(scenarioAddedEventAvro.getConditions(), hubId));
                    existingScenario.get().setActions(getActions(scenarioAddedEventAvro.getActions(), hubId));
                    scenarioRepository.save(existingScenario.get());
                }
            }
            case null -> throw new RuntimeException("Get empty payload of hub event");
            default -> throw new RuntimeException("Unknown type of hub event: " + payload.getClass());
        }
    }

    private Scenario getScenario(String hubId, ScenarioAddedEventAvro scenarioEvent) {
        return Scenario.builder()
                .name(scenarioEvent.getName())
                .hubId(hubId)
                .actions(getActions(scenarioEvent.getActions(), hubId))
                .conditions(getConditions(scenarioEvent.getConditions(), hubId))
                .build();
    }

    private List<Action> getActions(List<DeviceActionAvro> actions, String hubId) {
        List<Action> result = new ArrayList<>();
        for (DeviceActionAvro action : actions) {
            sensorRepository.findByIdAndHubId(action.getSensorId(), hubId).ifPresent(sensor -> result.add(Action.builder()
                    .type(ActionType.valueOf(action.getType().name()))
                    .actionValue(action.getValue())
                    .actionSensor(sensor)
                    .build()));
        }
        return result;
    }

    private List<Condition> getConditions(List<ScenarioConditionAvro> conditions, String hubId) {
        List<Condition> result = new ArrayList<>();
        for (ScenarioConditionAvro scenarioCondition : conditions) {
            sensorRepository.findByIdAndHubId(scenarioCondition.getSensorId(), hubId).ifPresent(sensor -> result.add(Condition.builder()
                    .type(ConditionType.valueOf(scenarioCondition.getType().name()))
                    .operation(ConditionOperationType.valueOf(scenarioCondition.getOperation().name()))
                    .conditionValue(scenarioCondition.getValue())
                    .conditionSensor(sensor)
                    .build()));
        }
        return result;
    }
}
