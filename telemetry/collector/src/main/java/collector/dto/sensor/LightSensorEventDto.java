package collector.dto.sensor;

import collector.dto.InputEventDto;
import collector.dto.enums.InputEventTypeDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LightSensorEventDto extends InputEventDto {
    @NotNull
    @Min(1)
    @Max(100)
    private int linkQuality;
    @NotNull
    @Min(1)
    @Max(100)
    private int luminosity;

    @Override
    public InputEventTypeDto getType() {
        return InputEventTypeDto.LIGHT_SENSOR_EVENT;
    }
}
