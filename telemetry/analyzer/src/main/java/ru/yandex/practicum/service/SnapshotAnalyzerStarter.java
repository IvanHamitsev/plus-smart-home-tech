package ru.yandex.practicum.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.serialize.SnapshotDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotAnalyzerStarter {

    private Consumer<String, SensorsSnapshotAvro> snapshotConsumer;

    @GrpcClient("hub-router")
    private HubRouterControllerBlockingStub grpcClient;

    @Autowired
    private SnapshotProcess processor;

    @Value("${analyzer.kafkaHost:localhost}")
    String kafkaHost;
    @Value("${analyzer.kafkaPort:9092}")
    String kafkaPort;
    @Value("${analyzer.snapshotGroupId:snapshotEvents}")
    String groupIdConfig;
    @Value("${analyzer.snapshotTopic}")
    String snapshotTopic;

    public void start() {
        try {
            Properties consumerConfig = new Properties();
            consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost + ":" + kafkaPort);
            consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdConfig);
            consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class);
            consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SnapshotDeserializer.class);
            snapshotConsumer = new KafkaConsumer<>(consumerConfig);
            snapshotConsumer.subscribe(List.of(snapshotTopic));
            ConsumerRecords<String, SensorsSnapshotAvro> records = null;

            ManagedChannel handMadeChannel = ManagedChannelBuilder
                    .forTarget("localhost:59090")
                    .usePlaintext().build();

            grpcClient = HubRouterControllerGrpc.newBlockingStub(handMadeChannel);

            while (true) {
                try {
                    records = snapshotConsumer.poll(Duration.ofMillis(100));
                    for (var record : records) {
                        log.info("Получено сообщение Snapshot со смещением {}:\n{}\n",
                                record.offset(), record.value());
                        var request = processor.pushSnapshot(record.value());
                        if (null != request) {
                            grpcClient.sendDeviceActionMessage(request);
                        }
                    }
                    snapshotConsumer.commitAsync();
                } catch (RecordDeserializationException e) {
                    log.error("Пропускаем неясную запись от хаба {}", records);
                    snapshotConsumer.commitSync();
                }
            }
        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки снапшота", e);
        } finally {
            try {
                snapshotConsumer.commitSync();
            } finally {
                log.info("Закрываем snapshotConsumer");
                snapshotConsumer.close();
            }
        }
    }
}
