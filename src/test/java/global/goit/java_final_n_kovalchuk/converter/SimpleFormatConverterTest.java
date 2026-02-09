package global.goit.java_final_n_kovalchuk.converter;

import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import global.goit.java_final_n_kovalchuk.parser.csv.CsvParser;
import global.goit.java_final_n_kovalchuk.parser.json.JsonParser;
import global.goit.java_final_n_kovalchuk.parser.xml.JacksonXmlParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for SimpleFormatConverter.
 * Tests all 6 conversion combinations between JSON, XML, and CSV formats.
 */
class SimpleFormatConverterTest {

    private final SimpleFormatConverter converter = new SimpleFormatConverter();

    @TempDir
    Path tempDir;

    @AfterEach
    void cleanup() {
        // Clean up any test output files
        // TempDir handles cleanup automatically
    }

    @Test
    void testJsonToXmlConversion() throws FileConversionException {
        // Arrange
        File inputFile = new File("src/test/resources/test_simple.json");
        File outputFile = tempDir.resolve("output.xml").toFile();

        // Act
        converter.convert(inputFile, outputFile);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");

        // Verify output file can be parsed back
        JacksonXmlParser parser = new JacksonXmlParser();
        List<DataRecord> records = parser.parse(outputFile);
        assertFalse(records.isEmpty(), "Output should contain records");
        assertTrue(records.get(0).hasField("name"), "Record should have 'name' field");
    }

    @Test
    void testJsonToCsvConversion() throws FileConversionException {
        // Arrange
        File inputFile = new File("src/test/resources/test_simple.json");
        File outputFile = tempDir.resolve("output.csv").toFile();

        // Act
        converter.convert(inputFile, outputFile);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");

        // Verify output file can be parsed back
        CsvParser parser = new CsvParser();
        List<DataRecord> records = parser.parse(outputFile);
        assertFalse(records.isEmpty(), "Output should contain records");
        assertTrue(records.get(0).hasField("name"), "Record should have 'name' field");
    }

    @Test
    void testXmlToJsonConversion() throws FileConversionException {
        // Arrange
        File inputFile = new File("src/test/resources/test_simple.xml");
        File outputFile = tempDir.resolve("output.json").toFile();

        // Act
        converter.convert(inputFile, outputFile);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");

        // Verify output file can be parsed back
        JsonParser parser = new JsonParser();
        List<DataRecord> records = parser.parse(outputFile);
        assertFalse(records.isEmpty(), "Output should contain records");
        assertTrue(records.get(0).hasField("name"), "Record should have 'name' field");
    }

    @Test
    void testXmlToCsvConversion() throws FileConversionException {
        // Arrange
        File inputFile = new File("src/test/resources/test_simple.xml");
        File outputFile = tempDir.resolve("output.csv").toFile();

        // Act
        converter.convert(inputFile, outputFile);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");

        // Verify output file can be parsed back
        CsvParser parser = new CsvParser();
        List<DataRecord> records = parser.parse(outputFile);
        assertFalse(records.isEmpty(), "Output should contain records");
        assertTrue(records.get(0).hasField("name"), "Record should have 'name' field");
    }

    @Test
    void testCsvToJsonConversion() throws FileConversionException {
        // Arrange
        File inputFile = new File("src/test/resources/test_simple.csv");
        File outputFile = tempDir.resolve("output.json").toFile();

        // Act
        converter.convert(inputFile, outputFile);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");

        // Verify output file can be parsed back
        JsonParser parser = new JsonParser();
        List<DataRecord> records = parser.parse(outputFile);
        assertFalse(records.isEmpty(), "Output should contain records");
        assertTrue(records.get(0).hasField("name"), "Record should have 'name' field");
    }

    @Test
    void testCsvToXmlConversion() throws FileConversionException {
        // Arrange
        File inputFile = new File("src/test/resources/test_simple.csv");
        File outputFile = tempDir.resolve("output.xml").toFile();

        // Act
        converter.convert(inputFile, outputFile);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");

        // Verify output file can be parsed back
        JacksonXmlParser parser = new JacksonXmlParser();
        List<DataRecord> records = parser.parse(outputFile);
        assertFalse(records.isEmpty(), "Output should contain records");
        assertTrue(records.get(0).hasField("name"), "Record should have 'name' field");
    }

    @Test
    void testJsonArrayToXmlConversion() throws FileConversionException {
        // Arrange
        File inputFile = new File("src/test/resources/test_array.json");
        File outputFile = tempDir.resolve("output_array.xml").toFile();

        // Act
        converter.convert(inputFile, outputFile);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");

        // Verify output file can be parsed back
        JacksonXmlParser parser = new JacksonXmlParser();
        List<DataRecord> records = parser.parse(outputFile);
        assertFalse(records.isEmpty(), "Output should contain records");
    }

    @Test
    void testConversionWithNonExistentInputFile() {
        // Arrange
        File inputFile = new File("src/test/resources/nonexistent.json");
        File outputFile = tempDir.resolve("output.json").toFile();

        // Act & Assert
        assertThrows(FileConversionException.class, () -> converter.convert(inputFile, outputFile));
    }

    @Test
    void testConversionWithInvalidOutputPath() throws IOException {
        // Arrange
        File inputFile = new File("src/test/resources/test_simple.json");
        // Create a directory with the same name as the output file
        Path dirPath = tempDir.resolve("output_dir");
        Files.createDirectories(dirPath);
        File outputFile = dirPath.toFile();

        // Act & Assert
        // This should fail because we're trying to write to a directory
        assertThrows(FileConversionException.class, () -> converter.convert(inputFile, outputFile));
    }
}
