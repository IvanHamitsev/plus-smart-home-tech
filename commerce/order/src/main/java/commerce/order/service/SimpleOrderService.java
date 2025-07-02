package commerce.order.service;

import commerce.interaction.dto.delivery.DeliveryDto;
import commerce.interaction.dto.order.CreateNewOrderRequest;
import commerce.interaction.dto.order.OrderDto;
import commerce.interaction.dto.order.OrderState;
import commerce.interaction.dto.order.ProductReturnRequest;
import commerce.interaction.dto.warehouse.AddressDto;
import commerce.interaction.dto.warehouse.AssemblyProductsForOrderRequest;
import commerce.interaction.dto.warehouse.BookedProductsDto;
import commerce.interaction.exception.NoOrderFoundException;
import commerce.interaction.exception.NotAuthorizedUserException;
import commerce.interaction.rest_api.DeliveryFeign;
import commerce.interaction.rest_api.PaymentFeign;
import commerce.interaction.rest_api.WarehouseFeign;
import commerce.order.mapper.OrderMapper;
import commerce.order.model.Order;
import commerce.order.model.OrderProduct;
import commerce.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Setter
@Slf4j
@AllArgsConstructor
public class SimpleOrderService implements OrderService {

    private final OrderRepository repository;

    private final WarehouseFeign warehouseFeign;
    private final PaymentFeign paymentFeign;
    private final DeliveryFeign deliveryFeign;

    @Override
    public List<OrderDto> getClientOrders(String username) {
        var order = repository.findByUsername(username);
        if (order.isEmpty()) {
            throw new NotAuthorizedUserException(String.format("No user with name '%s' find", username));
        }
        return order.parallelStream()
                .map(OrderMapper::mapOrder)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        // создать сущности
        Order order = createNewOrder(request.getShoppingCart().getProducts());
        DeliveryDto delivery = createOrderDelivery(order.getId().toString(), request.getDeliveryAddress());
        BookedProductsDto booking = createWarehouseBooking(order.getId().toString(), order.getProducts());

        // заполнить заказ
        order = fillOrderDetails(order, booking, delivery);

        // отправить оплату
        paymentFeign.payment(OrderMapper.mapOrder(order));
        return OrderMapper.mapOrder(order);
    }

    @Override
    public OrderDto productReturn(ProductReturnRequest request) {
        var order = getExistingOrder(request.getOrderId());
        // сформировать новый лист продуктов в заказе
        var newProductList = order.getProducts().stream()
                // сначала peek-ом вычесть необходимое количество
                .peek(e -> {
                            var deductedQuantity = request.getProducts().get(e.getProductId().toString());
                            if (null != deductedQuantity) {
                                e.setQuantity(e.getQuantity() - deductedQuantity);
                            }
                        }
                )
                // затем filter-ом убрать нулевое и отрицательное количество
                .filter(e -> e.getQuantity() > 0)
                .toList();
        // при работе peek-ом мы меняли экземпляр листа самого order
        // order.setProducts(newProductList);

        var booking = warehouseFeign.assemblyProductsForOrder(AssemblyProductsForOrderRequest.builder()
                .orderId(order.getId().toString())
                .products(OrderMapper.mapOrderProductList(order.getProducts()))
                .build());

        order.setDeliveryVolume(booking.getDeliveryVolume());
        order.setDeliveryWeight(booking.getDeliveryWeight());
        order.setFragile(booking.getFragile());
        order.setState(OrderState.PRODUCT_RETURNED);
        log.info("Order {} get status PRODUCT_RETURNED", order.getId().toString());
        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto paymentResult(String orderId, OrderState paymentState) {
        var order = getExistingOrder(orderId);
        order.setState(paymentState);
        log.info("Payment for order {} get status {}", order.getId().toString(), paymentState);
        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto deliveryResult(String orderId, OrderState orderState) {
        var order = getExistingOrder(orderId);
        order.setState(orderState);
        log.info("Delivery for order {} get status {}", orderId, orderState);

        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto orderComplete(String orderId) {
        var order = getExistingOrder(orderId);
        order.setState(OrderState.COMPLETED);
        log.info("Order {} get state COMPLETE", orderId);
        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto calculateTotalCost(String orderId) {
        var order = getExistingOrder(orderId);
        order.setTotalPrice(paymentFeign.getTotalCost(OrderMapper.mapOrder(order)));
        log.info("Calculate total cost {} for order {}", order.getTotalPrice(), orderId);
        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto calculateDeliveryCost(String orderId) {
        var order = getExistingOrder(orderId);
        order.setDeliveryPrice(deliveryFeign.getDeliveryCost(OrderMapper.mapOrder(order)));
        log.info("Calculate cost of delivery {} for order {}", order.getDeliveryPrice(), orderId);
        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto assemblyOk(String orderId) {
        var order = getExistingOrder(orderId);
        order.setState(OrderState.ASSEMBLED);
        log.info("Order {} get status ASSEMBLED", orderId);
        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto assemblyFailed(String orderId) {
        var order = getExistingOrder(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        log.info("Order {} get status ASSEMBLY_FAILED", orderId);
        return OrderMapper.mapOrder(repository.save(order));
    }

    private Order getExistingOrder(String orderId) {
        return repository.findById(UUID.fromString(orderId)).orElseThrow(
                () -> new NoOrderFoundException(String.format("No order with Id %s in database", orderId))
        );
    }

    private Order createNewOrder(Map<String, Integer> productsMap) {
        Order order = Order.builder()
                .products(OrderMapper.mapToOrderProductList(productsMap))
                .state(OrderState.NEW)
                .build();
        return repository.save(order);
    }

    private DeliveryDto createOrderDelivery(String orderId, AddressDto recipient) {
        return deliveryFeign.planDelivery(DeliveryDto.builder()
                .fromAddress(warehouseFeign.getAddress())
                .toAddress(recipient)
                .orderId(orderId)
                .build());
    }

    private BookedProductsDto createWarehouseBooking(String orderId, List<OrderProduct> productsList) {
        return warehouseFeign.assemblyProductsForOrder(AssemblyProductsForOrderRequest.builder()
                .orderId(orderId)
                .products(OrderMapper.mapOrderProductList(productsList))
                .build());
    }

    private Order fillOrderDetails(Order order, BookedProductsDto booking, DeliveryDto delivery) {
        order.setDeliveryVolume(booking.getDeliveryVolume());
        order.setDeliveryWeight(booking.getDeliveryWeight());
        order.setFragile(booking.getFragile());
        order.setProductPrice(paymentFeign.productCost(OrderMapper.mapOrder(order)));
        order.setDeliveryPrice(deliveryFeign.getDeliveryCost(OrderMapper.mapOrder(order)));
        order.setTotalPrice(paymentFeign.getTotalCost(OrderMapper.mapOrder(order)));
        order.setDeliveryId(UUID.fromString(delivery.getDeliveryId()));
        return repository.save(order);
    }
}
