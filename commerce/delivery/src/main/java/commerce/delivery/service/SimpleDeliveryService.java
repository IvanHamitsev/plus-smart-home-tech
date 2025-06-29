package commerce.delivery.service;

import commerce.delivery.mapper.DeliveryMapper;
import commerce.delivery.model.Delivery;
import commerce.delivery.repository.DeliveryRepository;
import commerce.interaction.dto.delivery.DeliveryDto;
import commerce.interaction.dto.delivery.DeliveryState;
import commerce.interaction.dto.order.OrderDto;
import commerce.interaction.dto.warehouse.ShippedToDeliveryRequest;
import commerce.interaction.exception.NoDeliveryFoundException;
import commerce.interaction.rest_api.OrderFeign;
import commerce.interaction.rest_api.WarehouseFeign;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "delivery-price")
public class SimpleDeliveryService implements DeliveryService {

    private Double baseCost;
    private Double addressRatio;
    private Double fragileRatio;
    private Double farRatio;
    private Double weightRatio;
    private Double volumeRatio;

    private final DeliveryRepository repository;

    private final OrderFeign orderFeign;
    private final WarehouseFeign warehouseFeign;

    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        // перевести в статус созданных?
        deliveryDto.setState(DeliveryState.CREATED);
        var delivery = repository.save(DeliveryMapper.mapDeliveryDto(deliveryDto));
        return DeliveryMapper.mapDelivery(delivery);
    }

    @Override
    public void deliverySuccessful(String deliveryId) {
        var delivery = setDeliveryState(deliveryId, DeliveryState.DELIVERED);
        // не забываем отметить это в заказе!
        orderFeign.deliveryOk(delivery.getOrderId());
    }

    @Override
    public void deliveryPicked(String deliveryId) {
        var delivery = setDeliveryState(deliveryId, DeliveryState.IN_PROGRESS);
        // отметить в заказе
        orderFeign.assembly(delivery.getOrderId());
        // сообщить на склад
        warehouseFeign.shippedToDelivery(ShippedToDeliveryRequest.builder()
                .deliveryId(deliveryId)
                .orderId(delivery.getOrderId())
                .build());
    }

    @Override
    public void deliveryFailed(String deliveryId) {
        var delivery = setDeliveryState(deliveryId, DeliveryState.FAILED);
        orderFeign.deliveryFailed(delivery.getOrderId());
        // что делать со складом?
    }

    @Override
    public Double deliveryCost(OrderDto orderDto) {
        var delivery = repository.findById(UUID.fromString(orderDto.getDeliveryId())).orElseThrow(
                () -> new NoDeliveryFoundException("No delivery found Id = " + orderDto.getDeliveryId())
        );

        // Правило 0. Базовая стоимость
        double resultCost = baseCost;

        // Правило 1. Для ADDRESS_2 цена доставки с коэффициентом 2
        if (delivery.getFrom().getCountry().equals("ADDRESS_2")) {
            resultCost = resultCost * addressRatio;
        }

        // Правило 2. Хрупкое с коэффициентом 1.2
        if (orderDto.getFragile()) {
            resultCost = resultCost * fragileRatio;
        }

        // Правило 3. Коэффициент для несоседних улиц доставки 1.2
        if (!(delivery.getFrom().getStreet().equals(delivery.getTo().getStreet()) &&
                delivery.getFrom().getCity().equals(delivery.getTo().getCity()))) {
            resultCost = resultCost * farRatio;
        }

        // Правило 4. Добавленная стоимость от веса и объёма
        resultCost += orderDto.getDeliveryWeight() * weightRatio;
        resultCost += orderDto.getDeliveryVolume() * volumeRatio;

        return resultCost;
    }

    private Delivery setDeliveryState(String deliveryId, DeliveryState state) {
        var delivery = repository.findById(UUID.fromString(deliveryId)).orElseThrow(
                () -> new NoDeliveryFoundException("No delivery found Id = " + deliveryId)
        );
        delivery.setState(state);
        return repository.save(delivery);
    }
}
