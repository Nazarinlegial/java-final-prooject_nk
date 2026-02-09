package global.goit.java_final_n_kovalchuk.exception;

/**
 * Базовий клас винятків для операцій конвертації файлів.
 * Цей виняток генерується, коли виникає помилка під час конвертації формату файлу.
 */
public class FileConversionException extends RuntimeException {

    /**
     * Конструює новий FileConversionException із зазначеним повідомленням.
     *
     * @param message повідомлення про помилку
     */
    public FileConversionException(String message) {
        super(message);
    }

    /**
     * Конструює новий FileConversionException із зазначеним повідомленням та причиною.
     *
     * @param message повідомлення про помилку
     * @param cause   причина винятку
     */
    public FileConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
