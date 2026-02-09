package global.goit.java_final_n_kovalchuk.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for exception classes.
 * Tests FileConversionException and InvalidInputException constructors and behavior.
 */
@DisplayName("Exception Unit Tests")
class ExceptionTests {

    // ==================== FileConversionException Tests ====================

    @Test
    @DisplayName("FileConversionException should store message")
    void testFileConversionExceptionWithMessage() {
        // Arrange & Act
        FileConversionException exception = new FileConversionException("Conversion failed");

        // Assert
        assertEquals("Conversion failed", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("FileConversionException should store message and cause")
    void testFileConversionExceptionWithMessageAndCause() {
        // Arrange
        Throwable cause = new IOException("File not found");

        // Act
        FileConversionException exception = new FileConversionException("Conversion failed", cause);

        // Assert
        assertEquals("Conversion failed", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("FileConversionException should handle null message")
    void testFileConversionExceptionWithNullMessage() {
        // Arrange & Act
        FileConversionException exception = new FileConversionException((String) null);

        // Assert
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("FileConversionException should handle empty message")
    void testFileConversionExceptionWithEmptyMessage() {
        // Arrange & Act
        FileConversionException exception = new FileConversionException("");

        // Assert
        assertEquals("", exception.getMessage());
    }

    @Test
    @DisplayName("FileConversionException should handle null cause")
    void testFileConversionExceptionWithNullCause() {
        // Arrange & Act
        FileConversionException exception = new FileConversionException("Error", null);

        // Assert
        assertEquals("Error", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("FileConversionException should be RuntimeException")
    void testFileConversionExceptionIsRuntimeException() {
        // Arrange & Act
        FileConversionException exception = new FileConversionException("Error");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("FileConversionException should preserve cause type")
    void testFileConversionExceptionPreservesCauseType() {
        // Arrange
        IOException cause = new IOException("IO error");

        // Act
        FileConversionException exception = new FileConversionException("Wrapper", cause);

        // Assert
        assertTrue(exception.getCause() instanceof IOException);
    }

    @Test
    @DisplayName("FileConversionException should handle chained causes")
    void testFileConversionExceptionWithChainedCause() {
        // Arrange
        Throwable rootCause = new NullPointerException("Null pointer");
        Throwable intermediateCause = new IllegalStateException("Illegal state", rootCause);

        // Act
        FileConversionException exception = new FileConversionException("Top level", intermediateCause);

        // Assert
        assertEquals("Top level", exception.getMessage());
        assertEquals(intermediateCause, exception.getCause());
        assertEquals(rootCause, exception.getCause().getCause());
    }

    @Test
    @DisplayName("FileConversionException should be throwable")
    void testFileConversionExceptionIsThrowable() {
        // Arrange & Act
        FileConversionException exception = new FileConversionException("Error");

        // Assert
        assertTrue(exception instanceof Throwable);
    }

    @Test
    @DisplayName("FileConversionException should handle long messages")
    void testFileConversionExceptionWithLongMessage() {
        // Arrange
        String longMessage = "Error: ".repeat(1000);

        // Act
        FileConversionException exception = new FileConversionException(longMessage);

        // Assert
        assertEquals(longMessage, exception.getMessage());
    }

    @Test
    @DisplayName("FileConversionException should handle special characters in message")
    void testFileConversionExceptionWithSpecialCharacters() {
        // Arrange
        String message = "Error: \"quotes\" and, commas; and\n newlines";

        // Act
        FileConversionException exception = new FileConversionException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("FileConversionException should handle unicode in message")
    void testFileConversionExceptionWithUnicode() {
        // Arrange
        String message = "Помилка: Привіт 世界 مرحبا";

        // Act
        FileConversionException exception = new FileConversionException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    // ==================== InvalidInputException Tests ====================

    @Test
    @DisplayName("InvalidInputException should store message")
    void testInvalidInputExceptionWithMessage() {
        // Arrange & Act
        InvalidInputException exception = new InvalidInputException("Invalid input");

        // Assert
        assertEquals("Invalid input", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("InvalidInputException should store message and cause")
    void testInvalidInputExceptionWithMessageAndCause() {
        // Arrange
        Throwable cause = new IllegalArgumentException("Invalid argument");

        // Act
        InvalidInputException exception = new InvalidInputException("Invalid input", cause);

        // Assert
        assertEquals("Invalid input", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("InvalidInputException should handle null message")
    void testInvalidInputExceptionWithNullMessage() {
        // Arrange & Act
        InvalidInputException exception = new InvalidInputException((String) null);

        // Assert
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("InvalidInputException should handle empty message")
    void testInvalidInputExceptionWithEmptyMessage() {
        // Arrange & Act
        InvalidInputException exception = new InvalidInputException("");

        // Assert
        assertEquals("", exception.getMessage());
    }

    @Test
    @DisplayName("InvalidInputException should handle null cause")
    void testInvalidInputExceptionWithNullCause() {
        // Arrange & Act
        InvalidInputException exception = new InvalidInputException("Error", null);

        // Assert
        assertEquals("Error", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("InvalidInputException should be FileConversionException")
    void testInvalidInputExceptionIsFileConversionException() {
        // Arrange & Act
        InvalidInputException exception = new InvalidInputException("Error");

        // Assert
        assertTrue(exception instanceof FileConversionException);
    }

    @Test
    @DisplayName("InvalidInputException should be RuntimeException")
    void testInvalidInputExceptionIsRuntimeException() {
        // Arrange & Act
        InvalidInputException exception = new InvalidInputException("Error");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("InvalidInputException should preserve cause type")
    void testInvalidInputExceptionPreservesCauseType() {
        // Arrange
        IllegalArgumentException cause = new IllegalArgumentException("Invalid arg");

        // Act
        InvalidInputException exception = new InvalidInputException("Wrapper", cause);

        // Assert
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
    }

    @Test
    @DisplayName("InvalidInputException should handle chained causes")
    void testInvalidInputExceptionWithChainedCause() {
        // Arrange
        Throwable rootCause = new NullPointerException("Null pointer");
        Throwable intermediateCause = new IllegalStateException("Illegal state", rootCause);

        // Act
        InvalidInputException exception = new InvalidInputException("Top level", intermediateCause);

        // Assert
        assertEquals("Top level", exception.getMessage());
        assertEquals(intermediateCause, exception.getCause());
        assertEquals(rootCause, exception.getCause().getCause());
    }

    @Test
    @DisplayName("InvalidInputException should be throwable")
    void testInvalidInputExceptionIsThrowable() {
        // Arrange & Act
        InvalidInputException exception = new InvalidInputException("Error");

        // Assert
        assertTrue(exception instanceof Throwable);
    }

    @Test
    @DisplayName("InvalidInputException should handle long messages")
    void testInvalidInputExceptionWithLongMessage() {
        // Arrange
        String longMessage = "Error: ".repeat(1000);

        // Act
        InvalidInputException exception = new InvalidInputException(longMessage);

        // Assert
        assertEquals(longMessage, exception.getMessage());
    }

    @Test
    @DisplayName("InvalidInputException should handle special characters in message")
    void testInvalidInputExceptionWithSpecialCharacters() {
        // Arrange
        String message = "Error: \"quotes\" and, commas; and\n newlines";

        // Act
        InvalidInputException exception = new InvalidInputException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("InvalidInputException should handle unicode in message")
    void testInvalidInputExceptionWithUnicode() {
        // Arrange
        String message = "Помилка: Привіт 世界 مرحبا";

        // Act
        InvalidInputException exception = new InvalidInputException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    // ==================== Exception Hierarchy Tests ====================

    @Test
    @DisplayName("InvalidInputException should be catchable as FileConversionException")
    void testInvalidInputExceptionCaughtAsFileConversionException() {
        // Arrange & Act
        try {
            throw new InvalidInputException("Invalid input");
        } catch (FileConversionException e) {
            // Assert - should catch InvalidInputException as FileConversionException
            assertTrue(e instanceof InvalidInputException);
            assertEquals("Invalid input", e.getMessage());
        }
    }

    @Test
    @DisplayName("InvalidInputException should be catchable as RuntimeException")
    void testInvalidInputExceptionCaughtAsRuntimeException() {
        // Arrange & Act
        try {
            throw new InvalidInputException("Invalid input");
        } catch (RuntimeException e) {
            // Assert - should catch InvalidInputException as RuntimeException
            assertTrue(e instanceof InvalidInputException);
            assertEquals("Invalid input", e.getMessage());
        }
    }

    @Test
    @DisplayName("FileConversionException should be catchable as RuntimeException")
    void testFileConversionExceptionCaughtAsRuntimeException() {
        // Arrange & Act
        try {
            throw new FileConversionException("Conversion error");
        } catch (RuntimeException e) {
            // Assert - should catch FileConversionException as RuntimeException
            assertTrue(e instanceof FileConversionException);
            assertEquals("Conversion error", e.getMessage());
        }
    }

    @Test
    @DisplayName("FileConversionException should be catchable as Exception")
    void testFileConversionExceptionCaughtAsException() {
        // Arrange & Act
        try {
            throw new FileConversionException("Conversion error");
        } catch (Exception e) {
            // Assert - should catch FileConversionException as Exception
            assertTrue(e instanceof FileConversionException);
            assertEquals("Conversion error", e.getMessage());
        }
    }

    @Test
    @DisplayName("Exception stack trace should include cause")
    void testExceptionStackTraceIncludesCause() {
        // Arrange
        Throwable cause = new IOException("IO error");
        FileConversionException exception = new FileConversionException("Wrapper", cause);

        // Act
        StackTraceElement[] stackTrace = exception.getStackTrace();

        // Assert
        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }

    @Test
    @DisplayName("Exception should maintain message after setting cause")
    void testExceptionMessageAfterSettingCause() {
        // Arrange
        String message = "Original message";
        Throwable cause = new IOException("Cause");
        FileConversionException exception = new FileConversionException(message, cause);

        // Act & Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("Multiple exceptions should be distinguishable")
    void testMultipleExceptionsAreDistinguishable() {
        // Arrange & Act
        FileConversionException ex1 = new FileConversionException("Error 1");
        FileConversionException ex2 = new FileConversionException("Error 2");
        InvalidInputException ex3 = new InvalidInputException("Error 3");

        // Assert
        assertNotEquals(ex1, ex2);
        assertNotEquals(ex1, ex3);
        assertNotEquals(ex2, ex3);
        assertEquals("Error 1", ex1.getMessage());
        assertEquals("Error 2", ex2.getMessage());
        assertEquals("Error 3", ex3.getMessage());
    }

    @Test
    @DisplayName("Exception should be serializable")
    void testExceptionIsSerializable() {
        // Arrange
        FileConversionException exception = new FileConversionException("Test");

        // Assert - Exception should be serializable
        assertTrue(exception instanceof java.io.Serializable);
    }

    @Test
    @DisplayName("InvalidInputException should be serializable")
    void testInvalidInputExceptionIsSerializable() {
        // Arrange
        InvalidInputException exception = new InvalidInputException("Test");

        // Assert - Exception should be serializable
        assertTrue(exception instanceof java.io.Serializable);
    }
}
