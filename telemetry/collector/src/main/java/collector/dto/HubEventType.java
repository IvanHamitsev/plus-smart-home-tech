package collector.dto;

public enum HubEventType {

    DEVICE_ADDED, DEVICE_REMOVED, SCENARIO_ADDED, SCENARIO_REMOVED;

    public static HubEventType from(String str) {
        return switch (str.toLowerCase()) {
            case "device_added" -> DEVICE_ADDED;
            case "device_removed" -> DEVICE_REMOVED;
            case "scenario_added" -> SCENARIO_ADDED;
            case "scenario_removed" -> SCENARIO_REMOVED;
            default -> null;
        };
    }
}
