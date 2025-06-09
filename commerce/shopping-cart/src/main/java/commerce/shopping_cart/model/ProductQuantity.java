package commerce.shopping_cart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "product_quantity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuantity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    @UuidGenerator
    UUID productId;
    @Column(nullable = false)
    Integer quantity;
}
