package collector.service;

import collector.dto.InputEventDto;
import collector.serdes.InputEventAvroSerializer;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    Producer<String, InputEventDto> kafkaProducer;

    // инициализацию нельзя сделать в конструкторе, поскольку не пройдёт инжекция через @Value
    @PostConstruct
    public void initKafka() {
        Properties kafkaConfig = new Properties();

        kafkaConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost + ":" + kafkaPort);
        kafkaConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kafkaConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, InputEventAvroSerializer.class);

        kafkaProducer = new KafkaProducer<>(kafkaConfig);
    }

    // отправка в топик telemetry.sensors.v1
    public void sendSensor(InputEventDto measure) {
        Long timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        ProducerRecord<String, InputEventDto> record = new ProducerRecord<>(sensorTopic, null, timestamp, measure.getHubId(), measure);
        kafkaProducer.send(record);
    }

    // отправка в топик telemetry.hubs.v1
    public void sendHub(InputEventDto action) {
        Long timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        ProducerRecord<String, InputEventDto> record = new ProducerRecord<>(hubTopic, null, timestamp, action.getHubId(), action);
        kafkaProducer.send(record);
    }

    @Override
    public void close() throws Exception {
        kafkaProducer.close();
    }
}
