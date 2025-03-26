package ru.yandex.practicum.serialize;

import org.apache.avro.Schema;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class SnapshotDeserializer extends BaseAvroDeserializer<SensorsSnapshotAvro> {
    public SnapshotDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }

    public SnapshotDeserializer(Schema schema) {
        super(schema);
    }
}
