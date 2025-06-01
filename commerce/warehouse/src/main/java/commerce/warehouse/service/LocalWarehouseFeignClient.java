package commerce.warehouse.service;

import commerce.interaction.dto.product.ProductQuantityStateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "warehouse")
public interface LocalWarehouseFeignClient {
    @GetMapping("/quantityState")
    void setQuantityState(ProductQuantityStateRequest request);
}
