package commerce.interaction.feign_clients;

import commerce.interaction.dto.product.ProductQuantityStateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "warehouse")
public interface WarehouseFeignClient {
    @GetMapping("/quantityState")
    void setQuantityState(ProductQuantityStateRequest request);
}
