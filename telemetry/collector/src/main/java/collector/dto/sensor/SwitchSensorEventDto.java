package collector.dto.sensor;

import collector.dto.InputEventDto;
import collector.dto.enums.InputEventTypeDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SwitchSensorEventDto extends InputEventDto {
    @NotNull
    Boolean state;

    @Override
    public InputEventTypeDto getType() {
        return InputEventTypeDto.SWITCH_SENSOR_EVENT;
    }
}
