package commerce.order.service;

import commerce.interaction.dto.order.CreateNewOrderRequest;
import commerce.interaction.dto.order.OrderState;
import commerce.interaction.dto.order.ProductReturnRequest;
import commerce.interaction.dto.order.OrderDto;

import java.util.List;

public interface OrderService {
    public List<OrderDto> getClientOrders(String username);

    public OrderDto createOrder(CreateNewOrderRequest request);

    public OrderDto productReturn(ProductReturnRequest request);

    public OrderDto paymentResult(String orderId, OrderState paymentState);

    public OrderDto deliveryResult(String orderId, OrderState orderState);

    public OrderDto orderComplete(String orderId);

    public OrderDto calculateTotalCost(String orderId);

    public OrderDto calculateDeliveryCost(String orderId);

    public OrderDto assemblyOk(String orderId);

    public OrderDto assemblyFailed(String orderId);
}
