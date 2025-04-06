package commerce.shopping_store.model;

import commerce.interaction.dto.product.ProductCategory;
import commerce.interaction.dto.product.ProductState;
import commerce.interaction.dto.product.QuantityState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "shopping_store_product")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @Column(name = "id")
    String productId;
    @Column(name = "product_name", nullable = false)
    String productName;
    @Column(nullable = false)
    String description;
    @Column(name = "image_src")
    String imageSrc;
    @Column(name = "quantity_state", nullable = false)
    QuantityState quantityState;
    @Column(name = "product_state", nullable = false)
    ProductState productState;
    @Column(name = "product_category", nullable = false)
    ProductCategory productCategory;
    @Column(nullable = false)
    Float price;
}
