package commerce.interaction.rest_api;

import commerce.interaction.dto.delivery.DeliveryDto;
import commerce.interaction.dto.order.OrderDto;

public interface DeliveryRestApi {
    DeliveryDto planDelivery(DeliveryDto delivery);

    void deliverySuccessful(String id);

    void deliveryPicked(String id);

    void deliveryFailed(String id);

    Double getDeliveryCost(OrderDto order);
}
