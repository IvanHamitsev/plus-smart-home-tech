package collector.serdes;

import collector.dto.hub.HubEventDto;
import collector.mapper.InpDtoMapper;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.serialization.Serializer;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HubEventAvroSerializer implements Serializer<HubEventDto> {
    @Override
    public byte[] serialize(String topic, HubEventDto hubEvent) {
        if (hubEvent == null) {
            return null;
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);

            switch (hubEvent.getType()) {
                case DEVICE_ADDED -> {
                    DeviceAddedEvent realEvent = InpDtoMapper.mapDeviceAddedEvent(hubEvent);
                    DatumWriter<DeviceAddedEvent> datumWriter = new SpecificDatumWriter<>(DeviceAddedEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case DEVICE_REMOVED -> {
                    DeviceRemovedEvent realEvent = InpDtoMapper.mapDeviceRemovedEvent(hubEvent);
                    DatumWriter<DeviceRemovedEvent> datumWriter = new SpecificDatumWriter<>(DeviceRemovedEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case SCENARIO_ADDED -> {
                    ScenarioAddedEvent realEvent = InpDtoMapper.mapScenarioAddedEvent(hubEvent);
                    DatumWriter<ScenarioAddedEvent> datumWriter = new SpecificDatumWriter<>(ScenarioAddedEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case SCENARIO_REMOVED -> {
                    ScenarioRemovedEvent realEvent = InpDtoMapper.mapScenarioRemovedEvent(hubEvent);
                    DatumWriter<ScenarioRemovedEvent> datumWriter = new SpecificDatumWriter<>(ScenarioRemovedEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                default -> {
                    throw new SerializationException("Ошибка определения типа переданного элемента HubEventDto");
                }
            }

            encoder.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Ошибка сериализации экземпляра SensorEventDto", e);
        }
    }
}
