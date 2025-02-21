package collector.dto.sensor;

import collector.dto.enums.SensorEventTypeDto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SwitchSensorEventDto extends SensorEventDto {
    @NotNull
    Boolean state;
    @Override
    public SensorEventTypeDto getType() {
        return SensorEventTypeDto.SWITCH_SENSOR_EVENT;
    }
}
