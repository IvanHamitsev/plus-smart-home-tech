package commerce.interaction.dto.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeQuantityRequest {
    @NotBlank
    private String productId;
    @NotNull
    private Integer newQuantity;
}
