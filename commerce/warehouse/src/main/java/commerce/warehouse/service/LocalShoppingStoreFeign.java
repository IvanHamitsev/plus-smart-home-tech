package commerce.warehouse.service;

import commerce.interaction.dto.product.ProductCategory;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.product.ProductQuantityStateRequest;
import commerce.interaction.rest_api.ShoppingStoreRestApi;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "warehouse")
public interface LocalShoppingStoreFeign extends ShoppingStoreRestApi {
    @Override
    @GetMapping
    public List<ProductDto> findProductByCategory(@RequestBody ProductCategory category);

    @Override
    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto);

    @Override
    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto);

    @Override
    @PostMapping("/removeProductFromStore")
    public ProductDto removeProduct(@RequestBody String productId);

    @Override
    @PostMapping("/quantityState")
    public void setQuantityState(@RequestBody @Valid ProductQuantityStateRequest request);

    @Override
    @GetMapping("/{productId}")
    public ProductDto findProductById(@PathVariable String productId);
}
