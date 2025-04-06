package collector.service;

import collector.serialize.InputGrpcSerializer;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import commerce.shopping_store.grpc.telemetry.event.HubEventProto;
import commerce.shopping_store.grpc.telemetry.event.SensorEventProto;

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

    Producer<String, Object> kafkaProducer;

    // инициализацию нельзя сделать в конструкторе, поскольку не пройдёт инжекция через @Value
    @PostConstruct
    public void initKafka() {
        Properties kafkaConfig = new Properties();

        kafkaConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost + ":" + kafkaPort);
        kafkaConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kafkaConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, InputGrpcSerializer.class);

        kafkaProducer = new KafkaProducer<>(kafkaConfig);
    }

    // отправка в топик telemetry.sensors.v1
    public void sendSensor(SensorEventProto measure) {
        Long timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        ProducerRecord<String, Object> record = new ProducerRecord<>(sensorTopic, null, timestamp, measure.getHubId(), measure);
        kafkaProducer.send(record);
        kafkaProducer.flush();
    }

    // отправка в топик telemetry.hubs.v1
    public void sendHub(HubEventProto action) {
        Long timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        ProducerRecord<String, Object> record = new ProducerRecord<>(hubTopic, null, timestamp, action.getHubId(), action);
        kafkaProducer.send(record);
        kafkaProducer.flush();
    }

    @Override
    public void close() throws Exception {
        kafkaProducer.close();
    }
}
