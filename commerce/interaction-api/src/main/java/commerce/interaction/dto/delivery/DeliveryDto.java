package commerce.interaction.dto.delivery;

import commerce.interaction.dto.warehouse.AddressDto;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryDto {
    String deliveryId;
    @NotNull
    DeliveryState state;
    @NotNull
    String orderId;
    @NotNull
    AddressDto fromAddress;
    @NotNull
    AddressDto toAddress;
}
