package collector.dto.hub;

import collector.dto.enums.HubEventType;
import lombok.Data;

@Data
public class DeviceRemovedEvent extends HubEvent {

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
