package commerce.shopping_cart.service;

import commerce.shopping_cart.kafka.telemetry.event.SensorEventAvro;
import commerce.shopping_cart.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;

public interface AggregationProcess {
    public Optional<SensorsSnapshotAvro> pushSensorEvent(SensorEventAvro measure);
}
