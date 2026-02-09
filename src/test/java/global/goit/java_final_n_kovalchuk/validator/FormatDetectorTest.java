package global.goit.java_final_n_kovalchuk.validator;

import global.goit.java_final_n_kovalchuk.exception.InvalidInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FormatDetector.
 * Tests file format detection based on file extensions.
 */
class FormatDetectorTest {

    @Test
    void testDetectJsonFormat() throws InvalidInputException {
        // Arrange
        String filePath = "data.json";

        // Act
        FormatDetector.FileFormat format = FormatDetector.detectFormat(filePath);

        // Assert
        assertEquals(FormatDetector.FileFormat.JSON, format);
    }

    @Test
    void testDetectJsonFormatWithUppercase() throws InvalidInputException {
        // Arrange
        String filePath = "data.JSON";

        // Act
        FormatDetector.FileFormat format = FormatDetector.detectFormat(filePath);

        // Assert
        assertEquals(FormatDetector.FileFormat.JSON, format);
    }

    @Test
    void testDetectJsonFormatWithMixedCase() throws InvalidInputException {
        // Arrange
        String filePath = "data.JsOn";

        // Act
        FormatDetector.FileFormat format = FormatDetector.detectFormat(filePath);

        // Assert
        assertEquals(FormatDetector.FileFormat.JSON, format);
    }

    @Test
    void testDetectXmlFormat() throws InvalidInputException {
        // Arrange
        String filePath = "data.xml";

        // Act
        FormatDetector.FileFormat format = FormatDetector.detectFormat(filePath);

        // Assert
        assertEquals(FormatDetector.FileFormat.XML, format);
    }

    @Test
    void testDetectXmlFormatWithUppercase() throws InvalidInputException {
        // Arrange
        String filePath = "data.XML";

        // Act
        FormatDetector.FileFormat format = FormatDetector.detectFormat(filePath);

        // Assert
        assertEquals(FormatDetector.FileFormat.XML, format);
    }

    @Test
    void testDetectCsvFormat() throws InvalidInputException {
        // Arrange
        String filePath = "data.csv";

        // Act
        FormatDetector.FileFormat format = FormatDetector.detectFormat(filePath);

        // Assert
        assertEquals(FormatDetector.FileFormat.CSV, format);
    }

    @Test
    void testDetectCsvFormatWithUppercase() throws InvalidInputException {
        // Arrange
        String filePath = "data.CSV";

        // Act
        FormatDetector.FileFormat format = FormatDetector.detectFormat(filePath);

        // Assert
        assertEquals(FormatDetector.FileFormat.CSV, format);
    }

    @Test
    void testDetectWithNullPath() {
        // Arrange
        String filePath = null;

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FormatDetector.detectFormat(filePath));
        assertTrue(exception.getMessage().contains("File path cannot be null or empty"));
    }

    @Test
    void testDetectWithEmptyPath() {
        // Arrange
        String filePath = "";

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FormatDetector.detectFormat(filePath));
        assertTrue(exception.getMessage().contains("File path cannot be null or empty"));
    }

    @Test
    void testDetectWithWhitespacePath() {
        // Arrange
        String filePath = "   ";

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FormatDetector.detectFormat(filePath));
        assertTrue(exception.getMessage().contains("File path cannot be null or empty"));
    }

    @Test
    void testDetectWithNoExtension() {
        // Arrange
        String filePath = "datafile";

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FormatDetector.detectFormat(filePath));
        assertTrue(exception.getMessage().contains("has no extension"));
    }

    @Test
    void testDetectWithTrailingDot() {
        // Arrange
        String filePath = "datafile.";

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FormatDetector.detectFormat(filePath));
        assertTrue(exception.getMessage().contains("has no extension"));
    }

    @Test
    void testDetectWithUnsupportedFormat() {
        // Arrange
        String filePath = "data.txt";

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FormatDetector.detectFormat(filePath));
        assertTrue(exception.getMessage().contains("Unsupported file format"));
        assertTrue(exception.getMessage().contains(".txt"));
    }

    @Test
    void testDetectWithUnsupportedFormatPdf() {
        // Arrange
        String filePath = "data.pdf";

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FormatDetector.detectFormat(filePath));
        assertTrue(exception.getMessage().contains("Unsupported file format"));
        assertTrue(exception.getMessage().contains(".pdf"));
    }

    @Test
    void testDetectWithPathContainingMultipleDots() throws InvalidInputException {
        // Arrange
        String filePath = "my.data.file.json";

        // Act
        FormatDetector.FileFormat format = FormatDetector.detectFormat(filePath);

        // Assert - should detect based on last extension
        assertEquals(FormatDetector.FileFormat.JSON, format);
    }

    @Test
    void testDetectWithAbsolutePath() throws InvalidInputException {
        // Arrange
        String filePath = "/path/to/data.json";

        // Act
        FormatDetector.FileFormat format = FormatDetector.detectFormat(filePath);

        // Assert
        assertEquals(FormatDetector.FileFormat.JSON, format);
    }

    @Test
    void testDetectWithRelativePath() throws InvalidInputException {
        // Arrange
        String filePath = "../data.xml";

        // Act
        FormatDetector.FileFormat format = FormatDetector.detectFormat(filePath);

        // Assert
        assertEquals(FormatDetector.FileFormat.XML, format);
    }

    @Test
    void testDetectWithSpecialCharactersInPath() throws InvalidInputException {
        // Arrange
        String filePath = "data-file_v2.0.csv";

        // Act
        FormatDetector.FileFormat format = FormatDetector.detectFormat(filePath);

        // Assert
        assertEquals(FormatDetector.FileFormat.CSV, format);
    }

    @Test
    void testDetectWithWhitespaceInPath() throws InvalidInputException {
        // Arrange
        String filePath = "  data.json  ";

        // Act
        FormatDetector.FileFormat format = FormatDetector.detectFormat(filePath);

        // Assert - should trim whitespace
        assertEquals(FormatDetector.FileFormat.JSON, format);
    }
}
