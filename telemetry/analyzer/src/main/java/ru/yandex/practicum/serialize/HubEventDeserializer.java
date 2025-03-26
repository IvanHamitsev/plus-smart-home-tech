package ru.yandex.practicum.serialize;

import org.apache.avro.Schema;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public class HubEventDeserializer extends BaseAvroDeserializer<HubEventAvro> {
    public HubEventDeserializer() {
        super(HubEventAvro.getClassSchema());
    }

    public HubEventDeserializer(Schema schema) {
        super(schema);
    }
}
