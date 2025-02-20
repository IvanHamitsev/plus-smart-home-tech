package collector.serdes;

import collector.dto.InpDtoMapper;
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

            switch (measure.getType()) {
                case LIGHT_SENSOR_EVENT -> {
                    LightSensorEvent realEvent = InpDtoMapper.mapLightSensorEvent(measure);
                    DatumWriter<LightSensorEvent> datumWriter = new SpecificDatumWriter<>(LightSensorEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case SWITCH_SENSOR_EVENT -> {
                    SwitchSensorEvent realEvent = InpDtoMapper.mapSwitchSensorEvent(measure);
                    DatumWriter<SwitchSensorEvent> datumWriter = new SpecificDatumWriter<>(SwitchSensorEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case MOTION_SENSOR_EVENT -> {
                    MotionSensorEvent realEvent = InpDtoMapper.mapMotionSensorEvent(measure);
                    DatumWriter<MotionSensorEvent> datumWriter = new SpecificDatumWriter<>(MotionSensorEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case TEMPERATURE_SENSOR_EVENT -> {
                    TemperatureSensorEvent realEvent = InpDtoMapper.mapTemperatureSensorEvent(measure);
                    DatumWriter<TemperatureSensorEvent> datumWriter = new SpecificDatumWriter<>(TemperatureSensorEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case CLIMATE_SENSOR_EVENT -> {
                    ClimateSensorEvent realEvent = InpDtoMapper.mapClimateSensorEvent(measure);
                    DatumWriter<ClimateSensorEvent> datumWriter = new SpecificDatumWriter<>(ClimateSensorEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                default -> {
                    throw new SerializationException("Ошибка определения типа переданного элемента SensorEvent");
                }
            }
            encoder.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Ошибка сериализации экземпляра SensorEvent", e);
        }
    }
}
