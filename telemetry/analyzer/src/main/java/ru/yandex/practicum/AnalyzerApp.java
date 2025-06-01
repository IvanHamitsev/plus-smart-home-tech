package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.service.HubEventAnalyzerStarter;
import ru.yandex.practicum.service.SnapshotAnalyzerStarter;

@SpringBootApplication
public class AnalyzerApp {
    public static void main(String[] args) {
        var context = SpringApplication.run(AnalyzerApp.class, args);

        // поток обработки сообщений от хабов
        HubEventAnalyzerStarter hubAnalyzer = context.getBean(HubEventAnalyzerStarter.class);
        Thread hubAnalyzerThread = new Thread(hubAnalyzer);
        hubAnalyzerThread.start();

        // поток получения и анализа снапшотов
        SnapshotAnalyzerStarter snapshotAnalyzer = context.getBean(SnapshotAnalyzerStarter.class);
        snapshotAnalyzer.start();
    }
}
