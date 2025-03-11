package ru.yandex.practicum.serialize;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SensorsSnapshotSerializer implements Serializer<SensorsSnapshotAvro> {
    @Override
    public byte[] serialize(String s, SensorsSnapshotAvro snapshot) {
        if (snapshot == null) {
            return null;
        }

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(stream, null);
            DatumWriter<SensorsSnapshotAvro> writer = new SpecificDatumWriter<>(SensorsSnapshotAvro.class);

            writer.write(snapshot, encoder);
            encoder.flush();

            return stream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Can't serialize snapshot: " + e.getMessage());
        }
    }
}
