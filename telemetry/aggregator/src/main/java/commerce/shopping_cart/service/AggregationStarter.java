package commerce.shopping_cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import commerce.shopping_cart.kafka.telemetry.event.SensorEventAvro;
import commerce.shopping_cart.kafka.telemetry.event.SensorsSnapshotAvro;
import commerce.shopping_cart.serialize.SensorEventDeserializer;
import commerce.shopping_cart.serialize.SensorsSnapshotSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    // ... объявление полей и конструктора ...
    private Consumer<String, SensorEventAvro> consumer;
    private Producer<String, SensorsSnapshotAvro> producer;
    @Autowired
    private AggregationProcess processor;

    @Value("${aggregator.kafkaHost:localhost}")
    String kafkaHost;
    @Value("${aggregator.kafkaPort:9092}")
    String kafkaPort;
    @Value("${aggregator.groupId:snapshot}")
    String groupIdConfig;
    @Value("${aggregator.sensorTopic}")
    String sensorTopic;
    @Value("${aggregator.snapshotTopic}")
    String snapshotTopic;

    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        try {
            // ... подготовка к обработке данных ...
            // ... например, подписка на топик ...

            // Consumer
            Properties consumerConfig = new Properties();
            consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost + ":" + kafkaPort);
            consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdConfig);
            consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class);
            consumer = new KafkaConsumer<>(consumerConfig);
            consumer.subscribe(List.of(sensorTopic));

            // Producer
            Properties producerConfig = new Properties();
            producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost + ":" + kafkaPort);
            producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SensorsSnapshotSerializer.class);
            producer = new KafkaProducer<>(producerConfig);
            ConsumerRecords<String, SensorEventAvro> records = null;
            // Цикл обработки событий
            while (true) {
                // ... реализация цикла опроса ...
                try {
                    records = consumer.poll(Duration.ofMillis(100));
                    for (var sensorRecord : records) {
                        log.info("Получено сообщение sensorEvent со смещением {}:\n{}\n",
                                sensorRecord.offset(), sensorRecord.value());
                        // ... и обработка полученных данных ...
                        Optional<SensorsSnapshotAvro> snapshot = processor.pushSensorEvent(sensorRecord.value());
                        // если снапшот обновился, заслать его в топик снапшотов
                        if (snapshot.isPresent()) {
                            ProducerRecord<String, SensorsSnapshotAvro> snapshotRecord = new ProducerRecord<>(snapshotTopic, snapshot.get());
                            log.info("Send ProducerRecord({}, hubId={})", snapshotTopic, snapshot.get().getHubId());
                            producer.send(snapshotRecord);
                            producer.flush();
                        }
                    }
                    consumer.commitAsync();
                } catch (RecordDeserializationException e) {
                    log.error("Пропускаем неясную запись событий от датчиков {}", records);
                    consumer.commitSync();
                }

            }
        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {
                // Перед тем, как закрыть продюсер и консьюмер, нужно убедится,
                // что все сообщения, лежащие в буффере, отправлены и
                // все оффсеты обработанных сообщений зафиксированы

                // здесь нужно вызвать метод продюсера для сброса данных в буффере
                producer.flush();
                // здесь нужно вызвать метод консьюмера для фиксиции смещений
                consumer.commitSync();

            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }
}
