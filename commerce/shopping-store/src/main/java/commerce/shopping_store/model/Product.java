package commerce.shopping_store.model;

import commerce.interaction.dto.product.ProductCategory;
import commerce.interaction.dto.product.ProductState;
import commerce.interaction.dto.product.QuantityState;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "shopping_store_product")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue
    @UuidGenerator
    UUID productId;
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
    @Enumerated(EnumType.STRING)
    ProductCategory productCategory;
    @Column(nullable = false)
    BigDecimal price;
}
