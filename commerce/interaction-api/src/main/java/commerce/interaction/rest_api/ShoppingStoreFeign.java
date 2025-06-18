package commerce.interaction.rest_api;

import commerce.interaction.dto.product.ProductCategory;
import commerce.interaction.dto.product.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreFeign extends ShoppingStoreRestApi {
    @Override
    @GetMapping
    public Page<ProductDto> findProductByCategory(@RequestParam("category") ProductCategory category,
                                                  @RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer size,
                                                  @RequestParam(required = false) String sortField);

    @Override
    @PutMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto);

    @Override
    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto);

    @Override
    @PostMapping("/removeProductFromStore")
    public ProductDto removeProduct(@RequestBody String productId);

    @Override
    @PostMapping("/quantityState")
    public ProductDto setQuantityState(@RequestParam String productId, @RequestParam String quantityState);

    @Override
    @GetMapping("/{productId}")
    public ProductDto findProductById(@PathVariable String productId);
}
