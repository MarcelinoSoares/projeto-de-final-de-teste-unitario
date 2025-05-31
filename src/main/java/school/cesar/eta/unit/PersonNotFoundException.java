package school.cesar.eta.unit;

/**
 * Exception thrown when a person is not found.
 */
public class PersonNotFoundException extends RuntimeException {

    /**
     * Constructs a new exception with the specified message.
     *
     * @param message
     *            the detail message
     */
    public PersonNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified message and cause.
     *
     * @param message
     *            the detail message
     * @param cause
     *            the cause
     */
    public PersonNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}