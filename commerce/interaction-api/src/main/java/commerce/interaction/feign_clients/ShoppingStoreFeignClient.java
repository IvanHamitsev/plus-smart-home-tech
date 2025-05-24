package commerce.interaction.feign_clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreFeignClient {

}
