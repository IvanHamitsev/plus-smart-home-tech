package collector.dto;

public class DeviceRemovedEvent extends HubEvent {

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
