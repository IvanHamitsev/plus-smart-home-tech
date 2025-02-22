package collector.dto.sensor;

import collector.dto.InputEventDto;
import collector.dto.enums.InputEventTypeDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MotionSensorEventDto extends InputEventDto {
    @Min(1)
    @Max(100)
    private int linkQuality;
    @NotNull
    Boolean motion;
    Integer voltage;

    @Override
    public InputEventTypeDto getType() {
        return InputEventTypeDto.MOTION_SENSOR_EVENT;
    }
}
