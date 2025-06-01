package commerce.shopping_cart.serialize;

import org.apache.avro.Schema;
import commerce.shopping_cart.kafka.telemetry.event.SensorsSnapshotAvro;

public class SnapshotDeserializer extends BaseAvroDeserializer<SensorsSnapshotAvro> {
    public SnapshotDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }

    public SnapshotDeserializer(Schema schema) {
        super(schema);
    }
}
