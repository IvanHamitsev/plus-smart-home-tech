package collector.dto.hub;

import collector.dto.enums.HubEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ScenarioAddedEvent extends HubEvent {

    @Size(min = 3)
    String name;

    @NotBlank
    List<ScenarioCondition> conditions;
    @NotBlank
    List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
