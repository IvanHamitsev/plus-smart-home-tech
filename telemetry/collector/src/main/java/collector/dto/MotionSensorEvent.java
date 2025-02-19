package collector.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MotionSensorEvent extends SensorEvent {
    @Min(1)
    @Max(100)
    private int linkQuality;
    @NotNull
    Boolean motion;
    Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
