package collector.service;

import collector.dto.hub.HubEventDto;
import collector.dto.sensor.SensorEventDto;
import collector.serdes.HubEventAvroSerializer;
import collector.serdes.SensorEventAvroSerializer;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class CollectorService implements AutoCloseable {
    @Value("${collector.kafkaHost:localhost}")
    String kafkaHost;
    @Value("${collector.kafkaPort:9092}")
    String kafkaPort;
    @Value("${collector.sensorTopic}")
    String sensorTopic;
    @Value("${collector.hubTopic}")
    String hubTopic;

    Producer<String, SensorEventDto> sensorProducer;
    Producer<String, HubEventDto> hubProducer;

    // инициализацию нельзя сделать в конструкторе, поскольку не пройдёт инжекция через @Value
    @PostConstruct
    public void initKafka() {
        Properties sensorConfig = new Properties();
        Properties hubConfig = new Properties();

        sensorConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost + ":" + kafkaPort);
        hubConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost + ":" + kafkaPort);

        sensorConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        hubConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);

        sensorConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SensorEventAvroSerializer.class);
        hubConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, HubEventAvroSerializer.class);

        sensorProducer = new KafkaProducer<>(sensorConfig);
        hubProducer = new KafkaProducer<>(hubConfig);
    }

    // отправка в топик telemetry.sensors.v1
    public void sendSensor(SensorEventDto measure) {
        ProducerRecord<String, SensorEventDto> record = new ProducerRecord<>(sensorTopic, measure);
        sensorProducer.send(record);
    }

    // отправка в топик telemetry.hubs.v1
    public void sendHub(HubEventDto action) {
        ProducerRecord<String, HubEventDto> record = new ProducerRecord<>(hubTopic, action);
        hubProducer.send(record);
    }

    @Override
    public void close() throws Exception {
        sensorProducer.close();
        hubProducer.close();
    }
}
