package commerce.interaction.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DimensionDto {
    @Min(0)
    @NotNull
    Double width;
    @Min(0)
    @NotNull
    Double height;
    @Min(0)
    @NotNull
    Double depth;
}
