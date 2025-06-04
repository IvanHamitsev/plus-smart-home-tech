package commerce.interaction.rest_api;

import commerce.interaction.dto.cart.ChangeQuantityRequest;
import commerce.interaction.dto.cart.ShoppingCartDto;

import java.util.Map;

public interface ShoppingCartRestApi {
    ShoppingCartDto findCartByUsername(String username);

    ShoppingCartDto addProducts(String username, Map<String, Integer> products);

    void blockCart(String username);

    ShoppingCartDto removeProducts(String username, Map<String, Integer> products);

    ShoppingCartDto changeQuantity(String username, ChangeQuantityRequest request);
}
