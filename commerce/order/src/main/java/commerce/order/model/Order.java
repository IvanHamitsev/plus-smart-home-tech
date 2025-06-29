package commerce.order.model;

import commerce.interaction.dto.order.OrderState;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    @UuidGenerator
    UUID id;
    String username;
    @Column(name = "shopping_cart_id")
    UUID shoppingCartId;
    @Column(name = "payment_id")
    UUID paymentId;
    @Column(name = "delivery_id")
    UUID deliveryId;
    OrderState state;
    @Column(name = "delivery_weight")
    Double deliveryWeight;
    @Column(name = "delivery_volume")
    Double deliveryVolume;
    Boolean fragile;
    @Column(name = "delivery_price")
    Double deliveryPrice;
    @Column(name = "product_price")
    Double productPrice;
    @Column(name = "total_price")
    Double totalPrice;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    List<OrderProduct> products;
}
