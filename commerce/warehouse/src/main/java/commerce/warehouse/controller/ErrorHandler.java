package commerce.warehouse.controller;

import commerce.interaction.exception.NoSpecifiedProductInWarehouseException;
import commerce.interaction.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import commerce.interaction.exception.SpecifiedProductAlreadyInWarehouseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> productAlreadyInWarehouse(SpecifiedProductAlreadyInWarehouseException e) {
        return Map.of("ProductAlreadyInWarehouse: ", e.getMessage());
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> productLowQuantity(ProductInShoppingCartLowQuantityInWarehouseException e) {
        return Map.of("ProductLowQuantity: ", e.getMessage());
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> noSpecifiedProduct(NoSpecifiedProductInWarehouseException e) {
        return Map.of("NoSpecifiedProduct: ", e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> otherException(RuntimeException e) {
        return Map.of("UnknownException: ", e.getMessage());
    }
}
