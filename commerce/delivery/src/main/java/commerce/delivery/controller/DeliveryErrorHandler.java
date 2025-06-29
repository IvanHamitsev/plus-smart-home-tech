package commerce.delivery.controller;

import commerce.interaction.exception.NoDeliveryFoundException;
import commerce.interaction.exception.NoOrderFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class DeliveryErrorHandler {
    @ExceptionHandler(NoOrderFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> noOrder(NoOrderFoundException e) {
        return Map.of("No such order! ", e.getMessage());
    }

    @ExceptionHandler(NoDeliveryFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> noDelivery(NoDeliveryFoundException e) {
        return Map.of("No such delivery! ", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> unknownException(Exception e) {
        return Map.of("Unknown exception! ", e.getMessage());
    }
}
