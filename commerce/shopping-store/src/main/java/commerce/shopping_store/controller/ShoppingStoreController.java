package commerce.shopping_store.controller;

import commerce.interaction.dto.product.ProductCategory;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.product.ProductQuantityStateRequest;
import commerce.shopping_store.service.ShoppingStoreService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shopping-store")
@AllArgsConstructor
public class ShoppingStoreController {
    private final ShoppingStoreService service;

    @GetMapping
    public List<ProductDto> findProductByCategory(@RequestBody ProductCategory category) {
        return service.findProductByCategory(category);
    }

    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        return service.createProduct(productDto);
    }

    @PostMapping
    // если не применять @Valid, можно не заполнять часть свойств
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return service.updateProduct(productDto);
    }

    // помним, что удаление не настоящее, а установка признака недоступности
    @PostMapping("/removeProductFromStore")
    public void removeProduct(@RequestBody String productId) {
        service.removeProduct(productId);
    }

    @PostMapping("/quantityState")
    public void setQuantityState(@RequestBody @Valid ProductQuantityStateRequest request) {
        service.setQuantityState(request);
    }

    @GetMapping("/{productId}")
    public ProductDto findProductById(@PathVariable String productId) {
        return service.findProductById(productId);
    }
}
