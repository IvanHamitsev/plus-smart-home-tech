package collector.mapper;

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

    public static HubEventAvro buildHubEventAvro(HubEventProto inp, Object payload) {
        Instant timeUTC = Instant.ofEpochSecond(inp.getTimestamp().getSeconds(), inp.getTimestamp().getNanos());
        return HubEventAvro.newBuilder()
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
                .setState(inp.getSwitchSensorEvent().getState())
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

    public static HubEventAvro mapDeviceAddedEvent(HubEventProto inp) {
        DeviceAddedEventAvro payload = DeviceAddedEventAvro.newBuilder()
                .setId(inp.getDeviceAdded().getId())
                .setType(DeviceTypeAvro.valueOf(inp.getDeviceAdded().getType().toString()))
                .build();
        return buildHubEventAvro(inp, payload);
    }

    public static HubEventAvro mapDeviceRemovedEvent(HubEventProto inp) {
        DeviceRemovedEventAvro payload = DeviceRemovedEventAvro.newBuilder()
                .setId(inp.getDeviceRemoved().getId())
                .build();
        return buildHubEventAvro(inp, payload);
    }

    public static ScenarioConditionAvro mapScenarioCondition(ScenarioConditionProto inp) {
        int conditionValue;
        if (inp.hasBoolValue()) {
            conditionValue = inp.getBoolValue() ? 1 : 0;
        } else {
            conditionValue = inp.getIntValue();
        }
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(inp.getSensorId())
                .setType(ConditionTypeAvro.valueOf(inp.getType().toString())) // It was ScenarioConditionType earlier
                .setOperation(ConditionOperationAvro.valueOf(inp.getOperation().toString())) // It was ScenarioOperationType earlier
                .setValue(conditionValue)
                .build();
    }

    private static DeviceActionAvro mapDeviceAction(DeviceActionProto inp) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(inp.getSensorId())
                .setType(ActionTypeAvro.valueOf(inp.getType().toString()))
                .setValue(inp.getValue())
                .build();
    }

    public static HubEventAvro mapScenarioAddedEvent(HubEventProto inp) {

        var conditionsList = inp.getScenarioAdded().getConditionList().parallelStream().map(InputDtoMapper::mapScenarioCondition).toList();
        var actionsList = inp.getScenarioAdded().getActionList().parallelStream().map(InputDtoMapper::mapDeviceAction).toList();

        ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                .setName(inp.getScenarioAdded().getName())
                .setConditions(conditionsList)
                .setActions(actionsList)
                .build();
        return buildHubEventAvro(inp, payload);
    }

    public static HubEventAvro mapScenarioRemovedEvent(HubEventProto inp) {

        ScenarioRemovedEventAvro payload = ScenarioRemovedEventAvro.newBuilder()
                .setName(inp.getScenarioRemoved().getName())
                .build();

        return buildHubEventAvro(inp, payload);
    }
}
