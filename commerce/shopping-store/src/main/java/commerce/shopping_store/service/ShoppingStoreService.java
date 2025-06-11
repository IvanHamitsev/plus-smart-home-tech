package commerce.shopping_store.service;

import commerce.interaction.dto.product.ProductCategory;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.product.ProductQuantityStateRequest;
import commerce.interaction.exception.NotFoundException;

import java.util.List;

public interface ShoppingStoreService {
    List<ProductDto> findProductByCategory(ProductCategory productCategory, Integer page, Integer size, String sort);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto) throws NotFoundException;

    ProductDto removeProduct(String productId);

    ProductDto setQuantityState(ProductQuantityStateRequest request);

    ProductDto findProductById(String productId);
}
