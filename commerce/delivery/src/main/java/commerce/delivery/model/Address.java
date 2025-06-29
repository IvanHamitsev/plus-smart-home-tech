package commerce.delivery.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    @UuidGenerator
    UUID id;
    String country;
    String city;
    String street;
    String house;
    String flat;
}
