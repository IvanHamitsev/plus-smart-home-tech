package commerce.payment.service;

import commerce.interaction.dto.order.OrderDto;
import commerce.interaction.dto.payment.PaymentDto;

public interface PaymentService {
    public PaymentDto performPayment(OrderDto order);

    public Double getTotalCost(OrderDto order);

    public Double getProductCost(OrderDto order);

    public void setPaymentSuccess(String id);

    public void setPaymentFailed(String id);
}
