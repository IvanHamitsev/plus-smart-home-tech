package collector.dto.hub;

import collector.dto.enums.HubEventTypeDto;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = HubEventTypeDto.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedEventDto.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedEventDto.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioAddedEventDto.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioRemovedEventDto.class, name = "SCENARIO_REMOVED")
})

@Data
public abstract class HubEventDto {
    @NotBlank
    private String id;
    @NotBlank
    private String hubId;
    private Instant timestamp = Instant.now();
    private HubEventTypeDto type;

    @NotNull
    public abstract HubEventTypeDto getType();
}
