package collector.dto.hub;

import collector.dto.enums.HubEventTypeDto;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ScenarioRemovedEventDto extends HubEventDto {
    @Size(min = 3)
    String name;

    @Override
    public HubEventTypeDto getType() {
        return HubEventTypeDto.SCENARIO_REMOVED;
    }
}
