package commerce.interaction.rest_api;

import commerce.interaction.dto.order.CreateNewOrderRequest;
import commerce.interaction.dto.order.OrderDto;
import commerce.interaction.dto.order.ProductReturnRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderFeign extends OrderRestApi {
    @Override
    @GetMapping
    public List<OrderDto> getClientOrders(@RequestParam String username);

    @Override
    @PutMapping
    public OrderDto createNewOrder(@RequestBody CreateNewOrderRequest request);

    @Override
    @PostMapping("/return")
    public OrderDto productReturn(@RequestBody ProductReturnRequest request);

    @Override
    @PostMapping("/payment")
    public OrderDto paymentOk(@RequestParam String orderId);

    @Override
    @PostMapping("/payment/failed")
    public OrderDto paymentFailed(@RequestParam String orderId);

    @Override
    @PostMapping("/delivery")
    public OrderDto deliveryOk(@RequestParam String orderId);

    @Override
    @PostMapping("/delivery/failed")
    public OrderDto deliveryFailed(@RequestParam String orderId);

    @Override
    @PostMapping("/completed")
    public OrderDto complete(@RequestParam String orderId);

    @Override
    @PostMapping("/calculate/total")
    public OrderDto calculateTotalCost(@RequestParam String orderId);

    @Override
    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryCost(@RequestParam String orderId);

    @Override
    @PostMapping("/assembly")
    public OrderDto assembly(@RequestParam String orderId);

    @Override
    @PostMapping("/assembly/failed")
    public OrderDto assemblyFailed(@RequestParam String orderId);
}
