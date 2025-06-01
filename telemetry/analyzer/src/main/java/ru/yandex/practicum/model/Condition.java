package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "condition")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    ConditionType type;
    @Column(nullable = false)
    ConditionOperationType operation;
    @Column(nullable = false, name = "condition_value")
    Integer conditionValue;
    @ManyToOne(optional = false)
    @JoinColumn(name = "sensor_id")
    Sensor conditionSensor;
}
