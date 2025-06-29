package commerce.delivery.controller;

import commerce.delivery.service.DeliveryService;
import commerce.interaction.dto.delivery.DeliveryDto;
import commerce.interaction.dto.order.OrderDto;
import commerce.interaction.rest_api.DeliveryRestApi;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/v1/delivery")
@AllArgsConstructor
public class DeliveryController implements DeliveryRestApi {

    private final DeliveryService service;

    @Override
    @PutMapping
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        return service.planDelivery(deliveryDto);
    }

    @Override
    @PostMapping("/successful")
    public void deliverySuccessful(String deliveryId) {
        service.deliverySuccessful(deliveryId);
    }

    @Override
    @PostMapping("/picked")
    public void deliveryPicked(String deliveryId) {
        service.deliveryPicked(deliveryId);
    }

    @Override
    @PostMapping("/fail")
    public void deliveryFailed(String deliveryId) {
        service.deliveryFailed(deliveryId);
    }

    @Override
    @PostMapping("/cost")
    public Double getDeliveryCost(OrderDto orderDto) {
        return service.deliveryCost(orderDto);
    }
}
