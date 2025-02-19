package collector.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TemperatureSensorEvent extends SensorEvent {

    @NotNull
    @Min(-60)
    @Max(300)
    Integer temperatureC;
    @NotNull
    @Min(-80)
    @Max(600)
    Integer temperatureF;
    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
