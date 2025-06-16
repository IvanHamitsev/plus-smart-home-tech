package commerce.shopping_store.controller;

import commerce.interaction.dto.product.ProductCategory;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.product.ProductQuantityStateRequest;
import commerce.interaction.dto.product.QuantityState;
import commerce.interaction.rest_api.ShoppingStoreRestApi;
import commerce.shopping_store.service.ShoppingStoreService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shopping-store")
@AllArgsConstructor
public class ShoppingStoreController implements ShoppingStoreRestApi {
    private final ShoppingStoreService service;

    @Override
    @GetMapping("/{productId}")
    public ProductDto findProductById(@PathVariable String productId) {
        return service.findProductById(productId);
    }

    @Override
    @GetMapping
    public Page<ProductDto> findProductByCategory(
            @RequestParam("category") ProductCategory category,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sort) {
        return service.findProductByCategory(category, page, size, sort);
    }

    @Override
    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        var retValue = service.createProduct(productDto);
        return retValue;
    }

    @Override
    @PostMapping
    // если не применять @Valid, можно не заполнять часть свойств
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return service.updateProduct(productDto);
    }

    // помним, что удаление не настоящее, а установка признака недоступности
    @Override
    @PostMapping("/removeProductFromStore")
    public ProductDto removeProduct(@RequestBody String productId) {
        return service.removeProduct(productId);
    }

    @Override
    @PostMapping("/quantityState")
    public ProductDto setQuantityState(@RequestParam String productId, @RequestParam String quantityState) {
        ProductQuantityStateRequest request = new ProductQuantityStateRequest(productId, QuantityState.valueOf(quantityState));
        return service.setQuantityState(request);
    }
}
