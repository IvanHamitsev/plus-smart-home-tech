package commerce.interaction.dto.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ShoppingCartDto {
    @NotBlank
    private String shoppingCartId;
    @NotNull
    private Map<String, Integer> products;
}
