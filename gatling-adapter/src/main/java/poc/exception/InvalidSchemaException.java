package poc.exception;

public class InvalidSchemaException extends RuntimeException {

    public InvalidSchemaException(String message) {
        super("Invalid schema" + message);
    }
}
