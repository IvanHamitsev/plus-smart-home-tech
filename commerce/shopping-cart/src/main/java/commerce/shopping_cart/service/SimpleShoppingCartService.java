package commerce.shopping_cart.service;

import commerce.interaction.dto.cart.ChangeQuantityRequest;
import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.warehouse.ProductsDimensionsInfo;
import commerce.interaction.exception.ForbiddenException;
import commerce.interaction.exception.NotFoundException;
import commerce.interaction.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import commerce.shopping_cart.mapper.CartMapper;
import commerce.shopping_cart.model.Cart;
import commerce.shopping_cart.repository.ProductQuantityRepository;
import commerce.shopping_cart.repository.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class SimpleShoppingCartService implements ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final ProductQuantityRepository quantityRepository;
    // обращения на склад
    private final LocalWarehouseFeign warehouseFeign;

    @Override
    public ShoppingCartDto findCartByUsername(String username) {
        var cart = getOrCreateCart(username);
        return CartMapper.mapCart(cart);
    }

    @Override
    public ShoppingCartDto addProducts(String username, Map<String, Integer> products) {
        var cart = getOrCreateCart(username);
        if (cart.getIsActivate()) {
            var productsList = CartMapper.mapMapToListProducts(products);
            quantityRepository.saveAll(productsList);
            cart.getProducts().addAll(productsList);
            var cartDto = CartMapper.mapCart(cartRepository.save(cart));
            // надо проверить доступность на складе
            // Feign клиент сам не сгенерит исключение ProductInShoppingCartLowQuantityInWarehouseException
            try {
                var result = warehouseFeign.checkCart(cartDto);
            } catch (RuntimeException ex) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException(
                        String.format("Cart of user %s contains low quantity product", username));
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
    public ShoppingCartDto resetProducts(String username, List<String> productsUuids) {
        var cart = cartRepository.findByOwner(username).orElseThrow(
                () -> new NotFoundException(String.format("No cart for user %s exists", username)));

        var productToDeleteList = cart.getProducts().stream()
                .filter(element ->
                        productsUuids.contains(element.getProductId().toString())
                )
                .toList();
        cart.getProducts().removeAll(productToDeleteList);

        var resultCart = cartRepository.save(cart);
        return CartMapper.mapCart(resultCart);
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeQuantityRequest request) {
        var cart = getOrCreateCart(username);
        AtomicBoolean listContainsProduct = new AtomicBoolean(false);
        var productQuantityList = cart.getProducts().stream()
                .map(element -> {
                    if (element.getProductId().equals(UUID.fromString(request.getProductId()))) {
                        element.setQuantity(request.getNewQuantity());
                        listContainsProduct.set(true);
                    }
                    return element;
                })
                .toList();
        if (!listContainsProduct.get()) {
            throw new NotFoundException(String.format("No productId %s in cart of user %s", request.getProductId(), username));
        }
        // Проверить доступность на складе
        ProductsDimensionsInfo checkResult;
        try {
            checkResult = warehouseFeign.checkCart(CartMapper.mapCart(cart));
        } catch (RuntimeException ex) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException(
                    String.format("Cart of user %s contains low quantity product", username));
        }

        var resultCart = cartRepository.save(cart);
        return CartMapper.mapCart(resultCart);
    }

    private Cart getOrCreateCart(String username) {
        var optionalCart = cartRepository.findByOwner(username);

        if (optionalCart.isEmpty()) {
            var cart = Cart.builder()
                    .owner(username)
                    .products(new ArrayList<>())
                    .isActivate(true)
                    .build();
            // не забываем её реально создать
            cartRepository.save(cart);
            optionalCart = Optional.of(cart);
        }
        return optionalCart.get();
    }
}
