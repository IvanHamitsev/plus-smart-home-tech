package collector.serialize;

import collector.mapper.InputDtoMapper;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.serialization.Serializer;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InputGrpcSerializer implements Serializer<Object> {

    @Override
    public byte[] serialize(String topic, Object inp) {
        if (inp == null) {
            return null;
        }

        if (inp instanceof SensorEventProto) {
            return serializeSensor(topic, (SensorEventProto) inp);
        }

        if (inp instanceof HubEventProto) {
            return serializeHub(topic, (HubEventProto) inp);
        }

        throw new SerializationException("Ошибка определения типа переданного от Hub Router объекта");
    }


    public byte[] serializeSensor(String topic, SensorEventProto inpMessage) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
            SensorEventAvro realEvent;
            switch (inpMessage.getPayloadCase()) {
                case LIGHT_SENSOR_EVENT -> {
                    realEvent = InputDtoMapper.mapLightSensorEvent(inpMessage);
                }
                case SWITCH_SENSOR_EVENT -> {
                    realEvent = InputDtoMapper.mapSwitchSensorEvent(inpMessage);
                }
                case MOTION_SENSOR_EVENT -> {
                    realEvent = InputDtoMapper.mapMotionSensorEvent(inpMessage);
                }
                case TEMPERATURE_SENSOR_EVENT -> {
                    realEvent = InputDtoMapper.mapTemperatureSensorEvent(inpMessage);
                }
                case CLIMATE_SENSOR_EVENT -> {
                    realEvent = InputDtoMapper.mapClimateSensorEvent(inpMessage);
                }
                default -> {
                    throw new SerializationException("Ошибка определения типа сенсора, переданного от Hub Router элемента SensorEventProto");
                }
            }
            DatumWriter<SensorEventAvro> datumWriter = new SpecificDatumWriter<>(SensorEventAvro.class);
            datumWriter.write(realEvent, encoder);
            encoder.flush();
            return outputStream.toByteArray();
        } catch (
                IOException e) {
            throw new SerializationException("Ошибка сериализации экземпляра SensorEventProto", e);
        }
    }

    public byte[] serializeHub(String topic, HubEventProto inpMessage) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
            switch (inpMessage.getPayloadCase()) {
                case DEVICE_ADDED -> {
                    DeviceAddedEvent realEvent = InputDtoMapper.mapDeviceAddedEvent(inpMessage);
                    DatumWriter<DeviceAddedEvent> datumWriter = new SpecificDatumWriter<>(DeviceAddedEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case DEVICE_REMOVED -> {
                    DeviceRemovedEvent realEvent = InputDtoMapper.mapDeviceRemovedEvent(inpMessage);
                    DatumWriter<DeviceRemovedEvent> datumWriter = new SpecificDatumWriter<>(DeviceRemovedEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case SCENARIO_ADDED -> {
                    ScenarioAddedEvent realEvent = InputDtoMapper.mapScenarioAddedEvent(inpMessage);
                    DatumWriter<ScenarioAddedEvent> datumWriter = new SpecificDatumWriter<>(ScenarioAddedEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case SCENARIO_REMOVED -> {
                    ScenarioRemovedEvent realEvent = InputDtoMapper.mapScenarioRemovedEvent(inpMessage);
                    DatumWriter<ScenarioRemovedEvent> datumWriter = new SpecificDatumWriter<>(ScenarioRemovedEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                default -> {
                    throw new SerializationException("Ошибка определения типа сенсора, переданного от Hub Router элемента HubEventProto");
                }
            }
            encoder.flush();
            return outputStream.toByteArray();
        } catch (
                IOException e) {
            throw new SerializationException("Ошибка сериализации экземпляра HubEventProto", e);
        }
    }
}
