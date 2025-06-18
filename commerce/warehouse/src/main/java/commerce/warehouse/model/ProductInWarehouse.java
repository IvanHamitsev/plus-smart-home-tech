package commerce.warehouse.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "product_in_warehouse")
@Getter
@Setter
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
