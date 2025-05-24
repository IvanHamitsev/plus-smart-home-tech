package commerce.shopping_cart.service;

import commerce.interaction.dto.cart.ChangeQuantityRequest;
import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.exception.ForbiddenException;
import commerce.interaction.exception.NotFoundException;
import commerce.shopping_cart.mapper.CartMapper;
import commerce.shopping_cart.model.Cart;
import commerce.shopping_cart.repository.ShoppingCartRepository;
import commerce.warehouse.service.WarehouseService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleShoppingCartService implements ShoppingCartService {

    private ShoppingCartRepository cartRepository;
    // обращения на склад
    private WarehouseService warehouseService;

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
            warehouseService.checkCart(cartDto);
            return cartDto;
        } else {
            throw new ForbiddenException(String.format("Cart of user %s is blocked", username));
        }
    }

    @Override
    public void changeCartActivity(String username, boolean way) {
        var cart = cartRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException(String.format("No cart for user %s exists", username)));
        cart.setIsActivate(way);
        cartRepository.save(cart);
    }

    @Override
    public ShoppingCartDto resetProducts(String username, Map<String, Integer> productsMap) {
        var cart = cartRepository.findByUsername(username).orElseThrow(
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
        warehouseService.checkCart(CartMapper.mapCart(cart));

        cart.setProducts(productQuantityList);
        cartRepository.save(cart);
        return CartMapper.mapCart(cart);
    }

    private Cart getOrCreateCart(String username) {
        return cartRepository.findByUsername(username).orElse(
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
