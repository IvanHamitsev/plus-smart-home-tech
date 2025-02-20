package collector.dto.sensor;

import collector.dto.enums.SensorEventType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LightSensorEvent extends SensorEvent {
    @NotNull
    @Min(1)
    @Max(100)
    private int linkQuality;
    @NotNull
    @Min(1)
    @Max(100)
    private int luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
