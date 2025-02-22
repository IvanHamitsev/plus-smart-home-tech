package collector.serdes;

import collector.dto.InputEventDto;
import collector.mapper.InputDtoMapper;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.serialization.Serializer;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InputEventAvroSerializer implements Serializer<InputEventDto> {
    @Override
    public byte[] serialize(String topic, InputEventDto inputEvent) {
        if (inputEvent == null) {
            return null;
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);

            switch (inputEvent.getType()) {
                case LIGHT_SENSOR_EVENT -> {
                    LightSensorEvent realEvent = InputDtoMapper.mapLightSensorEvent(inputEvent);
                    DatumWriter<LightSensorEvent> datumWriter = new SpecificDatumWriter<>(LightSensorEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case SWITCH_SENSOR_EVENT -> {
                    SwitchSensorEvent realEvent = InputDtoMapper.mapSwitchSensorEvent(inputEvent);
                    DatumWriter<SwitchSensorEvent> datumWriter = new SpecificDatumWriter<>(SwitchSensorEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case MOTION_SENSOR_EVENT -> {
                    MotionSensorEvent realEvent = InputDtoMapper.mapMotionSensorEvent(inputEvent);
                    DatumWriter<MotionSensorEvent> datumWriter = new SpecificDatumWriter<>(MotionSensorEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case TEMPERATURE_SENSOR_EVENT -> {
                    TemperatureSensorEvent realEvent = InputDtoMapper.mapTemperatureSensorEvent(inputEvent);
                    DatumWriter<TemperatureSensorEvent> datumWriter = new SpecificDatumWriter<>(TemperatureSensorEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case CLIMATE_SENSOR_EVENT -> {
                    ClimateSensorEvent realEvent = InputDtoMapper.mapClimateSensorEvent(inputEvent);
                    DatumWriter<ClimateSensorEvent> datumWriter = new SpecificDatumWriter<>(ClimateSensorEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case DEVICE_ADDED -> {
                    DeviceAddedEvent realEvent = InputDtoMapper.mapDeviceAddedEvent(inputEvent);
                    DatumWriter<DeviceAddedEvent> datumWriter = new SpecificDatumWriter<>(DeviceAddedEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case DEVICE_REMOVED -> {
                    DeviceRemovedEvent realEvent = InputDtoMapper.mapDeviceRemovedEvent(inputEvent);
                    DatumWriter<DeviceRemovedEvent> datumWriter = new SpecificDatumWriter<>(DeviceRemovedEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case SCENARIO_ADDED -> {
                    ScenarioAddedEvent realEvent = InputDtoMapper.mapScenarioAddedEvent(inputEvent);
                    DatumWriter<ScenarioAddedEvent> datumWriter = new SpecificDatumWriter<>(ScenarioAddedEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                case SCENARIO_REMOVED -> {
                    ScenarioRemovedEvent realEvent = InputDtoMapper.mapScenarioRemovedEvent(inputEvent);
                    DatumWriter<ScenarioRemovedEvent> datumWriter = new SpecificDatumWriter<>(ScenarioRemovedEvent.class);
                    datumWriter.write(realEvent, encoder);
                }
                default -> {
                    throw new SerializationException("Ошибка определения типа переданного от Hub Router элемента");
                }
            }

            encoder.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Ошибка сериализации экземпляра InputEventDto", e);
        }
    }
}
