package commerce.shopping_store.serialize;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;

public class BaseAvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {
    // ....
    private final DatumReader<T> reader;
    private final DecoderFactory decoder;

    public BaseAvroDeserializer(Schema schema) {
        this(DecoderFactory.get(), schema);
    }

    public BaseAvroDeserializer(DecoderFactory decoderFactory, Schema schema) {
        // ...
        reader = new SpecificDatumReader<>(schema);
        decoder = decoderFactory;
    }

    // ...

    @Override
    public T deserialize(String topic, byte[] data) {
        // Код десериализации двоичных данных
        try {
            if (null != data) {
                BinaryDecoder decode = decoder.binaryDecoder(data, null);
                return reader.read(null, decode);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Can't deserialize data from topic " + topic);
        }
    }
}
