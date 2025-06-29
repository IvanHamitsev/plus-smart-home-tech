package commerce.delivery.mapper;

import commerce.delivery.model.Address;
import commerce.delivery.model.Delivery;
import commerce.interaction.dto.delivery.DeliveryDto;
import commerce.interaction.dto.warehouse.AddressDto;

import java.util.UUID;

public class DeliveryMapper {
    public static DeliveryDto mapDelivery(Delivery inp) {
        return DeliveryDto.builder()
                .deliveryId(inp.getId().toString())
                .state(inp.getState())
                .orderId(inp.getOrderId())
                .fromAddress(mapAddress(inp.getFrom()))
                .toAddress(mapAddress(inp.getTo()))
                .build();
    }

    public static Delivery mapDeliveryDto(DeliveryDto inp) {
        return Delivery.builder()
                .id(UUID.fromString(inp.getDeliveryId()))
                .state(inp.getState())
                .orderId(inp.getOrderId())
                .from(mapAddressDto(inp.getFromAddress()))
                .to(mapAddressDto(inp.getToAddress()))
                .build();
    }

    public static AddressDto mapAddress(Address inp) {
        return AddressDto.builder()
                .country(inp.getCountry())
                .city(inp.getCity())
                .street(inp.getStreet())
                .house(inp.getHouse())
                .flat(inp.getFlat())
                .build();
    }

    private static Address mapAddressDto(AddressDto inp) {
        return Address.builder()
                .country(inp.getCountry())
                .city(inp.getCity())
                .street(inp.getStreet())
                .house(inp.getHouse())
                .flat(inp.getFlat())
                .build();
    }
}
