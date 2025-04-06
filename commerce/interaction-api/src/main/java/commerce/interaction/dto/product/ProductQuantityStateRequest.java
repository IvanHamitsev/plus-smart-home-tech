package commerce.interaction.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductQuantityStateRequest {
    @NotBlank
    String productId;
    @NotNull
    QuantityState quantityState;
}
