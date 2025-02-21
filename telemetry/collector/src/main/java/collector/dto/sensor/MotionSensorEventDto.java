package collector.dto.sensor;

import collector.dto.enums.SensorEventTypeDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MotionSensorEventDto extends SensorEventDto {
    @Min(1)
    @Max(100)
    private int linkQuality;
    @NotNull
    Boolean motion;
    Integer voltage;

    @Override
    public SensorEventTypeDto getType() {
        return SensorEventTypeDto.MOTION_SENSOR_EVENT;
    }
}
