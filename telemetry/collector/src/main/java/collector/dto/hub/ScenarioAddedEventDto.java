package collector.dto.hub;

import collector.dto.InputEventDto;
import collector.dto.enums.InputEventTypeDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ScenarioAddedEventDto extends InputEventDto {

    @Size(min = 3)
    String name;

    @NotBlank
    List<ScenarioConditionDto> conditions;
    @NotBlank
    List<DeviceActionDto> actions;

    @Override
    public InputEventTypeDto getType() {
        return InputEventTypeDto.SCENARIO_ADDED_EVENT;
    }
}
