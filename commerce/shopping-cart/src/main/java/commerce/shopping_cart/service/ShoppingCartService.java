package commerce.shopping_cart.service;

import commerce.interaction.dto.cart.ChangeQuantityRequest;
import commerce.interaction.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    ShoppingCartDto findCartByUsername(String username);

    ShoppingCartDto addProducts(String username, Map<String, Integer> products);

    void changeCartActivity(String username, boolean way);

    ShoppingCartDto resetProducts(String username, List<String> products);

    ShoppingCartDto changeQuantity(String username, ChangeQuantityRequest request);
}
