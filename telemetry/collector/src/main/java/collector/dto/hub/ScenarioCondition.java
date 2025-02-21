package collector.dto.hub;

import lombok.Data;

@Data
public class ScenarioCondition {
    String sensorId;
    String type; // Enum ScenarioConditionType
    String operation; // Enum ScenarioOperationType
    Integer value;
}
