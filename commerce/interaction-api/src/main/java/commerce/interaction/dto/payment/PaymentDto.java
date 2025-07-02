package commerce.interaction.dto.payment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {
    String paymentId;
    Double totalPayment;
    Double deliveryTotal;
    Double feeTotal; // стоимость налога
}
