package commerce.warehouse.controller;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.warehouse.AddProductToWarehouseRequest;
import commerce.interaction.dto.warehouse.AddressDto;
import commerce.interaction.dto.warehouse.NewProductInWarehouseRequest;
import commerce.interaction.dto.warehouse.ProductsDimensionsInfo;
import commerce.interaction.rest_api.WarehouseRestApi;
import commerce.warehouse.service.WarehouseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouse")
@AllArgsConstructor
public class WarehouseController implements WarehouseRestApi {
    private final WarehouseService service;

    @Override
    @PutMapping
    public ProductDto createProduct(@RequestBody NewProductInWarehouseRequest request) {
        return service.createProduct(request);
    }

    @Override
    @PostMapping("/check")
    public ProductsDimensionsInfo checkCart(@RequestBody ShoppingCartDto cart) {
        return service.checkCart(cart);
    }

    @Override
    @PostMapping("/add")
    public void addQuantity(@RequestBody AddProductToWarehouseRequest request) {
        service.addQuantity(request);
    }

    @Override
    @GetMapping("/address")
    public AddressDto getAddress() {
        return service.getAddress();
    }
}
