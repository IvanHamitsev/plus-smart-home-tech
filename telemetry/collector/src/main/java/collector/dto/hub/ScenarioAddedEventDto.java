package collector.dto.hub;

import collector.dto.enums.HubEventTypeDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ScenarioAddedEventDto extends HubEventDto {

    @Size(min = 3)
    String name;

    @NotBlank
    List<ScenarioConditionDto> conditions;
    @NotBlank
    List<DeviceActionDto> actions;

    @Override
    public HubEventTypeDto getType() {
        return HubEventTypeDto.SCENARIO_ADDED;
    }
}
