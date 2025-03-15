package collector.mapper;

import collector.dto.enums.InputEventTypeDto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;

public class InputDtoMapper {

    private static SensorEventAvro buildSensorEventAvro(SensorEventProto inp, Object payload) {
        Instant timeUTC = Instant.ofEpochSecond(inp.getTimestamp().getSeconds(), inp.getTimestamp().getNanos());
        return SensorEventAvro.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(timeUTC)
                .setPayload(payload)
                .build();
    }

    public static SensorEventAvro mapLightSensorEvent(SensorEventProto inp) {
        LightSensorAvro payload = LightSensorAvro.newBuilder()
                .setLinkQuality(inp.getLightSensorEvent().getLinkQuality())
                .setLuminosity(inp.getLightSensorEvent().getLuminosity())
                .build();
        return buildSensorEventAvro(inp, payload);
    }

    public static SensorEventAvro mapSwitchSensorEvent(SensorEventProto inp) {
        SwitchSensorAvro payload = SwitchSensorAvro.newBuilder()
                .setState(inp.getSwitchSensorEventOrBuilder().getState())
                .build();
        return buildSensorEventAvro(inp, payload);
    }

    public static SensorEventAvro mapMotionSensorEvent(SensorEventProto inp) {
        MotionSensorAvro payload = MotionSensorAvro.newBuilder()
                .setLinkQuality(inp.getMotionSensorEvent().getLinkQuality())
                .setMotion(inp.getMotionSensorEvent().getMotion())
                .setVoltage(inp.getMotionSensorEvent().getVoltage())
                .build();
        return buildSensorEventAvro(inp, payload);
    }

    public static SensorEventAvro mapTemperatureSensorEvent(SensorEventProto inp) {
        TemperatureSensorAvro payload = TemperatureSensorAvro.newBuilder()
                .setTemperatureC(inp.getTemperatureSensorEvent().getTemperatureC())
                .setTemperatureF(inp.getTemperatureSensorEvent().getTemperatureF())
                .build();
        return buildSensorEventAvro(inp, payload);
    }

    public static SensorEventAvro mapClimateSensorEvent(SensorEventProto inp) {
        ClimateSensorAvro payload = ClimateSensorAvro.newBuilder()
                .setTemperatureC(inp.getClimateSensorEvent().getTemperatureC())
                .setHumidity(inp.getClimateSensorEvent().getHumidity())
                .setCo2Level(inp.getClimateSensorEvent().getCo2Level())
                .build();
        return buildSensorEventAvro(inp, payload);
    }

    public static DeviceAddedEvent mapDeviceAddedEvent(HubEventProto inp) {
        Instant timeUTC = Instant.ofEpochSecond(inp.getTimestamp().getSeconds(), inp.getTimestamp().getNanos());
        return DeviceAddedEvent.newBuilder()
                .setHubId(inp.getHubId())
                .setTimestamp(timeUTC)
                .setDeviceType(inp.getDeviceAdded().getType().toString())
                .setType(InputEventTypeDto.DEVICE_ADDED_EVENT.toString())
                .build();
    }

    public static DeviceRemovedEvent mapDeviceRemovedEvent(HubEventProto inp) {
        Instant timeUTC = Instant.ofEpochSecond(inp.getTimestamp().getSeconds(), inp.getTimestamp().getNanos());
        return DeviceRemovedEvent.newBuilder()
                .setHubId(inp.getHubId())
                .setTimestamp(timeUTC)
                .setType(InputEventTypeDto.DEVICE_REMOVED_EVENT.toString())
                .build();
    }

    public static ScenarioCondition mapScenarioCondition(ScenarioConditionProto inp) {
        int conditionValue;
        if (inp.hasBoolValue()) {
            conditionValue = inp.getBoolValue()?1:0;
        } else {
            conditionValue = inp.getIntValue();
        }
        return ScenarioCondition.newBuilder()
                .setSensorId(inp.getSensorId())
                .setType(ScenarioConditionType.valueOf(inp.getType().toString()))
                .setOperation(ScenarioOperationType.valueOf(inp.getOperation().toString()))
                .setValue(conditionValue)
                .build();
    }

    private static DeviceAction mapDeviceAction(DeviceActionProto inp) {
        return DeviceAction.newBuilder()
                .setSensorId(inp.getSensorId())
                .setType(ActionType.valueOf(inp.getType().toString()))
                .setValue(inp.getValue())
                .build();
    }

    public static ScenarioAddedEvent mapScenarioAddedEvent(HubEventProto inp) {
        Instant timeUTC = Instant.ofEpochSecond(inp.getTimestamp().getSeconds(), inp.getTimestamp().getNanos());

        var conditionsList = inp.getScenarioAdded().getConditionList().parallelStream().map(InputDtoMapper::mapScenarioCondition).toList();
        var actionsList = inp.getScenarioAdded().getActionList().parallelStream().map(InputDtoMapper::mapDeviceAction).toList();

        return ScenarioAddedEvent.newBuilder()
                .setHubId(inp.getHubId())
                .setTimestamp(timeUTC)
                .setName(inp.getScenarioAdded().getName())
                .setConditions(conditionsList)
                .setActions(actionsList)
                .setType(InputEventTypeDto.SCENARIO_ADDED_EVENT.toString())
                .build();
    }

    public static ScenarioRemovedEvent mapScenarioRemovedEvent(HubEventProto inp) {
        Instant timeUTC = Instant.ofEpochSecond(inp.getTimestamp().getSeconds(), inp.getTimestamp().getNanos());
        return ScenarioRemovedEvent.newBuilder()
                .setHubId(inp.getHubId())
                .setTimestamp(timeUTC)
                .setName(inp.getScenarioRemoved().getName())
                .setType(InputEventTypeDto.SCENARIO_REMOVED_EVENT.toString())
                .build();
    }
}
