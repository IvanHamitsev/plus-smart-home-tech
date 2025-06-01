package commerce.interaction.feign_clients;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.warehouse.ProductsDimensionsInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Why FeignClient don't create Component?
// What url specify?
@FeignClient(name = "shopping-cart")
public interface ShoppingCartFeignClient {

    @PostMapping("/check")
    ProductsDimensionsInfo checkCart(@RequestBody ShoppingCartDto shoppingCartDto);
}
