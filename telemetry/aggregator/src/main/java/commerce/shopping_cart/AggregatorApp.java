package commerce.shopping_cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import commerce.shopping_cart.service.AggregationStarter;

@SpringBootApplication
public class AggregatorApp {
    public static void main(String[] args) {
        var context = SpringApplication.run(AggregatorApp.class, args);
        AggregationStarter aggregator = context.getBean(AggregationStarter.class);
        // запуск цикла обработки входных сообщений датчиков
        aggregator.start();
    }
}
