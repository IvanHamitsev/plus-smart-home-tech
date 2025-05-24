package commerce.warehouse.service;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.product.ProductQuantityStateRequest;
import commerce.interaction.dto.product.QuantityState;
import commerce.interaction.dto.warehouse.AddProductToWarehouseRequest;
import commerce.interaction.dto.warehouse.AddressDto;
import commerce.interaction.dto.warehouse.NewProductInWarehouseRequest;
import commerce.interaction.dto.warehouse.ProductsDimensionsInfo;
import commerce.interaction.exception.NoSpecifiedProductInWarehouseException;
import commerce.interaction.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import commerce.interaction.exception.SpecifiedProductAlreadyInWarehouseException;
import commerce.shopping_store.service.ShoppingStoreService;
import commerce.warehouse.model.WarehouseMapper;
import commerce.warehouse.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SimpleWarehouseService implements WarehouseService {

    private final WarehouseRepository repository;
    // как правильно взаимодействовать с shopping store ?
    ShoppingStoreService shoppingStoreService;

    @Override
    @Transactional
    public void createProduct(NewProductInWarehouseRequest request) {
        if (repository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException(String.format("Product %s already stored", request.getProductId()));
        }
        repository.save(WarehouseMapper.mapRequest(request));
    }

    @Override
    public ProductsDimensionsInfo checkCart(ShoppingCartDto cart) {
        if (cart.getProducts().entrySet().parallelStream()
                .filter(element ->
                        element.getValue() > repository.getQuantityById(element.getKey())
                )
                .findAny().isEmpty()) {
            var productsList = repository.findAllById(cart.getProducts().keySet());
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
        var product = repository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("No product with id " + request.getProductId()));

        product.setQuantity(product.getQuantity() + request.getQuantity());
        repository.save(product);
        shoppingStoreService.setQuantityState(new ProductQuantityStateRequest(request.getProductId(),
                QuantityState.getFromInteger(product.getQuantity())));
    }

    @Override
    public AddressDto getAddress() {
        // откуда брать адрес?
        return new AddressDto("Country", "City", "Street", "25", "5");
    }
}
