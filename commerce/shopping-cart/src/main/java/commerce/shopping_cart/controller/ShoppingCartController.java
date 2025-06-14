package commerce.shopping_cart.controller;

import commerce.interaction.dto.cart.ChangeQuantityRequest;
import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.rest_api.ShoppingCartRestApi;
import commerce.shopping_cart.service.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@AllArgsConstructor
public class ShoppingCartController implements ShoppingCartRestApi {
    private final ShoppingCartService service;

    @GetMapping
    @Override
    public ShoppingCartDto findCartByUsername(@RequestParam String username) {
        return service.findCartByUsername(username);
    }

    @PutMapping
    @Override
    public ShoppingCartDto addProducts(@RequestParam String username, @RequestBody(required = false) Map<String, Integer> products) {
        return service.addProducts(username, products);
    }

    @DeleteMapping
    @Override
    public void blockCart(@RequestParam String username) {
        service.changeCartActivity(username, false);
    }

    @PostMapping("/remove")
    @Override
    public ShoppingCartDto removeProducts(@RequestParam String username, @RequestBody List<String> productsUuids) {
        return service.resetProducts(username, productsUuids);
    }

    @PostMapping("/change-quantity")
    @Override
    public ShoppingCartDto changeQuantity(@RequestParam String username, @RequestBody @Valid ChangeQuantityRequest request) {
        return service.changeQuantity(username, request);
    }
}
