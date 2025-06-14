package commerce.interaction.rest_api;

import commerce.interaction.dto.cart.ChangeQuantityRequest;
import commerce.interaction.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;

public interface ShoppingCartRestApi {
    ShoppingCartDto findCartByUsername(String username);

    ShoppingCartDto addProducts(String username, Map<String, Integer> products);

    void blockCart(String username);

    ShoppingCartDto removeProducts(String username, List<String> productsUuids);

    ShoppingCartDto changeQuantity(String username, ChangeQuantityRequest request);
}
