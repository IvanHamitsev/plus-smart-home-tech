package commerce.delivery.service;

import commerce.delivery.mapper.DeliveryMapper;
import commerce.delivery.model.Delivery;
import commerce.delivery.repository.DeliveryRepository;
import commerce.interaction.dto.delivery.DeliveryDto;
import commerce.interaction.dto.delivery.DeliveryState;
import commerce.interaction.dto.order.OrderDto;
import commerce.interaction.dto.warehouse.ShippedToDeliveryRequest;
import commerce.interaction.exception.NoDeliveryFoundException;
import commerce.interaction.exception.ValidationException;
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
        log.info("Get request for delivery from {} to {}", deliveryDto.getFromAddress(), deliveryDto.getToAddress());
        validateDelivery(deliveryDto);
        // перевести в статус созданных?
        deliveryDto.setState(DeliveryState.CREATED);
        var delivery = repository.save(DeliveryMapper.mapDeliveryDto(deliveryDto));
        log.trace("Delivery from {} to {} get status CREATED and saved to DB as {}",
                deliveryDto.getFromAddress(),
                deliveryDto.getToAddress(),
                delivery.getId().toString());
        return DeliveryMapper.mapDelivery(delivery);
    }

    @Override
    public void deliverySuccessful(String deliveryId) {
        var delivery = setDeliveryState(deliveryId, DeliveryState.DELIVERED);
        log.info("Delivery {} get status DELIVERED", deliveryId);
        // не забываем отметить это в заказе!
        orderFeign.deliveryOk(delivery.getOrderId());
        log.trace("Information of delivery {} send to Order", delivery.getOrderId());
    }

    @Override
    public void deliveryPicked(String deliveryId) {
        var delivery = setDeliveryState(deliveryId, DeliveryState.IN_PROGRESS);
        log.trace("Delivery {} get status IN_PROGRESS", deliveryId);
        // отметить в заказе
        orderFeign.assembly(delivery.getOrderId());
        log.trace("Send delivery {} to Order to assembly orderId {}", deliveryId, delivery.getOrderId());

        // сообщить на склад
        warehouseFeign.shippedToDelivery(ShippedToDeliveryRequest.builder()
                .deliveryId(deliveryId)
                .orderId(delivery.getOrderId())
                .build());
        log.trace("Send delivery {} to Warehouse shippedToDelivery", deliveryId);
        log.info("Delivery {} successful picked", deliveryId);
    }

    @Override
    public void deliveryFailed(String deliveryId) {
        var delivery = setDeliveryState(deliveryId, DeliveryState.FAILED);
        orderFeign.deliveryFailed(delivery.getOrderId());
        log.trace("Send delivery {} to Order with request deliveryFailed. OrderID {}", deliveryId, delivery.getOrderId());
        // что делать со складом?
        log.info("Delivery {} become DeliveryState.FAILED", deliveryId);
    }

    @Override
    public Double deliveryCost(OrderDto orderDto) {
        log.trace("Get request to calculate cost of delivery {}", orderDto.getDeliveryId());
        var delivery = repository.findById(UUID.fromString(orderDto.getDeliveryId())).orElseThrow(
                () -> new NoDeliveryFoundException("No delivery found Id = " + orderDto.getDeliveryId())
        );

        // Правило 0. Базовая стоимость
        double resultCost = baseCost;
        log.trace("Delivery {} Base cost {}", orderDto.getDeliveryId(), resultCost);
        // Правило 1. Для ADDRESS_2 цена доставки с коэффициентом 2
        if (delivery.getFrom().getCountry().equals("ADDRESS_2")) {
            resultCost = resultCost * addressRatio;
        }
        log.trace("Delivery {} cost after Rule 1 {}", orderDto.getDeliveryId(), resultCost);
        // Правило 2. Хрупкое с коэффициентом 1.2
        if (orderDto.getFragile()) {
            resultCost = resultCost * fragileRatio;
        }
        log.trace("Delivery {} cost after Rule 2 {}", orderDto.getDeliveryId(), resultCost);
        // Правило 3. Коэффициент для несоседних улиц доставки 1.2
        if (!(delivery.getFrom().getStreet().equals(delivery.getTo().getStreet()) &&
                delivery.getFrom().getCity().equals(delivery.getTo().getCity()))) {
            resultCost = resultCost * farRatio;
        }
        log.trace("Delivery {} cost after Rule 3 {}", orderDto.getDeliveryId(), resultCost);
        // Правило 4. Добавленная стоимость от веса и объёма
        resultCost += orderDto.getDeliveryWeight() * weightRatio;
        resultCost += orderDto.getDeliveryVolume() * volumeRatio;
        log.info("Calculate delivery {} cost as {}", orderDto.getDeliveryId(), resultCost);
        return resultCost;
    }

    private Delivery setDeliveryState(String deliveryId, DeliveryState state) {
        log.trace("Move delivery {} status to {} ...", deliveryId, state);
        var delivery = repository.findById(UUID.fromString(deliveryId)).orElseThrow(
                () -> new NoDeliveryFoundException("No delivery found Id = " + deliveryId)
        );
        delivery.setState(state);
        log.trace("Delivery {} status successful changed", deliveryId);
        return repository.save(delivery);
    }

    private void validateDelivery(DeliveryDto delivery) {
        if ((null == delivery) ||
                (null == delivery.getFromAddress()) ||
                (null == delivery.getToAddress())) {
            log.warn("Get incorrect delivery {}", delivery);
            throw new ValidationException("Incorrect delivery");
        }
        log.trace("Delivery {} from {} to {} correct",
                delivery.getDeliveryId(),
                delivery.getFromAddress(),
                delivery.getToAddress());
    }
}
