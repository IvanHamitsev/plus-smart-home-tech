package commerce.interaction.dto.order;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.warehouse.AddressDto;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateNewOrderRequest {
    @NotNull
    AddressDto deliveryAddress;
    @NotNull
    ShoppingCartDto shoppingCart;
}
