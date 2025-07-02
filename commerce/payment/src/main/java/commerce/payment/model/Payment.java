package commerce.payment.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@Table(name = "payment")
public class Payment {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    @UuidGenerator
    UUID id;
    @Column(name = "order_id")
    UUID orderId;
    @Column(name = "product_cost")
    Double productCost;
    @Column(name = "delivery_cost")
    Double deliveryCost;
    @Column(name = "total_cost")
    Double totalCost;
    PaymentStatus status;
}
