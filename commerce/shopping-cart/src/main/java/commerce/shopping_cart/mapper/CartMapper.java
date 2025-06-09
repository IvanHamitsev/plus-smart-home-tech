package commerce.shopping_cart.mapper;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.shopping_cart.model.Cart;
import commerce.shopping_cart.model.ProductQuantity;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class CartMapper {
    public static ShoppingCartDto mapCart(Cart cart) {
        var productsList = cart.getProducts().stream()
                .collect(Collectors.toMap(ProductQuantity::getProductId, ProductQuantity::getQuantity));
        // возможно имя, а не Id ?
        return ShoppingCartDto.builder()
                .shoppingCartId(cart.getCartId().toString()) // в DTO объектах ID, в соответствии с заданием, именно String
                .products(mapListProductsToMap(productsList))
                .build();
    }

    public static Cart mapShoppingCartDto(ShoppingCartDto cartDto, String ownerName, boolean isActive) {
        var productsList = mapMapToListProducts(cartDto.getProducts());

        return Cart.builder()
                // Id сгенерит БД
                .owner(ownerName)
                .products(productsList)
                .isActivate(isActive)
                .build();
    }

    public static List<ProductQuantity> mapMapToListProducts(Map<String, Integer> products) {
        return products.entrySet().stream()
                .map(element -> ProductQuantity.builder()
                        .productId(UUID.fromString(element.getKey())) // key не может быть null
                        .quantity(element.getValue())
                        .build())
                .toList();
    }

    public static Map<String, Integer> mapListProductsToMap(Map<UUID, Integer> products) {
        // втупую
        /*Map<String, Integer> resultMap = new HashMap<>();
        for (Map.Entry<UUID, Integer> element : products.entrySet()) {
            resultMap.put(element.getKey().toString(), element.getValue());
        }
        return resultMap;*/
        // стримом
        return products.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue()));
    }
}
