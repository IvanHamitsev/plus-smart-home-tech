package commerce.interaction.rest_api;

import commerce.interaction.dto.cart.ShoppingCartDto;
import commerce.interaction.dto.product.ProductDto;
import commerce.interaction.dto.warehouse.AddProductToWarehouseRequest;
import commerce.interaction.dto.warehouse.AddressDto;
import commerce.interaction.dto.warehouse.NewProductInWarehouseRequest;
import commerce.interaction.dto.warehouse.ProductsDimensionsInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    @GetMapping("/address")
    AddressDto getAddress();
}
