package collector.dto.hub;

import lombok.Data;

@Data
public class ScenarioConditionDto {
    String sensorId;
    String type; // Enum ScenarioConditionTypeDto
    String operation; // Enum ScenarioOperationTypeDto
    Integer value;
}
