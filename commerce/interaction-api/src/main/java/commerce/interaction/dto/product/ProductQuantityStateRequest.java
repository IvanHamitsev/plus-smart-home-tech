package commerce.interaction.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductQuantityStateRequest {
    @NotNull
    String productId;
    @NotNull
    QuantityState quantityState;
}
