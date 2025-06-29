package commerce.payment.controller;

import commerce.interaction.exception.NoOrderFoundException;
import commerce.interaction.exception.NotEnoughInfoInOrderToCalculateException;
import commerce.interaction.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class PaymentErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> productNotFound(final NotFoundException e) {
        return Map.of("No product found: ", e.getMessage());
    }

    @ExceptionHandler(NoOrderFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> orderNotFound(NoOrderFoundException e) {
        return Map.of("No such order: ", e.getMessage());
    }

    @ExceptionHandler(NotEnoughInfoInOrderToCalculateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> notEnoughInformation(NotEnoughInfoInOrderToCalculateException e) {
        return Map.of("Not enough information in order: ", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> unknownException(Exception e) {
        return Map.of("Unknown error: ", e.getMessage());
    }
}
