package commerce.shopping_store.controller;

import commerce.interaction.exception.NotFoundException;
import commerce.interaction.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse notFoundExceptionHandler(final NotFoundException exception) {
        log.warn(exception.getMessage(), exception);
        return new ErrorResponse("No entity", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse badRequest(final ValidationException exception) {
        log.warn(exception.getMessage(), exception);
        return new ErrorResponse("Not valid", exception.getMessage());
    }
}
