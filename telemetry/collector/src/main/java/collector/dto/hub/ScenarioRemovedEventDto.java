package collector.dto.hub;

import collector.dto.InputEventDto;
import collector.dto.enums.InputEventTypeDto;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ScenarioRemovedEventDto extends InputEventDto {
    @Size(min = 3)
    String name;

    @Override
    public InputEventTypeDto getType() {
        return InputEventTypeDto.SCENARIO_REMOVED_EVENT;
    }
}
