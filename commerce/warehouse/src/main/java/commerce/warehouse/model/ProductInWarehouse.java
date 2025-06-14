package commerce.warehouse.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "product_in_warehouse")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductInWarehouse {
    @Id
    @Column(name = "id")
    UUID productId;
    Boolean fragile;
    @Column(nullable = false)
    Double width;
    @Column(nullable = false)
    Double height;
    @Column(nullable = false)
    Double depth;
    @Column(nullable = false)
    Double weight;
    @Column(nullable = false)
    Integer quantity;
}
