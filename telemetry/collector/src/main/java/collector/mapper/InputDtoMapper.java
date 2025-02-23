package collector.mapper;

import collector.dto.InputEventDto;
import collector.dto.hub.*;
import collector.dto.sensor.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

public class InputDtoMapper {
    public static LightSensorEvent mapLightSensorEvent(InputEventDto inp) {
        return LightSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setLinkQuality(((LightSensorEventDto) inp).getLinkQuality())
                .setLuminosity(((LightSensorEventDto) inp).getLuminosity())
                .setType(inp.getType().toString())
                .build();
    }

    public static SwitchSensorEvent mapSwitchSensorEvent(InputEventDto inp) {
        return SwitchSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setState(((SwitchSensorEventDto) inp).getState())
                .setType(inp.getType().toString())
                .build();
    }

    public static MotionSensorEvent mapMotionSensorEvent(InputEventDto inp) {
        return MotionSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setLinkQuality(((MotionSensorEventDto) inp).getLinkQuality())
                .setMotion(((MotionSensorEventDto) inp).getMotion())
                .setVoltage(((MotionSensorEventDto) inp).getVoltage())
                .setType(inp.getType().toString())
                .build();
    }

    public static TemperatureSensorEvent mapTemperatureSensorEvent(InputEventDto inp) {
        return TemperatureSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setTemperatureC(((TemperatureSensorEventDto) inp).getTemperatureC())
                .setTemperatureF(((TemperatureSensorEventDto) inp).getTemperatureF())
                .setType(inp.getType().toString())
                .build();
    }

    public static ClimateSensorEvent mapClimateSensorEvent(InputEventDto inp) {
        return ClimateSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setTemperatureC(((ClimateSensorEventDto) inp).getTemperatureC())
                .setHumidity(((ClimateSensorEventDto) inp).getHumidity())
                .setCo2Level(((ClimateSensorEventDto) inp).getCo2Level())
                .setType(inp.getType().toString())
                .build();
    }

    public static DeviceAddedEvent mapDeviceAddedEvent(InputEventDto inp) {
        return DeviceAddedEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setDeviceType(((DeviceAddedEventDto) inp).getDeviceType())
                .setType(inp.getType().toString())
                .build();
    }

    public static DeviceRemovedEvent mapDeviceRemovedEvent(InputEventDto inp) {
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

    public static ScenarioAddedEvent mapScenarioAddedEvent(InputEventDto inp) {
        return ScenarioAddedEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setName(((ScenarioAddedEventDto) inp).getName())
                .setConditions(List.copyOf(((ScenarioAddedEventDto) inp).getConditions().parallelStream().map(InputDtoMapper::mapScenarioCondition).toList()))
                .setActions(List.copyOf(((ScenarioAddedEventDto) inp).getActions().parallelStream().map(InputDtoMapper::mapDeviceAction).toList()))
                .setType(inp.getType().toString())
                .build();
    }

    public static ScenarioRemovedEvent mapScenarioRemovedEvent(InputEventDto inp) {
        return ScenarioRemovedEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setName(((ScenarioRemovedEventDto) inp).getName())
                .setType(inp.getType().toString())
                .build();
    }
}
