package collector.dto.enums;

public enum SensorEventType {
    CLIMATE_SENSOR_EVENT, LIGHT_SENSOR_EVENT, MOTION_SENSOR_EVENT, SWITCH_SENSOR_EVENT, TEMPERATURE_SENSOR_EVENT;

    public static SensorEventType from(String str) {
        return switch (str.toLowerCase()) {
            case "climate_sensor_event" -> CLIMATE_SENSOR_EVENT;
            case "light_sensor_event" -> LIGHT_SENSOR_EVENT;
            case "motion_sensor_event" -> MOTION_SENSOR_EVENT;
            case "switch_sensor_event" -> SWITCH_SENSOR_EVENT;
            case "temperature_sensor_event" -> TEMPERATURE_SENSOR_EVENT;
            default -> null;
        };
    }
}
