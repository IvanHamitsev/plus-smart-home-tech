package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "action")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    ActionType type;
    @Column(nullable = false, name = "action_value")
    Integer actionValue;
    @ManyToOne(optional = false)
    @JoinColumn(name = "sensor_id")
    Sensor actionSensor;
}
