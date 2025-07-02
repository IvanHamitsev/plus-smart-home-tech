package commerce.payment.mapper;

import commerce.interaction.dto.payment.PaymentDto;
import commerce.payment.model.Payment;

public class PaymentMapper {
    public static PaymentDto mapPayment(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getId().toString())
                .totalPayment(payment.getTotalCost())
                .deliveryTotal(payment.getDeliveryCost())
                .feeTotal(payment.getTotalCost() - payment.getDeliveryCost() - payment.getProductCost()) // налог
                .build();
    }
}
