package commerce.shopping_cart.service;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.warehouse.ProductsDimensionsInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "shopping-cart")
public interface LocalShoppingCartFeignClient {
    @PostMapping("/check")
    ProductsDimensionsInfo checkCart(@RequestBody ShoppingCartDto shoppingCartDto);
}
