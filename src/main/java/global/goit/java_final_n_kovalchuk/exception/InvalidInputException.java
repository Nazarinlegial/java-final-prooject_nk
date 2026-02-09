package global.goit.java_final_n_kovalchuk.exception;

/**
 * Exception thrown when invalid input is provided to the file converter.
 * This includes errors in CLI arguments, file validation failures, and format detection issues.
 */
public class InvalidInputException extends FileConversionException {

    /**
     * Constructs a new InvalidInputException with the specified detail message.
     *
     * @param message the detail message explaining the invalid input
     */
    public InvalidInputException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidInputException with the specified detail message and cause.
     *
     * @param message the detail message explaining the invalid input
     * @param cause   the cause of the exception
     */
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
