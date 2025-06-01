package commerce.shopping_cart.serialize;

import org.apache.avro.Schema;
import commerce.shopping_cart.kafka.telemetry.event.HubEventAvro;

public class HubEventDeserializer extends BaseAvroDeserializer<HubEventAvro> {
    public HubEventDeserializer() {
        super(HubEventAvro.getClassSchema());
    }

    public HubEventDeserializer(Schema schema) {
        super(schema);
    }
}
