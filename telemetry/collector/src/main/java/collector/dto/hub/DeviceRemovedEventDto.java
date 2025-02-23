package collector.dto.hub;

import collector.dto.InputEventDto;
import collector.dto.enums.InputEventTypeDto;
import lombok.Data;

@Data
public class DeviceRemovedEventDto extends InputEventDto {

    @Override
    public InputEventTypeDto getType() {
        return InputEventTypeDto.DEVICE_REMOVED;
    }
}
