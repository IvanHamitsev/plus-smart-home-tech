package commerce.interaction.rest_api;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.warehouse.AddProductToWarehouseRequest;
import commerce.interaction.dto.warehouse.AddressDto;
import commerce.interaction.dto.warehouse.NewProductInWarehouseRequest;
import commerce.interaction.dto.warehouse.ProductsDimensionsInfo;

public interface WarehouseRestApi {

    ProductsDimensionsInfo checkCart(ShoppingCartDto shoppingCartDto);

    ProductDto createProduct(NewProductInWarehouseRequest request);

    void addQuantity(AddProductToWarehouseRequest request);

    AddressDto getAddress();
}
