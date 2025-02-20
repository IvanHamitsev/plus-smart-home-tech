package collector.dto.hub;

import collector.dto.enums.HubEventType;
import jakarta.validation.constraints.NotBlank;

public class DeviceAddedEvent extends HubEvent {
    @NotBlank
    String deviceType;
    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
