package commerce.shopping_store.repository;

import commerce.interaction.dto.product.ProductCategory;
import commerce.shopping_store.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShoppingStoreRepository extends JpaRepository<Product, UUID> {
    List<Product> findByProductCategory(ProductCategory category, Pageable page); // ProductCategory or String argument ?

    List<Product> findByProductCategoryOrderByProductName(ProductCategory category, Pageable page);

    List<Product> findAll();
}
