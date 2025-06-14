package commerce.warehouse.service;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.product.QuantityState;
import commerce.interaction.dto.warehouse.AddProductToWarehouseRequest;
import commerce.interaction.dto.warehouse.AddressDto;
import commerce.interaction.dto.warehouse.NewProductInWarehouseRequest;
import commerce.interaction.dto.warehouse.ProductsDimensionsInfo;
import commerce.interaction.exception.NoSpecifiedProductInWarehouseException;
import commerce.interaction.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import commerce.interaction.exception.SpecifiedProductAlreadyInWarehouseException;
import commerce.warehouse.model.WarehouseMapper;
import commerce.warehouse.repository.WarehouseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class SimpleWarehouseService implements WarehouseService {

    private final WarehouseRepository repository;
    // взаимодействовать с shopping store через feign клиент
    private final LocalShoppingStoreFeign shoppingStoreFeign;

    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    public ProductDto createProduct(NewProductInWarehouseRequest request) {
        if (repository.existsById(UUID.fromString(request.getProductId()))) {
            throw new SpecifiedProductAlreadyInWarehouseException(String.format("Product %s already stored", request.getProductId()));
        }
        var product = repository.save(WarehouseMapper.mapRequest(request));
        return WarehouseMapper.mapProductInWarehouseToProductDto(product);
    }

    @Override
    public ProductsDimensionsInfo checkCart(ShoppingCartDto cart) {

        if (cart.getProducts().entrySet().parallelStream()
                .filter(element -> {
                            var askedQuantity = element.getValue();
                            var product = repository.findById(UUID.fromString(element.getKey()));
                            if (product.isPresent()) {
                                return askedQuantity > product.get().getQuantity();
                            } else {
                                // если вообще нет такого товара - тоже плохой случай
                                return true;
                            }
                        }
                )
                .findAny().isEmpty()) {
            var productsList = repository.findAllById(cart.getProducts().keySet().parallelStream()
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
        var product = repository.findById(UUID.fromString(request.getProductId()))
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("No product with id " + request.getProductId()));

        product.setQuantity(product.getQuantity() + request.getQuantity()); // добавить дополнительное количество, или установить заданное в request количество?
        repository.save(product);
        // и сообщить в магазин, о новом количестве
        try {
            // возможно товар ещё не добавлен в витрину, магазин про него не знает
            shoppingStoreFeign.setQuantityState(request.getProductId(), QuantityState.getFromInteger(product.getQuantity()).toString());
        } catch (RuntimeException e) {
            log.info("Данный товар {} ещё не выставлен в магазине", request.getProductId());
        }
    }

    @Override
    public AddressDto getAddress() {
        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }
}
