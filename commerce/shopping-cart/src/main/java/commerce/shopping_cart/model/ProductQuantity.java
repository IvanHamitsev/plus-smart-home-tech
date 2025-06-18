package commerce.shopping_cart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "product_quantity")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuantity {
    @Id
    @Column(name = "id", nullable = false)
    UUID productId;
    @Column(nullable = false)
    Integer quantity;
}
