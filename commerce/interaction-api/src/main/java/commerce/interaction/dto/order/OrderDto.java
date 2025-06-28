package commerce.interaction.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    @NotBlank
    String orderId;
    String shoppingCartId;
    @NotNull
    Map<String, Integer> products;
    String paymentId;
    String deliveryId;
    OrderState state;
    Double deliveryWeight;
    Double deliveryVolume;
    Boolean fragile;
    @Min(0)
    BigDecimal totalPrice;
    @Min(0)
    BigDecimal deliveryPrice;
    @Min(0)
    BigDecimal productPrice;
}
