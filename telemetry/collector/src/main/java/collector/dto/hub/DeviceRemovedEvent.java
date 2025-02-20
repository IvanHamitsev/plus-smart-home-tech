package collector.dto.hub;

import collector.dto.enums.HubEventType;

public class DeviceRemovedEvent extends HubEvent {

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
