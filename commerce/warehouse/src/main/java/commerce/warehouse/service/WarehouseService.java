package commerce.warehouse.service;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.warehouse.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface WarehouseService {
    ProductDto createProduct(NewProductInWarehouseRequest request);

    ProductsDimensionsInfo checkCart(ShoppingCartDto cart);

    void addQuantity(AddProductToWarehouseRequest request);

    BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest request);

    void shippedToDelivery(ShippedToDeliveryRequest request);

    void acceptReturn(@RequestBody Map<String, Integer> products);

    AddressDto getAddress();
}
