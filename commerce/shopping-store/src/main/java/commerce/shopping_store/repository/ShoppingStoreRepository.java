package commerce.shopping_store.repository;

import commerce.interaction.dto.product.ProductCategory;
import commerce.shopping_store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingStoreRepository extends JpaRepository<Product, String> {
    List<Product> findByCategory(ProductCategory category);
}
