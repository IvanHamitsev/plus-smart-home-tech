package commerce.interaction.rest_api;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.warehouse.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseFeign extends WarehouseRestApi {
    @Override
    @PutMapping
    ProductDto createProduct(@RequestBody NewProductInWarehouseRequest request);

    @Override
    @PostMapping("/check")
    ProductsDimensionsInfo checkCart(@RequestBody ShoppingCartDto cart);

    @Override
    @PostMapping("/add")
    void addQuantity(@RequestBody AddProductToWarehouseRequest request);

    @Override
    @PostMapping("/assembly")
    BookedProductsDto assemblyProductsForOrder(@RequestBody AssemblyProductsForOrderRequest request);

    @Override
    @PostMapping("/return")
    void acceptReturn(@RequestBody Map<String, Integer> products);

    @Override
    @PostMapping("/shipped")
    void shippedToDelivery(ShippedToDeliveryRequest request);

    @Override
    @GetMapping("/address")
    AddressDto getAddress();
}
