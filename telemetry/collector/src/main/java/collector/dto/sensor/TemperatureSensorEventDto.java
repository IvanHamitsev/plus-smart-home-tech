package collector.dto.sensor;

import collector.dto.enums.SensorEventTypeDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TemperatureSensorEventDto extends SensorEventDto {
    @NotNull
    @Min(-60)
    @Max(300)
    Integer temperatureC;
    @NotNull
    @Min(-80)
    @Max(600)
    Integer temperatureF;
    @Override
    public SensorEventTypeDto getType() {
        return SensorEventTypeDto.TEMPERATURE_SENSOR_EVENT;
    }
}
