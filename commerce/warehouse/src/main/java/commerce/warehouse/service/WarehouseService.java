package commerce.warehouse.service;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.warehouse.AddProductToWarehouseRequest;
import commerce.interaction.dto.warehouse.AddressDto;
import commerce.interaction.dto.warehouse.NewProductInWarehouseRequest;
import commerce.interaction.dto.warehouse.ProductsDimensionsInfo;

public interface WarehouseService {
    ProductDto createProduct(NewProductInWarehouseRequest request);

    ProductsDimensionsInfo checkCart(ShoppingCartDto cart);

    void addQuantity(AddProductToWarehouseRequest request);

    AddressDto getAddress();
}
