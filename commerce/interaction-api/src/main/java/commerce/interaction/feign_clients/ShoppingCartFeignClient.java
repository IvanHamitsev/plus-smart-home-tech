package commerce.interaction.feign_clients;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.warehouse.ProductsDimensionsInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "shopping-cart")
public interface ShoppingCartFeignClient {

    @GetMapping("/check")
    ProductsDimensionsInfo checkCart(ShoppingCartDto shoppingCartDto);
}
