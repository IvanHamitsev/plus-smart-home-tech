package commerce.interaction.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String str) {
        super(str);
    }
}
