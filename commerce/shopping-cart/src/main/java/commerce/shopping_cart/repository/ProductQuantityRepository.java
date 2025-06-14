package commerce.shopping_cart.repository;

import commerce.shopping_cart.model.ProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductQuantityRepository extends JpaRepository<ProductQuantity, UUID> {
}
