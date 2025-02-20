package collector.dto;

import collector.dto.sensor.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;

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
}
