package commerce.interaction.rest_api;

import commerce.interaction.dto.product.ProductCategory;
import commerce.interaction.dto.product.ProductDto;

import org.springframework.data.domain.Page;

public interface ShoppingStoreRestApi {

    Page<ProductDto> findProductByCategory(ProductCategory category, Integer page, Integer size, String sort);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    ProductDto removeProduct(String productId);

    //void setQuantityState(ProductQuantityStateRequest request);
    ProductDto setQuantityState(String productId, String quantityState);

    ProductDto findProductById(String productId);
}
