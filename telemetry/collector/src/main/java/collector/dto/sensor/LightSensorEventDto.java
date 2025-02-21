package collector.dto.sensor;

import collector.dto.enums.SensorEventTypeDto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LightSensorEventDto extends SensorEventDto {
    @NotNull
    @Min(1)
    @Max(100)
    private int linkQuality;
    @NotNull
    @Min(1)
    @Max(100)
    private int luminosity;

    @Override
    public SensorEventTypeDto getType() {
        return SensorEventTypeDto.LIGHT_SENSOR_EVENT;
    }
}
