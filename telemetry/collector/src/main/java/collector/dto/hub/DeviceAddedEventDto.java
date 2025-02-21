package collector.dto.hub;

import collector.dto.enums.HubEventTypeDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceAddedEventDto extends HubEventDto {
    @NotBlank
    String deviceType;
    @Override
    public HubEventTypeDto getType() {
        return HubEventTypeDto.DEVICE_ADDED;
    }
}
