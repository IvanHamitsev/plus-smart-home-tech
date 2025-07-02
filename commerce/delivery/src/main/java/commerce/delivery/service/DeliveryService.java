package commerce.delivery.service;

import commerce.interaction.dto.delivery.DeliveryDto;
import commerce.interaction.dto.order.OrderDto;

public interface DeliveryService {
    public DeliveryDto planDelivery(DeliveryDto deliveryDto);

    public void deliverySuccessful(String deliveryId);

    public void deliveryPicked(String id);

    public void deliveryFailed(String id);

    public Double deliveryCost(OrderDto order);
}
