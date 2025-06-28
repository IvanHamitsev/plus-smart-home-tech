package commerce.order.mapper;

import commerce.interaction.dto.order.OrderDto;
import commerce.order.model.Order;
import commerce.order.model.OrderProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDto mapOrder(Order order) {
        return OrderDto.builder()
                .orderId(order.getId().toString())
                .shoppingCartId(order.getShoppingCartId().toString())
                .paymentId(order.getPaymentId().toString())
                .deliveryId(order.getDeliveryId().toString())
                .state(order.getState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.getFragile())
                .totalPrice(BigDecimal.valueOf(order.getTotalPrice()))
                .deliveryPrice(BigDecimal.valueOf(order.getDeliveryPrice()))
                .productPrice(BigDecimal.valueOf(order.getProductPrice()))
                .products(mapOrderProductList(order.getProducts()))
                .build();
    }

    public static Map<String, Integer> mapOrderProductList(List<OrderProduct> products) {
        return products.stream()
                .collect(Collectors.toMap(e -> e.getProductId().toString(), e -> e.getQuantity()));
    }

    public static List<OrderProduct> mapToOrderProductList(Map<String, Integer> inp) {
        return inp.entrySet().stream()
                //.collect(Collectors.toList(OrderProduct.builder().productId(UUID.fromString(e -> e.getKey())).quantity(e -> e.getValue()).build()));
                .map(e ->
                        OrderProduct.builder()
                                .productId(UUID.fromString(e.getKey()))
                                .quantity(e.getValue())
                                .build())
                .toList();
    }
}
