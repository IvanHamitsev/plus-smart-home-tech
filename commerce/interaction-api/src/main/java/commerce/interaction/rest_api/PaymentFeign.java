package commerce.interaction.rest_api;

import commerce.interaction.dto.order.OrderDto;
import commerce.interaction.dto.payment.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentFeign extends PaymentRestApi {
    @PostMapping
    PaymentDto payment(@RequestBody OrderDto order);

    @PostMapping("/totalCost")
    Double getTotalCost(@RequestBody OrderDto order);

    @PostMapping("/refund")
    void paymentOk(@RequestBody String id);

    @PostMapping("/productCost")
    Double productCost(@RequestBody OrderDto order);

    @PostMapping("/failed")
    void paymentFailed(@RequestBody String id);
}
