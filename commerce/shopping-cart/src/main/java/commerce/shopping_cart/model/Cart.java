package commerce.shopping_cart.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shopping_cart")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    @UuidGenerator
    UUID cartId;
    @Column(nullable = false)
    String owner;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "cart_products",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    List<ProductQuantity> products;
    Boolean isActivate;
}
