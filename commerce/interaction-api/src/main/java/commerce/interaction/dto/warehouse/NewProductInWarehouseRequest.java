package commerce.interaction.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewProductInWarehouseRequest {
    @NotBlank
    String productId;
    Boolean fragile;
    @NotNull
    DimensionDto dimension;
    @Min(0)
    @NotNull
    Double weight;
}
