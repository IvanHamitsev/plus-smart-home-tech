package collector.mapper;

import collector.dto.enums.InputEventTypeDto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;

public class InputDtoMapper {
    public static LightSensorEvent mapLightSensorEvent(SensorEventProto inp) {
        Instant timeUTC = Instant.ofEpochSecond(inp.getTimestamp().getSeconds(), inp.getTimestamp().getNanos());
        return LightSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(timeUTC)
                .setLinkQuality(inp.getLightSensorEvent().getLinkQuality())
                .setLuminosity(inp.getLightSensorEvent().getLuminosity())
                .setType(InputEventTypeDto.LIGHT_SENSOR_EVENT.toString())
                .build();
    }

    public static SwitchSensorEvent mapSwitchSensorEvent(SensorEventProto inp) {
        Instant timeUTC = Instant.ofEpochSecond(inp.getTimestamp().getSeconds(), inp.getTimestamp().getNanos());
        return SwitchSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(timeUTC)
                .setState(inp.getSwitchSensorEventOrBuilder().getState())
                .setType(InputEventTypeDto.SWITCH_SENSOR_EVENT.toString())
                .build();
    }

    public static MotionSensorEvent mapMotionSensorEvent(SensorEventProto inp) {
        Instant timeUTC = Instant.ofEpochSecond(inp.getTimestamp().getSeconds(), inp.getTimestamp().getNanos());
        return MotionSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(timeUTC)
                .setLinkQuality(inp.getMotionSensorEvent().getLinkQuality())
                .setMotion(inp.getMotionSensorEvent().getMotion())
                .setVoltage(inp.getMotionSensorEvent().getVoltage())
                .setType(InputEventTypeDto.MOTION_SENSOR_EVENT.toString())
                .build();
    }

    public static TemperatureSensorEvent mapTemperatureSensorEvent(SensorEventProto inp) {
        Instant timeUTC = Instant.ofEpochSecond(inp.getTimestamp().getSeconds(), inp.getTimestamp().getNanos());
        return TemperatureSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(timeUTC)
                .setTemperatureC(inp.getTemperatureSensorEvent().getTemperatureC())
                .setTemperatureF(inp.getTemperatureSensorEvent().getTemperatureF())
                .setType(InputEventTypeDto.TEMPERATURE_SENSOR_EVENT.toString())
                .build();
    }

    public static ClimateSensorEvent mapClimateSensorEvent(SensorEventProto inp) {
        Instant timeUTC = Instant.ofEpochSecond(inp.getTimestamp().getSeconds(), inp.getTimestamp().getNanos());
        return ClimateSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(timeUTC)
                .setTemperatureC(inp.getClimateSensorEvent().getTemperatureC())
                .setHumidity(inp.getClimateSensorEvent().getHumidity())
                .setCo2Level(inp.getClimateSensorEvent().getCo2Level())
                .setType(InputEventTypeDto.CLIMATE_SENSOR_EVENT.toString())
                .build();
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
