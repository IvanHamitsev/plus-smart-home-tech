package collector.dto.hub;

import lombok.Data;

@Data
public class DeviceActionDto {
    private String sensorId;
    private String type; // Enum ActionTypeDto
    private Integer value;
}
