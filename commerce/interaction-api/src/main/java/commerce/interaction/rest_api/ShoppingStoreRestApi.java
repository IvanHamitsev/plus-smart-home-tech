package commerce.interaction.rest_api;

import commerce.interaction.dto.product.ProductCategory;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.product.ProductQuantityStateRequest;

import java.util.List;

public interface ShoppingStoreRestApi {

    List<ProductDto> findProductByCategory(ProductCategory category);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    ProductDto removeProduct(String productId);

    void setQuantityState(ProductQuantityStateRequest request);

    ProductDto findProductById(String productId);
}
