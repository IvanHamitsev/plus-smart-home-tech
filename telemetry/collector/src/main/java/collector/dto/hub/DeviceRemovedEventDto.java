package collector.dto.hub;

import collector.dto.enums.HubEventTypeDto;
import lombok.Data;

@Data
public class DeviceRemovedEventDto extends HubEventDto {

    @Override
    public HubEventTypeDto getType() {
        return HubEventTypeDto.DEVICE_REMOVED;
    }
}
