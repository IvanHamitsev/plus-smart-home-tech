package commerce.warehouse.model;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "product_in_warehouse")
@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductInWarehouse {
    @Id
    @Column(nullable = false)
    String id;
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
