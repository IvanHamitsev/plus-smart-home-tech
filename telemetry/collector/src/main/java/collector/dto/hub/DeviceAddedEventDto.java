package collector.dto.hub;

import collector.dto.InputEventDto;
import collector.dto.enums.InputEventTypeDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceAddedEventDto extends InputEventDto {
    @NotBlank
    String deviceType;

    @Override
    public InputEventTypeDto getType() {
        return InputEventTypeDto.DEVICE_ADDED_EVENT;
    }
}
