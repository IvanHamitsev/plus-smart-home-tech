package collector.mapper;

import collector.dto.hub.*;
import collector.dto.sensor.*;
import collector.dto.sensor.SensorEventDto;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioCondition;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorEvent;

import java.util.List;

public class InpDtoMapper {
    public static LightSensorEvent mapLightSensorEvent(SensorEventDto inp) {
        return LightSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setLinkQuality(((LightSensorEventDto)inp).getLinkQuality())
                .setLuminosity(((LightSensorEventDto)inp).getLuminosity())
                .setType(inp.getType().toString())
                .build();
    }

    public static SwitchSensorEvent mapSwitchSensorEvent(SensorEventDto inp) {
        return SwitchSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setState(((SwitchSensorEventDto)inp).getState())
                .setType(inp.getType().toString())
                .build();
    }

    public static MotionSensorEvent mapMotionSensorEvent(SensorEventDto inp) {
        return MotionSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setLinkQuality(((MotionSensorEventDto)inp).getLinkQuality())
                .setMotion(((MotionSensorEventDto)inp).getMotion())
                .setVoltage(((MotionSensorEventDto)inp).getVoltage())
                .setType(inp.getType().toString())
                .build();
    }

    public static TemperatureSensorEvent mapTemperatureSensorEvent(SensorEventDto inp) {
        return TemperatureSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setTemperatureC(((TemperatureSensorEventDto)inp).getTemperatureC())
                .setTemperatureF(((TemperatureSensorEventDto)inp).getTemperatureF())
                .setType(inp.getType().toString())
                .build();
    }

    public static ClimateSensorEvent mapClimateSensorEvent(SensorEventDto inp) {
        return ClimateSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setTemperatureC(((ClimateSensorEventDto)inp).getTemperatureC())
                .setHumidity(((ClimateSensorEventDto)inp).getHumidity())
                .setCo2Level(((ClimateSensorEventDto)inp).getCo2Level())
                .setType(inp.getType().toString())
                .build();
    }

    public static DeviceAddedEvent mapDeviceAddedEvent(HubEventDto inp) {
        return DeviceAddedEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setDeviceType(((DeviceAddedEventDto)inp).getDeviceType())
                .setType(inp.getType().toString())
                .build();
    }

    public static DeviceRemovedEvent mapDeviceRemovedEvent(HubEventDto inp) {
        return DeviceRemovedEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setType(inp.getType().toString())
                .build();
    }

    public static ScenarioCondition mapScenarioCondition(ScenarioConditionDto inp) {
        return ScenarioCondition.newBuilder()
                .setSensorId(inp.getSensorId())
                .setType(ScenarioConditionType.valueOf(inp.getType()))
                .setOperation(ScenarioOperationType.valueOf(inp.getOperation()))
                .setValue(inp.getValue())
                .build();
    }

    private static DeviceAction mapDeviceAction(DeviceActionDto inp) {
        return DeviceAction.newBuilder()
                .setSensorId(inp.getSensorId())
                .setType(ActionType.valueOf(inp.getType()))
                .setValue(inp.getValue())
                .build();
    }
    public static ScenarioAddedEvent mapScenarioAddedEvent(HubEventDto inp) {
        return ScenarioAddedEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setName(((ScenarioAddedEventDto)inp).getName())
                .setConditions(List.copyOf(((ScenarioAddedEventDto)inp).getConditions().parallelStream().map(InpDtoMapper::mapScenarioCondition).toList()))
                .setActions(List.copyOf(((ScenarioAddedEventDto)inp).getActions().parallelStream().map(InpDtoMapper::mapDeviceAction).toList()))
                .setType(inp.getType().toString())
                .build();
    }

    public static ScenarioRemovedEvent mapScenarioRemovedEvent(HubEventDto inp) {
        return ScenarioRemovedEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setName(((ScenarioRemovedEventDto)inp).getName())
                .setType(inp.getType().toString())
                .build();
    }
}
