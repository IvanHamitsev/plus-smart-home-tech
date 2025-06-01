package commerce.shopping_cart.service;

import commerce.interaction.dto.cart.ChangeQuantityRequest;
import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.exception.ForbiddenException;
import commerce.interaction.exception.NotFoundException;
import commerce.interaction.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import commerce.shopping_cart.mapper.CartMapper;
import commerce.shopping_cart.model.Cart;
import commerce.shopping_cart.repository.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class SimpleShoppingCartService implements ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    // обращения на склад
    private final LocalShoppingCartFeignClient shoppingCartFeignClient;

    @Override
    public ShoppingCartDto findCartByUsername(String username) {
        var cart = getOrCreateCart(username);
        return CartMapper.mapCart(cart);
    }

    @Override
    public ShoppingCartDto addProducts(String username, Map<String, Integer> products) {
        var cart = getOrCreateCart(username);
        if (cart.getIsActivate()) {
            cart.getProducts().addAll(CartMapper.mapMapToListProducts(products));
            cartRepository.save(cart);
            var cartDto = CartMapper.mapCart(cart);
            // надо проверить доступность на складе
            // Feign клиент сам не сгенерит исключение ProductInShoppingCartLowQuantityInWarehouseException
            if (null == shoppingCartFeignClient.checkCart(cartDto)) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException(
                        String.format("Cart of user %s contains low quantity products", username));
            }
            return cartDto;
        } else {
            throw new ForbiddenException(String.format("Cart of user %s is blocked", username));
        }
    }

    @Override
    public void changeCartActivity(String username, boolean way) {
        var cart = cartRepository.findByOwner(username).orElseThrow(
                () -> new NotFoundException(String.format("No cart for user %s exists", username)));
        cart.setIsActivate(way);
        cartRepository.save(cart);
    }

    @Override
    public ShoppingCartDto resetProducts(String username, Map<String, Integer> productsMap) {
        var cart = cartRepository.findByOwner(username).orElseThrow(
                () -> new NotFoundException(String.format("No cart for user %s exists", username)));

        var productsList = CartMapper.mapMapToListProducts(productsMap);

        if (!cart.getProducts().containsAll(productsList)) {
            throw new NotFoundException(String.format("Not oll of products in user %s cart", username));
        }
        // мы не удаляем, а задаём поправленный список
        cart.setProducts(productsList);
        cartRepository.save(cart);
        return CartMapper.mapCart(cart);
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeQuantityRequest request) {
        var cart = getOrCreateCart(username);
        AtomicBoolean listContainsProduct = new AtomicBoolean(false);
        var productQuantityList = cart.getProducts().stream()
                .map(element -> {
                    if (element.getProductId().equals(request.getProductId())) {
                        element.setQuantity(request.getNewQuantity());
                        listContainsProduct.set(true);
                    }
                    return element;
                })
                .toList();

        if (!listContainsProduct.get()) {
            throw new NotFoundException(String.format("No productId %s in cart of user %s", request.getProductId(), username));
        }
        // и проверить доступность на складе
        //warehouseService.checkCart(CartMapper.mapCart(cart));
        if (null == shoppingCartFeignClient.checkCart(CartMapper.mapCart(cart))) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException(
                    String.format("Cart of user %s contains low quantity products", username));
        }
        cart.setProducts(productQuantityList);
        cartRepository.save(cart);
        return CartMapper.mapCart(cart);
    }

    private Cart getOrCreateCart(String username) {
        return cartRepository.findByOwner(username).orElse(
                // если нет корзины, создать
                Cart.builder()
                        // где взять ID корзины?
                        .cartId(UUID.randomUUID().toString())
                        .owner(username)
                        .products(List.of())
                        .isActivate(true)
                        .build()
        );
    }
}
