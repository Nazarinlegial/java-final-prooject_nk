package global.goit.java_final_n_kovalchuk.validator;

import global.goit.java_final_n_kovalchuk.exception.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FileValidator.
 * Tests file validation including existence, readability, and format detection.
 */
class FileValidatorTest {

    @TempDir
    Path tempDir;

    @Test
    void testValidateExistingJsonFile() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test.json").toFile();
        String jsonContent = "{\"name\":\"John\",\"age\":30}";
        Files.writeString(testFile.toPath(), jsonContent);

        // Act
        FormatDetector.FileFormat format = FileValidator.validateFile(testFile.getAbsolutePath());

        // Assert
        assertEquals(FormatDetector.FileFormat.JSON, format);
    }

    @Test
    void testValidateExistingXmlFile() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test.xml").toFile();
        String xmlContent = "<root><name>John</name></root>";
        Files.writeString(testFile.toPath(), xmlContent);

        // Act
        FormatDetector.FileFormat format = FileValidator.validateFile(testFile.getAbsolutePath());

        // Assert
        assertEquals(FormatDetector.FileFormat.XML, format);
    }

    @Test
    void testValidateExistingCsvFile() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test.csv").toFile();
        String csvContent = "name,age\nJohn,30";
        Files.writeString(testFile.toPath(), csvContent);

        // Act
        FormatDetector.FileFormat format = FileValidator.validateFile(testFile.getAbsolutePath());

        // Assert
        assertEquals(FormatDetector.FileFormat.CSV, format);
    }

    @Test
    void testValidateWithNullPath() {
        // Arrange
        String filePath = null;

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FileValidator.validateFile(filePath));
        assertTrue(exception.getMessage().contains("File path cannot be null or empty"));
    }

    @Test
    void testValidateWithEmptyPath() {
        // Arrange
        String filePath = "";

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FileValidator.validateFile(filePath));
        assertTrue(exception.getMessage().contains("File path cannot be null or empty"));
    }

    @Test
    void testValidateWithWhitespacePath() {
        // Arrange
        String filePath = "   ";

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FileValidator.validateFile(filePath));
        assertTrue(exception.getMessage().contains("File path cannot be null or empty"));
    }

    @Test
    void testValidateNonExistentFile() {
        // Arrange
        String filePath = "non_existent_file.json";

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FileValidator.validateFile(filePath));
        assertTrue(exception.getMessage().contains("File does not exist"));
    }

    @Test
    void testValidateDirectoryInsteadOfFile() throws Exception {
        // Arrange
        Path dirPath = tempDir.resolve("testdir");
        Files.createDirectories(dirPath);

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FileValidator.validateFile(dirPath.toString()));
        assertTrue(exception.getMessage().contains("Path is not a file"));
    }

    @Test
    void testValidateEmptyFile() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("empty.json").toFile();
        Files.writeString(testFile.toPath(), "");

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FileValidator.validateFile(testFile.getAbsolutePath()));
        assertTrue(exception.getMessage().contains("File is empty"));
    }

    @Test
    void testValidateFileWithUnsupportedExtension() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test.txt").toFile();
        Files.writeString(testFile.toPath(), "some content");

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, 
            () -> FileValidator.validateFile(testFile.getAbsolutePath()));
        assertTrue(exception.getMessage().contains("Unsupported file format"));
    }

    @Test
    void testValidateFileWithWhitespaceInPath() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test.json").toFile();
        String jsonContent = "{\"name\":\"John\"}";
        Files.writeString(testFile.toPath(), jsonContent);
        String pathWithWhitespace = "  " + testFile.getAbsolutePath() + "  ";

        // Act
        FormatDetector.FileFormat format = FileValidator.validateFile(pathWithWhitespace);

        // Assert - should trim whitespace and validate successfully
        assertEquals(FormatDetector.FileFormat.JSON, format);
    }

    @Test
    void testValidateFileWithAbsolutePath() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test.json").toFile();
        String jsonContent = "{\"name\":\"John\"}";
        Files.writeString(testFile.toPath(), jsonContent);

        // Act
        FormatDetector.FileFormat format = FileValidator.validateFile(testFile.getAbsolutePath());

        // Assert
        assertEquals(FormatDetector.FileFormat.JSON, format);
    }

    @Test
    void testValidateFileWithRelativePath() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test.json").toFile();
        String jsonContent = "{\"name\":\"John\"}";
        Files.writeString(testFile.toPath(), jsonContent);

        // Act
        FormatDetector.FileFormat format = FileValidator.validateFile(testFile.getPath());

        // Assert
        assertEquals(FormatDetector.FileFormat.JSON, format);
    }

    @Test
    void testValidateFileWithSpecialCharactersInName() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test-file_v2.0.json").toFile();
        String jsonContent = "{\"name\":\"John\"}";
        Files.writeString(testFile.toPath(), jsonContent);

        // Act
        FormatDetector.FileFormat format = FileValidator.validateFile(testFile.getAbsolutePath());

        // Assert
        assertEquals(FormatDetector.FileFormat.JSON, format);
    }

    @Test
    void testValidateFileWithMultipleDotsInName() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("my.data.file.json").toFile();
        String jsonContent = "{\"name\":\"John\"}";
        Files.writeString(testFile.toPath(), jsonContent);

        // Act
        FormatDetector.FileFormat format = FileValidator.validateFile(testFile.getAbsolutePath());

        // Assert
        assertEquals(FormatDetector.FileFormat.JSON, format);
    }

    @Test
    void testValidateFileWithContent() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test.json").toFile();
        String jsonContent = """
            {
              "name": "John Doe",
              "age": 30,
              "email": "john@example.com"
            }
            """;
        Files.writeString(testFile.toPath(), jsonContent);

        // Act
        FormatDetector.FileFormat format = FileValidator.validateFile(testFile.getAbsolutePath());

        // Assert
        assertEquals(FormatDetector.FileFormat.JSON, format);
    }
}
