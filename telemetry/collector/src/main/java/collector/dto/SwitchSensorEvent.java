package collector.dto;

import jakarta.validation.constraints.*;

public class SwitchSensorEvent extends SensorEvent {
    @NotNull
    Boolean state;
    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
