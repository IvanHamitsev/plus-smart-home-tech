package commerce.interaction.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductsDimensionsInfo {
    @NotNull
    @Min(0)
    Double deliveryWeight;
    @NotNull
    @Min(0)
    Double deliveryVolume;
    @NotNull
    Boolean fragile;
}
