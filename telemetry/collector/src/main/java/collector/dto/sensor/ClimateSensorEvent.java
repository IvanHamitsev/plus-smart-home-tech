package collector.dto.sensor;

import collector.dto.enums.SensorEventType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClimateSensorEvent extends SensorEvent {
    @NotNull
    @Min(-60)
    @Max(80)
    Integer temperatureC;
    @Min(0)
    @Max(100)
    Integer humidity;
    Integer co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
