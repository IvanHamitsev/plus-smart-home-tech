package commerce.interaction.rest_api;

import commerce.interaction.dto.order.CreateNewOrderRequest;
import commerce.interaction.dto.order.OrderDto;
import commerce.interaction.dto.order.ProductReturnRequest;

import java.util.List;

public interface OrderRestApi {
    List<OrderDto> getClientOrders(String username);

    OrderDto createNewOrder(CreateNewOrderRequest request);

    OrderDto productReturn(ProductReturnRequest request);

    OrderDto paymentOk(String orderId);

    OrderDto paymentFailed(String orderId);

    OrderDto deliveryOk(String orderId);

    OrderDto deliveryFailed(String orderId);

    OrderDto complete(String orderId);

    OrderDto calculateTotalCost(String orderId);

    OrderDto calculateDeliveryCost(String orderId);

    OrderDto assembly(String orderId);

    OrderDto assemblyFailed(String orderId);
}
