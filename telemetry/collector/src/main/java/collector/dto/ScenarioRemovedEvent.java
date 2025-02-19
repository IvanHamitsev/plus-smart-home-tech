package collector.dto;

import jakarta.validation.constraints.Size;

public class ScenarioRemovedEvent extends HubEvent {
    @Size(min = 3)
    String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
