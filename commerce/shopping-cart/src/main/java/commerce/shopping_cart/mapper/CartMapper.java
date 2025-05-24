package commerce.shopping_cart.mapper;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.shopping_cart.model.Cart;
import commerce.shopping_cart.model.ProductQuantity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CartMapper {
    public static ShoppingCartDto mapCart(Cart cart) {
        var productsList = cart.getProducts().stream()
                .collect(Collectors.toMap(ProductQuantity::getProductId, ProductQuantity::getQuantity));
        // неверно! нужно имя, а не Id
        return ShoppingCartDto.builder()
                .shoppingCartId(cart.getCartId())
                .products(productsList)
                .build();
    }

    public static Cart mapShoppingCartDto(ShoppingCartDto cartDto, String ownerName, boolean isActive) {
        var productsList = mapMapToListProducts(cartDto.getProducts());

        return Cart.builder()
                .cartId(cartDto.getShoppingCartId())
                .owner(ownerName)
                .products(productsList)
                .isActivate(isActive)
                .build();
    }

    public static List<ProductQuantity> mapMapToListProducts(Map<String, Integer> products) {
        return products.entrySet().stream()
                .map(element -> ProductQuantity.builder()
                        .productId(element.getKey())
                        .quantity(element.getValue())
                        .build())
                .toList();
    }
}
