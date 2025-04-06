package collector.serialize;

import collector.mapper.InputDtoMapper;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.serialization.Serializer;
import commerce.shopping_store.grpc.telemetry.event.HubEventProto;
import commerce.shopping_store.grpc.telemetry.event.SensorEventProto;
import commerce.shopping_store.kafka.telemetry.event.HubEventAvro;
import commerce.shopping_store.kafka.telemetry.event.SensorEventAvro;

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
            SensorEventAvro sensorEventAvro;
            switch (inpMessage.getPayloadCase()) {
                case LIGHT_SENSOR_EVENT -> {
                    sensorEventAvro = InputDtoMapper.mapLightSensorEvent(inpMessage);
                }
                case SWITCH_SENSOR_EVENT -> {
                    sensorEventAvro = InputDtoMapper.mapSwitchSensorEvent(inpMessage);
                }
                case MOTION_SENSOR_EVENT -> {
                    sensorEventAvro = InputDtoMapper.mapMotionSensorEvent(inpMessage);
                }
                case TEMPERATURE_SENSOR_EVENT -> {
                    sensorEventAvro = InputDtoMapper.mapTemperatureSensorEvent(inpMessage);
                }
                case CLIMATE_SENSOR_EVENT -> {
                    sensorEventAvro = InputDtoMapper.mapClimateSensorEvent(inpMessage);
                }
                default -> {
                    throw new SerializationException("Ошибка определения типа сенсора, переданного от Hub Router элемента SensorEventProto");
                }
            }
            DatumWriter<SensorEventAvro> datumWriter = new SpecificDatumWriter<>(SensorEventAvro.class);
            datumWriter.write(sensorEventAvro, encoder);
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
            HubEventAvro hubEventAvro;
            switch (inpMessage.getPayloadCase()) {
                case DEVICE_ADDED -> {
                    hubEventAvro = InputDtoMapper.mapDeviceAddedEvent(inpMessage);
                }
                case DEVICE_REMOVED -> {
                    hubEventAvro = InputDtoMapper.mapDeviceRemovedEvent(inpMessage);
                }
                case SCENARIO_ADDED -> {
                    hubEventAvro = InputDtoMapper.mapScenarioAddedEvent(inpMessage);
                }
                case SCENARIO_REMOVED -> {
                    hubEventAvro = InputDtoMapper.mapScenarioRemovedEvent(inpMessage);
                }
                default -> {
                    throw new SerializationException("Ошибка определения типа сенсора, переданного от Hub Router элемента HubEventProto");
                }
            }
            DatumWriter<HubEventAvro> datumWriter = new SpecificDatumWriter<>(HubEventAvro.class);
            datumWriter.write(hubEventAvro, encoder);
            encoder.flush();
            return outputStream.toByteArray();
        } catch (
                IOException e) {
            throw new SerializationException("Ошибка сериализации экземпляра HubEventProto", e);
        }
    }
}
