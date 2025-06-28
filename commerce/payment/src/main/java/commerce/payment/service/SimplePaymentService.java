package commerce.payment.service;

import commerce.interaction.dto.order.OrderDto;
import commerce.interaction.dto.payment.PaymentDto;
import commerce.interaction.exception.NoOrderFoundException;
import commerce.interaction.exception.NotEnoughInfoInOrderToCalculateException;
import commerce.interaction.rest_api.OrderFeign;
import commerce.interaction.rest_api.ShoppingStoreFeign;
import commerce.payment.mapper.PaymentMapper;
import commerce.payment.model.Payment;
import commerce.payment.model.PaymentStatus;
import commerce.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@Setter
@RequiredArgsConstructor
@ConfigurationProperties
public class SimplePaymentService implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final OrderFeign orderFeign;
    private final ShoppingStoreFeign shoppingStoreFeign;

    // процент налога храним в конфигурации
    @Value("${taxPercent}")
    private Integer taxPercent;

    @Override
    public PaymentDto performPayment(OrderDto order) {
        if (null == order.getOrderId()) {
            throw new NoOrderFoundException("No enough information in order dto");
        }

        var payment = Payment.builder()
                .orderId(UUID.fromString(order.getOrderId()))
                .productCost(order.getProductPrice().doubleValue())
                .deliveryCost(order.getDeliveryPrice().doubleValue())
                .totalCost(order.getTotalPrice().doubleValue())
                .status(PaymentStatus.PENDING)
                .build();

        payment = paymentRepository.save(payment);

        return PaymentMapper.mapPayment(payment);
    }

    @Override
    public Double getTotalCost(OrderDto order) {
        if (order.getDeliveryPrice() == null || order.getDeliveryPrice().doubleValue() <= 0 ||
                order.getProductPrice() == null || order.getProductPrice().doubleValue() <= 0) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    "No enough information in order dto to calculate order price");
        }

        return order.getDeliveryPrice().doubleValue() + order.getProductPrice().doubleValue()
                + order.getProductPrice().doubleValue() * taxPercent / 100.0; // налог налагается на цену продукта, не суммарную
    }

    @Override
    public Double getProductCost(OrderDto order) {
        // пустой заказ не стоит 0, он просто некорректен
        if (order.getProducts() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("No products in order!");
        }

        return order.getProducts().entrySet().parallelStream()
                .map(e ->
                        shoppingStoreFeign.findProductById(e.getKey()).getPrice().doubleValue() * e.getValue())
                .reduce(0.0, Double::sum);
    }

    @Override
    public void setPaymentSuccess(String id) {
        var payment = paymentRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NoOrderFoundException("No payment with Id " + id)
        );

        payment.setStatus(PaymentStatus.SUCCESS);
        payment = paymentRepository.save(payment);
        orderFeign.paymentOk(payment.getOrderId().toString());
    }

    @Override
    public void setPaymentFailed(String id) {
        var payment = paymentRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NoOrderFoundException("No payment with Id " + id)
        );

        payment.setStatus(PaymentStatus.FAILED);
        payment = paymentRepository.save(payment);
        orderFeign.paymentFailed(payment.getOrderId().toString());
    }
}
