package commerce.shopping_cart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "product_quantity")
@Data
@Builder
public class ProductQuantity {
    @Id
    @Column(name = "id", nullable = false)
    String productId;
    @Column(nullable = false)
    Integer quantity;
}
