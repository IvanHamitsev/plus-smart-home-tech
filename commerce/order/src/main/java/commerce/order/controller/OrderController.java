package commerce.order.controller;

import commerce.interaction.dto.order.CreateNewOrderRequest;
import commerce.interaction.dto.order.OrderDto;
import commerce.interaction.dto.order.OrderState;
import commerce.interaction.dto.order.ProductReturnRequest;
import commerce.interaction.rest_api.OrderRestApi;
import commerce.order.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
@Slf4j
public class OrderController implements OrderRestApi {

    private final OrderService service;

    @Override
    @GetMapping
    public List<OrderDto> getClientOrders(@RequestParam String username) {
        log.info("Request of client orders: {}", username);
        return service.getClientOrders(username);
    }

    @Override
    @PutMapping
    public OrderDto createNewOrder(@RequestBody CreateNewOrderRequest request) {
        log.info("Create new order. Cart {} ", request.getShoppingCart().getShoppingCartId());
        return service.createNewOrder(request);
    }

    @Override
    @PostMapping("/return")
    public OrderDto productReturn(@RequestBody ProductReturnRequest request) {
        log.info("Return product for order {}", request.getOrderId());
        return service.productReturn(request);
    }

    @Override
    @PostMapping("/payment")
    public OrderDto paymentOk(@RequestParam String orderId) {
        log.info("Payment order: {}", orderId);
        return service.paymentResult(orderId, OrderState.PAID);
    }

    @Override
    @PostMapping("/payment/failed")
    public OrderDto paymentFailed(@RequestParam String orderId) {
        log.info("Payment fail for order {}", orderId);
        return service.paymentResult(orderId, OrderState.PAYMENT_FAILED);
    }

    @Override
    @PostMapping("/delivery")
    public OrderDto deliveryOk(@RequestParam String orderId) {
        log.info("Success delivery order {}", orderId);
        return service.deliveryResult(orderId, OrderState.DELIVERED);
    }

    @Override
    @PostMapping("/delivery/failed")
    public OrderDto deliveryFailed(@RequestParam String orderId) {
        log.info("Fail delivery order {}", orderId);
        return service.deliveryResult(orderId, OrderState.DELIVERY_FAILED);
    }

    @Override
    @PostMapping("/completed")
    public OrderDto complete(@RequestParam String orderId) {
        log.info("Complete order: {}", orderId);
        return service.orderComplete(orderId);
    }

    @Override
    @PostMapping("/calculate/total")
    public OrderDto calculateTotalCost(@RequestParam String orderId) {
        log.info("Request to calculate order: {}", orderId);
        return service.calculateTotalCost(orderId);
    }

    @Override
    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryCost(@RequestParam String orderId) {
        log.info("Request to calculate delivery cost for order: {}", orderId);
        return service.calculateDeliveryCost(orderId);
    }

    @Override
    @PostMapping("/assembly")
    public OrderDto assembly(@RequestParam String orderId) {
        log.info("Request to assembly order: {}", orderId);
        return service.assemblyOk(orderId);
    }

    @Override
    @PostMapping("/assembly/failed")
    public OrderDto assemblyFailed(@RequestParam String orderId) {
        log.info("Request to fail assembly for order: {}", orderId);
        return service.assemblyFailed(orderId);
    }
}
