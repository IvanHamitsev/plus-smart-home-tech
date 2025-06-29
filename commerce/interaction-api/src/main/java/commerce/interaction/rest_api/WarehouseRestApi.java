package commerce.interaction.rest_api;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.warehouse.*;

import java.util.Map;

public interface WarehouseRestApi {

    ProductsDimensionsInfo checkCart(ShoppingCartDto shoppingCartDto);

    ProductDto createProduct(NewProductInWarehouseRequest request);

    void addQuantity(AddProductToWarehouseRequest request);

    BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request);

    void acceptReturn(Map<String, Integer> products);

    void shippedToDelivery(ShippedToDeliveryRequest request);

    AddressDto getAddress();
}
