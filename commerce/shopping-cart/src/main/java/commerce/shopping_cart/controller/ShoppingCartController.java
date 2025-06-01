package commerce.shopping_cart.controller;

import commerce.interaction.dto.cart.ChangeQuantityRequest;
import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.shopping_cart.service.ShoppingCartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@AllArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService service;

    @GetMapping
    public ShoppingCartDto findCartByUsername(@NotBlank String username) {
        return service.findCartByUsername(username);
    }

    @PutMapping
    public ShoppingCartDto addProducts(@NotBlank String username, Map<String, Integer> products) {
        return service.addProducts(username, products);
    }

    @DeleteMapping
    public void blockCart(@NotBlank String username) {
        service.changeCartActivity(username, false);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeProducts(@NotBlank String username, Map<String, Integer> products) {
        return service.resetProducts(username, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeQuantity(@NotBlank String username, @Valid ChangeQuantityRequest request) {
        return service.changeQuantity(username, request);
    }
}
