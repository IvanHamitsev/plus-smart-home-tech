package collector.dto.hub;

import collector.dto.enums.HubEventType;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ScenarioRemovedEvent extends HubEvent {
    @Size(min = 3)
    String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
