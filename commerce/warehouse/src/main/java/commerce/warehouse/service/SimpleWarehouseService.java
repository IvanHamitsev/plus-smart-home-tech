package commerce.warehouse.service;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.product.QuantityState;
import commerce.interaction.dto.warehouse.*;
import commerce.interaction.exception.NoOrderFoundException;
import commerce.interaction.exception.NoSpecifiedProductInWarehouseException;
import commerce.interaction.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import commerce.interaction.exception.SpecifiedProductAlreadyInWarehouseException;
import commerce.interaction.rest_api.ShoppingStoreFeign;
import commerce.warehouse.mapper.WarehouseMapper;
import commerce.warehouse.model.OrderBooking;
import commerce.warehouse.model.ProductInWarehouse;
import commerce.warehouse.repository.WarehouseBookingRepository;
import commerce.warehouse.repository.WarehouseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SimpleWarehouseService implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseBookingRepository warehouseBookingRepository;

    private final ShoppingStoreFeign shoppingStoreFeign;

    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    public ProductDto createProduct(NewProductInWarehouseRequest request) {
        if (warehouseRepository.existsById(UUID.fromString(request.getProductId()))) {
            throw new SpecifiedProductAlreadyInWarehouseException(String.format("Product %s already stored", request.getProductId()));
        }
        var product = warehouseRepository.save(WarehouseMapper.mapRequest(request));
        return WarehouseMapper.mapProductInWarehouseToProductDto(product);
    }

    @Override
    public ProductsDimensionsInfo checkCart(ShoppingCartDto cart) {

        if (cart.getProducts().entrySet().parallelStream()
                .filter(element -> {
                            var askedQuantity = element.getValue();
                            var product = warehouseRepository.findById(UUID.fromString(element.getKey()));
                            if (product.isPresent()) {
                                return askedQuantity > product.get().getQuantity();
                            } else {
                                // если вообще нет такого товара - тоже плохой случай
                                return true;
                            }
                        }
                )
                .findAny().isEmpty()) {
            var productsList = warehouseRepository.findAllById(cart.getProducts().keySet().parallelStream()
                    .map(UUID::fromString).toList());
            boolean sumFragile = false;
            double sumWeight = 0;
            double sumVolume = 0;

            for (var product : productsList) {
                sumFragile |= product.getFragile();
                sumWeight += product.getWeight();
                // суммарный объём товаров конечно не есть объём посылки ...
                sumVolume += product.getWidth() * product.getHeight() * product.getDepth();
            }
            return new ProductsDimensionsInfo(sumWeight, sumVolume, sumFragile);
        } else {
            throw new ProductInShoppingCartLowQuantityInWarehouseException("Not enough product in warehouse");
        }
    }

    @Override
    public void addQuantity(AddProductToWarehouseRequest request) {
        var product = warehouseRepository.findById(UUID.fromString(request.getProductId()))
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("No product with id " + request.getProductId()));

        product.setQuantity(product.getQuantity() + request.getQuantity()); // добавить дополнительное количество, или установить заданное в request количество?
        warehouseRepository.save(product);
        // и сообщить в магазин, о новом количестве
        try {
            // возможно товар ещё не добавлен в витрину, магазин про него не знает
            shoppingStoreFeign.setQuantityState(request.getProductId(), QuantityState.getFromInteger(product.getQuantity()).toString());
        } catch (RuntimeException e) {
            log.info("Данный товар {} ещё не выставлен в магазине", request.getProductId());
        }
    }

    public BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest request) {
        var productsList = warehouseRepository.findAllById(
                request.getProducts().keySet().stream().map(UUID::fromString).toList());
        var quantitiesMap = productsList.stream()
                .collect(Collectors.toMap(ProductInWarehouse::getProductId, ProductInWarehouse::getQuantity));
        var lowQuantity = request.getProducts().keySet().stream()
                .filter(e -> request.getProducts().get(e) > quantitiesMap.get(UUID.fromString(e)))
                .findFirst();
        if (lowQuantity.isPresent()) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException(
                    String.format("Low quantity of product %s in warehouse", lowQuantity.get()));
        }

        productsList = productsList.parallelStream()
                .map(p -> {
                    p.setQuantity(p.getQuantity() - quantitiesMap.get(p.getProductId()));
                    return p;
                })
                .toList();
        warehouseRepository.saveAll(productsList);
        warehouseBookingRepository.save(OrderBooking.builder()
                .bookingId(UUID.fromString(request.getOrderId()))
                .products(productsList)
                .build());

        // а теперь сложить параметры заказа
        boolean sumFragile = false;
        double sumWeight = 0;
        double sumVolume = 0;

        for (var product : productsList) {
            sumFragile |= product.getFragile();
            sumWeight += product.getWeight();
            sumVolume += product.getWidth() * product.getHeight() * product.getDepth();
        }

        return BookedProductsDto.builder()
                .fragile(sumFragile)
                .deliveryVolume(sumVolume)
                .deliveryWeight(sumWeight)
                .build();
    }

    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        var booking = warehouseBookingRepository.findById(UUID.fromString(request.getOrderId())).orElseThrow(
                () -> new NoOrderFoundException("No order found " + request.getOrderId())
        );
        // Id доставки нам сообщили!
        booking.setDeliveryId(request.getDeliveryId());
        warehouseBookingRepository.save(booking);
    }

    public void acceptReturn(Map<String, Integer> products) {
        var productsList = warehouseRepository.findAllById(products.keySet().stream().map(UUID::fromString).toList());
        productsList = productsList.parallelStream()
                .map(p -> {
                    p.setQuantity(p.getQuantity() - products.get(p.getProductId().toString()));
                    return p;
                })
                .toList();
        warehouseRepository.saveAll(productsList);
    }

    @Override
    public AddressDto getAddress() {
        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }
}
