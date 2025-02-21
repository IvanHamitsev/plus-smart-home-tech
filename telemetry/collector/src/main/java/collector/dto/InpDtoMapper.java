package collector.dto;

import collector.dto.hub.HubEvent;
import collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

public class InpDtoMapper {
    public static LightSensorEvent mapLightSensorEvent(SensorEvent inp) {
        return LightSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setLinkQuality(((collector.dto.sensor.LightSensorEvent)inp).getLinkQuality())
                .setLuminosity(((collector.dto.sensor.LightSensorEvent)inp).getLuminosity())
                .setType(inp.getType().toString())
                .build();
    }

    public static SwitchSensorEvent mapSwitchSensorEvent(SensorEvent inp) {
        return SwitchSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setState(((collector.dto.sensor.SwitchSensorEvent)inp).getState())
                .setType(inp.getType().toString())
                .build();
    }

    public static MotionSensorEvent mapMotionSensorEvent(SensorEvent inp) {
        return MotionSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setLinkQuality(((collector.dto.sensor.MotionSensorEvent)inp).getLinkQuality())
                .setMotion(((collector.dto.sensor.MotionSensorEvent)inp).getMotion())
                .setVoltage(((collector.dto.sensor.MotionSensorEvent)inp).getVoltage())
                .setType(inp.getType().toString())
                .build();
    }

    public static TemperatureSensorEvent mapTemperatureSensorEvent(SensorEvent inp) {
        return TemperatureSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setTemperatureC(((collector.dto.sensor.TemperatureSensorEvent)inp).getTemperatureC())
                .setTemperatureF(((collector.dto.sensor.TemperatureSensorEvent)inp).getTemperatureF())
                .setType(inp.getType().toString())
                .build();
    }

    public static ClimateSensorEvent mapClimateSensorEvent(SensorEvent inp) {
        return ClimateSensorEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setTemperatureC(((collector.dto.sensor.ClimateSensorEvent)inp).getTemperatureC())
                .setHumidity(((collector.dto.sensor.ClimateSensorEvent)inp).getHumidity())
                .setCo2Level(((collector.dto.sensor.ClimateSensorEvent)inp).getCo2Level())
                .setType(inp.getType().toString())
                .build();
    }

    public static DeviceAddedEvent mapDeviceAddedEvent(HubEvent inp) {
        return DeviceAddedEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setDeviceType(((collector.dto.hub.DeviceAddedEvent)inp).getDeviceType())
                .setType(inp.getType().toString())
                .build();
    }

    public static DeviceRemovedEvent mapDeviceRemovedEvent(HubEvent inp) {
        return DeviceRemovedEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setType(inp.getType().toString())
                .build();
    }

    public static ScenarioCondition mapScenarioCondition(collector.dto.hub.ScenarioCondition inp) {
        return ScenarioCondition.newBuilder()
                .setSensorId(inp.getSensorId())
                .setType(ScenarioConditionType.valueOf(inp.getType()))
                .setOperation(ScenarioOperationType.valueOf(inp.getOperation()))
                .setValue(inp.getValue())
                .build();
    }

    private static DeviceAction mapDeviceAction(collector.dto.hub.DeviceAction inp) {
        return DeviceAction.newBuilder()
                .setSensorId(inp.getSensorId())
                .setType(ActionType.valueOf(inp.getType()))
                .setValue(inp.getValue())
                .build();
    }
    public static ScenarioAddedEvent mapScenarioAddedEvent(HubEvent inp) {
        return ScenarioAddedEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setName(((collector.dto.hub.ScenarioAddedEvent)inp).getName())
                .setConditions(List.copyOf(((collector.dto.hub.ScenarioAddedEvent)inp).getConditions().parallelStream().map(InpDtoMapper::mapScenarioCondition).toList()))
                .setActions(List.copyOf(((collector.dto.hub.ScenarioAddedEvent)inp).getActions().parallelStream().map(InpDtoMapper::mapDeviceAction).toList()))
                .setType(inp.getType().toString())
                .build();
    }

    public static ScenarioRemovedEvent mapScenarioRemovedEvent(HubEvent inp) {
        return ScenarioRemovedEvent.newBuilder()
                .setId(inp.getId())
                .setHubId(inp.getHubId())
                .setTimestamp(inp.getTimestamp())
                .setName(((collector.dto.hub.ScenarioRemovedEvent)inp).getName())
                .setType(inp.getType().toString())
                .build();
    }
}
