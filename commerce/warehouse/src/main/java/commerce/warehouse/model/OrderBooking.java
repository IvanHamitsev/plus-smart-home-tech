package commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderBooking {
    @Id
    @Column(name = "id")
    UUID bookingId;
    @ManyToMany
    @JoinTable(
            name = "booking_products",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    List<ProductInWarehouse> products;
    @Column(name = "delivery_id")
    String deliveryId;
}