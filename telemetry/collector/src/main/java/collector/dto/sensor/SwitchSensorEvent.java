package collector.dto.sensor;

import collector.dto.enums.SensorEventType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SwitchSensorEvent extends SensorEvent {
    @NotNull
    Boolean state;
    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
