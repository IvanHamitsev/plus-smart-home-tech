package commerce.shopping_store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import commerce.shopping_store.kafka.telemetry.event.HubEventAvro;
import commerce.shopping_store.serialize.HubEventDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventAnalyzerStarter implements Runnable {

    private Consumer<String, HubEventAvro> hubEventConsumer;
    @Autowired
    private HubEventProcess processor;

    @Value("${analyzer.kafkaHost:localhost}")
    String kafkaHost;
    @Value("${analyzer.kafkaPort:9092}")
    String kafkaPort;
    @Value("${analyzer.hubGroupId:hubEvents}")
    String groupIdConfig;
    @Value("${analyzer.hubTopic}")
    String hubTopic;

    @Override
    public void run() {
        try {
            Properties consumerConfig = new Properties();
            consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost + ":" + kafkaPort);
            consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdConfig);
            consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HubEventDeserializer.class);
            hubEventConsumer = new KafkaConsumer<>(consumerConfig);
            hubEventConsumer.subscribe(List.of(hubTopic));
            ConsumerRecords<String, HubEventAvro> records = null;

            while (true) {
                try {
                    records = hubEventConsumer.poll(Duration.ofMillis(2000));
                    for (var record : records) {
                        log.info("Получено сообщение HubEvent со смещением {}:\n{}\n",
                                record.offset(), record.value());
                        processor.pushHubEvent(record.value());
                    }
                    if (!records.isEmpty()) {
                        hubEventConsumer.commitAsync();
                    }
                } catch (RecordDeserializationException e) {
                    log.error("Пропускаем неясную запись от хаба {}", records);
                    hubEventConsumer.commitSync();
                }
            }
        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от хаба", e);
        } finally {
            try {
                hubEventConsumer.commitSync();
            } finally {
                log.info("Закрываем hubEventConsumer");
                hubEventConsumer.close();
            }
        }
    }
}
