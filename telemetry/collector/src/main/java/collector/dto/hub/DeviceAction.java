package collector.dto.hub;

import lombok.Data;

@Data
public class DeviceAction {
    private String sensorId;
    private String type; // Enum ActionType
    private Integer value;
}
