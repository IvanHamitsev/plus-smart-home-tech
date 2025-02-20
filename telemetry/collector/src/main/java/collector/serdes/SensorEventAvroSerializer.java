package collector.serdes;

import collector.dto.enums.SensorEventType;
import collector.dto.sensor.SensorEvent;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.serialization.Serializer;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SensorEventAvroSerializer implements Serializer<SensorEvent> {

    @Override
    public byte[] serialize(String topic, SensorEvent measure) {
        if (measure == null) {
            return null;
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
            // Debug Code
            if (measure.getType() == SensorEventType.LIGHT_SENSOR_EVENT) {
                LightSensorEvent realEvent = LightSensorEvent.newBuilder()
                        .setId(measure.getId())
                        .setHubId(measure.getHubId())
                        .setTimestamp(measure.getTimestamp())
                        .setLinkQuality(((collector.dto.sensor.LightSensorEvent)measure).getLinkQuality())
                        .setLuminosity(((collector.dto.sensor.LightSensorEvent)measure).getLuminosity())
                        .setType("LIGHT_SENSOR_EVENT")
                        .build();
                DatumWriter<LightSensorEvent> tmpDatum = new SpecificDatumWriter<>(LightSensorEvent.class);
                tmpDatum.write(realEvent, encoder);
            } else {
                var datumWriter = getDatumWriter(measure);
                datumWriter.write(measure, encoder);
            }
            encoder.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Ошибка сериализации экземпляра SensorEvent", e);
        }
    }

    private static SpecificDatumWriter getDatumWriter(SensorEvent measure) {
        switch (measure.getType()) {
            case SWITCH_SENSOR_EVENT -> {
                return new SpecificDatumWriter<>(SwitchSensorEvent.class);
            }
            case LIGHT_SENSOR_EVENT -> {
                return new SpecificDatumWriter<>(LightSensorEvent.class);
            }
            case MOTION_SENSOR_EVENT -> {
                return new SpecificDatumWriter<>(MotionSensorEvent.class);
            }
            case CLIMATE_SENSOR_EVENT -> {
                return new SpecificDatumWriter<>(ClimateSensorEvent.class);
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                return new SpecificDatumWriter<>(TemperatureSensorEvent.class);
            }
            default -> {
                throw new SerializationException("Ошибка определения типа переданного элемента SensorEvent");
            }
        }
    }
}
