package commerce.interaction.rest_api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import commerce.interaction.dto.delivery.DeliveryDto;
import commerce.interaction.dto.order.OrderDto;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryFeign extends DeliveryRestApi {
    @Override
    @PutMapping
    DeliveryDto planDelivery(@RequestBody DeliveryDto delivery);

    @Override
    @PostMapping("/successful")
    void deliverySuccessful(@RequestBody String id);

    @Override
    @PostMapping("/picked")
    void deliveryPicked(@RequestBody String id);

    @Override
    @PostMapping("/failed")
    void deliveryFailed(@RequestBody String id);

    @Override
    @PostMapping("/cost")
    Double getDeliveryCost(@RequestBody OrderDto order);
}
