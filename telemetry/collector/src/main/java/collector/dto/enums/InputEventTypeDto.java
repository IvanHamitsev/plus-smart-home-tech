package collector.dto.enums;

public enum InputEventTypeDto {
    // Sensors
    CLIMATE_SENSOR_EVENT, LIGHT_SENSOR_EVENT, MOTION_SENSOR_EVENT, SWITCH_SENSOR_EVENT, TEMPERATURE_SENSOR_EVENT,
    // Hubs
    DEVICE_ADDED, DEVICE_REMOVED, SCENARIO_ADDED, SCENARIO_REMOVED;

    public static InputEventTypeDto from(String str) {
        return switch (str.toLowerCase()) {
            // Sensors
            case "climate_sensor_event" -> CLIMATE_SENSOR_EVENT;
            case "light_sensor_event" -> LIGHT_SENSOR_EVENT;
            case "motion_sensor_event" -> MOTION_SENSOR_EVENT;
            case "switch_sensor_event" -> SWITCH_SENSOR_EVENT;
            case "temperature_sensor_event" -> TEMPERATURE_SENSOR_EVENT;
            // Hubs
            case "device_added" -> DEVICE_ADDED;
            case "device_removed" -> DEVICE_REMOVED;
            case "scenario_added" -> SCENARIO_ADDED;
            case "scenario_removed" -> SCENARIO_REMOVED;
            default -> null;
        };
    }
}
