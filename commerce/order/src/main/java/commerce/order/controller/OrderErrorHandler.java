package commerce.order.controller;

import commerce.interaction.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class OrderErrorHandler {
    @ExceptionHandler(NotAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> unauthorized(NotAuthorizedUserException e) {
        log.warn(e.getMessage());
        return Map.of("User not authorized ", e.getMessage());
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> noProduct(NoSpecifiedProductInWarehouseException e) {
        log.warn(e.getMessage());
        return Map.of("No product in warehouse ", e.getMessage());
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> lowQuantity(ProductInShoppingCartLowQuantityInWarehouseException e) {
        log.warn(e.getMessage());
        return Map.of("Low product quantity in warehouse ", e.getMessage());
    }

    @ExceptionHandler(NoOrderFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> noOrder(NoOrderFoundException e) {
        log.warn(e.getMessage());
        return Map.of("No such order ", e.getMessage());
    }

    @ExceptionHandler(NoDeliveryFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> noDelivery(NoDeliveryFoundException e) {
        log.warn(e.getMessage());
        return Map.of("No such delivery ", e.getMessage());
    }

    @ExceptionHandler(NotEnoughInfoInOrderToCalculateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> notEnoughInfo(NotEnoughInfoInOrderToCalculateException e) {
        log.warn(e.getMessage());
        return Map.of("Not enough information to calculate ", e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> productNotFound(final NotFoundException e) {
        log.warn(e.getMessage());
        return Map.of("Product not found ", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> unknownException(Exception e) {
        log.warn(e.getMessage());
        return Map.of("Unknown state ", e.getMessage());
    }
}
