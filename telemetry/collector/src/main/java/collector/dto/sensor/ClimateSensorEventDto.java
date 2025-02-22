package collector.dto.sensor;

import collector.dto.InputEventDto;
import collector.dto.enums.InputEventTypeDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClimateSensorEventDto extends InputEventDto {
    @NotNull
    @Min(-60)
    @Max(80)
    Integer temperatureC;
    @Min(0)
    @Max(100)
    Integer humidity;
    Integer co2Level;

    @Override
    public InputEventTypeDto getType() {
        return InputEventTypeDto.CLIMATE_SENSOR_EVENT;
    }
}
