package commerce.interaction.rest_api;

import commerce.interaction.dto.order.OrderDto;
import commerce.interaction.dto.payment.PaymentDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface PaymentRestApi {
    PaymentDto payment(@RequestBody OrderDto order);

    void paymentOk(@RequestBody String id);

    void paymentFailed(@RequestBody String id);

    Double productCost(@RequestBody OrderDto order);

    Double getTotalCost(@RequestBody OrderDto order);
}
