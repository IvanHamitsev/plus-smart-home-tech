package commerce.order.service;

import commerce.interaction.dto.delivery.DeliveryDto;
import commerce.interaction.dto.order.CreateNewOrderRequest;
import commerce.interaction.dto.order.OrderDto;
import commerce.interaction.dto.order.OrderState;
import commerce.interaction.dto.order.ProductReturnRequest;
import commerce.interaction.dto.warehouse.AssemblyProductsForOrderRequest;
import commerce.interaction.exception.NoOrderFoundException;
import commerce.interaction.exception.NotAuthorizedUserException;
import commerce.interaction.rest_api.DeliveryFeign;
import commerce.interaction.rest_api.PaymentFeign;
import commerce.interaction.rest_api.WarehouseFeign;
import commerce.order.mapper.OrderMapper;
import commerce.order.model.Order;
import commerce.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public OrderDto createNewOrder(CreateNewOrderRequest request) {
        var order = Order.builder()
                .products(OrderMapper.mapToOrderProductList(request.getShoppingCart().getProducts()))
                .state(OrderState.NEW)
                .build();

        var deliveryDto = deliveryFeign.planDelivery(DeliveryDto.builder()
                // а нужен ли адрес from ? по сути заглушка
                .fromAddress(warehouseFeign.getAddress())
                .toAddress(request.getDeliveryAddress())
                .orderId(order.getId().toString())
                .build());

        var booking = warehouseFeign.assemblyProductsForOrder(AssemblyProductsForOrderRequest.builder()
                .orderId(order.getId().toString())
                .products(OrderMapper.mapOrderProductList(order.getProducts()))
                .build());

        order.setDeliveryVolume(booking.getDeliveryVolume());
        order.setDeliveryWeight(booking.getDeliveryWeight());
        order.setFragile(booking.getFragile());
        order.setProductPrice(paymentFeign.productCost(OrderMapper.mapOrder(order)));
        order.setDeliveryPrice(deliveryFeign.getDeliveryCost(OrderMapper.mapOrder(order)));
        order.setTotalPrice(paymentFeign.getTotalCost(OrderMapper.mapOrder(order)));
        order.setDeliveryId(UUID.fromString(deliveryDto.getDeliveryId()));
        // при сохранении заказа получим Id
        order = repository.save(order);
        // отправить оплату
        paymentFeign.payment(OrderMapper.mapOrder(order));
        return OrderMapper.mapOrder(order);
    }

    @Override
    public OrderDto productReturn(ProductReturnRequest request) {
        var order = repository.findById(UUID.fromString(request.getOrderId())).orElseThrow(
                () -> new NoOrderFoundException("No order with Id=" + request.getOrderId())
        );
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

        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto paymentResult(String orderId, OrderState paymentState) {
        var order = repository.findById(UUID.fromString(orderId)).orElseThrow(
                () -> new NoOrderFoundException("No order with Id " + orderId)
        );
        order.setState(paymentState);
        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto deliveryResult(String orderId, OrderState orderState) {
        var order = repository.findById(UUID.fromString(orderId)).orElseThrow(
                () -> new NoOrderFoundException("No order with Id " + orderId)
        );
        order.setState(orderState);
        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto orderComplete(String orderId) {
        var order = repository.findById(UUID.fromString(orderId)).orElseThrow(
                () -> new NoOrderFoundException("No order with Id " + orderId)
        );
        // нужна проверка правил перехода из одного состояния в другое
        order.setState(OrderState.COMPLETED);
        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto calculateTotalCost(String orderId) {
        Order order = repository.findById(UUID.fromString(orderId)).orElseThrow(
                () -> new NoOrderFoundException("No order with Id " + orderId)
        );
        order.setTotalPrice(paymentFeign.getTotalCost(OrderMapper.mapOrder(order)));
        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto calculateDeliveryCost(String orderId) {
        Order order = repository.findById(UUID.fromString(orderId)).orElseThrow(
                () -> new NoOrderFoundException("No order with Id " + orderId)
        );
        order.setDeliveryPrice(deliveryFeign.getDeliveryCost(OrderMapper.mapOrder(order)));
        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto assemblyOk(String orderId) {
        var order = repository.findById(UUID.fromString(orderId)).orElseThrow(
                () -> new NoOrderFoundException("No order with Id " + orderId)
        );
        order.setState(OrderState.ASSEMBLED);
        return OrderMapper.mapOrder(repository.save(order));
    }

    @Override
    public OrderDto assemblyFailed(String orderId) {
        var order = repository.findById(UUID.fromString(orderId)).orElseThrow(
                () -> new NoOrderFoundException("No order with Id " + orderId)
        );
        order.setState(OrderState.ASSEMBLY_FAILED);
        return OrderMapper.mapOrder(repository.save(order));
    }
}
