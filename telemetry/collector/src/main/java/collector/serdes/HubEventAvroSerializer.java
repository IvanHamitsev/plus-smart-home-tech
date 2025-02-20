package collector.serdes;

import collector.dto.hub.HubEvent;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.serialization.Serializer;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HubEventAvroSerializer implements Serializer<HubEvent> {
    @Override
    public byte[] serialize(String topic, HubEvent event) {
        if (event == null) {
            return null;
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            var datumWriter = getDatumWriter(event);
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
            datumWriter.write(event, encoder);
            encoder.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Ошибка сериализации экземпляра SensorEvent", e);
        }
    }

    private static SpecificDatumWriter getDatumWriter(HubEvent event) {
        switch (event.getType()) {
            case DEVICE_ADDED -> {
                return new SpecificDatumWriter<>(DeviceAddedEvent.class);
            }
            case DEVICE_REMOVED -> {
                return new SpecificDatumWriter<>(DeviceRemovedEvent.class);
            }
            case SCENARIO_ADDED -> {
                return new SpecificDatumWriter<>(ScenarioAddedEvent.class);
            }
            case SCENARIO_REMOVED -> {
                return new SpecificDatumWriter<>(ScenarioRemovedEvent.class);
            }
            default -> {
                throw new SerializationException("Ошибка определения типа переданного элемента HubEvent");
            }
        }
    }
}
