package commerce.shopping_store.service;

import commerce.shopping_store.kafka.telemetry.event.SensorEventAvro;
import commerce.shopping_store.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;

public interface AggregationProcess {
    public Optional<SensorsSnapshotAvro> pushSensorEvent(SensorEventAvro measure);
}
