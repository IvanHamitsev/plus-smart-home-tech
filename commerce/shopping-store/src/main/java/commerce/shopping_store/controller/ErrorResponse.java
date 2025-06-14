package commerce.shopping_store.controller;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
    private final String description;
}
