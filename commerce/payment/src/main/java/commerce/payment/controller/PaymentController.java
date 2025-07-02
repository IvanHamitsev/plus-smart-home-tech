package commerce.payment.controller;

import commerce.interaction.dto.order.OrderDto;
import commerce.interaction.dto.payment.PaymentDto;
import commerce.interaction.rest_api.PaymentRestApi;
import commerce.payment.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@AllArgsConstructor
public class PaymentController implements PaymentRestApi {

    private final PaymentService service;

    @Override
    @PostMapping
    public PaymentDto payment(OrderDto order) {
        return service.performPayment(order);
    }

    @Override
    @PostMapping("/refund")
    public void paymentOk(String id) {
        service.setPaymentSuccess(id);
    }

    @Override
    @PostMapping("/failed")
    public void paymentFailed(String id) {
        service.setPaymentFailed(id);
    }

    @Override
    @PostMapping("/productCost")
    public Double productCost(OrderDto order) {
        return service.getProductCost(order);
    }

    @Override
    @PostMapping("/totalCost")
    public Double getTotalCost(OrderDto order) {
        return service.getTotalCost(order);
    }
}
