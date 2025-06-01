package commerce.warehouse.controller;

import commerce.interaction.dto.warehouse.AddProductToWarehouseRequest;
import commerce.interaction.dto.warehouse.AddressDto;
import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.warehouse.NewProductInWarehouseRequest;
import commerce.interaction.dto.warehouse.ProductsDimensionsInfo;
import commerce.warehouse.service.WarehouseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouse")
@AllArgsConstructor
public class WarehouseController {
    private final WarehouseService service;

    @PutMapping
    void createProduct(@RequestBody NewProductInWarehouseRequest request) {
        service.createProduct(request);
    }

    @PostMapping("/check")
    ProductsDimensionsInfo checkCart(@RequestBody ShoppingCartDto cart) {
        return service.checkCart(cart);
    }

    @PostMapping("/add")
    void addQuantity(@RequestBody AddProductToWarehouseRequest request) {
        service.addQuantity(request);
    }

    @GetMapping("/address")
    AddressDto getAddress() {
        return service.getAddress();
    }
}
