package global.goit.java_final_n_kovalchuk;

import global.goit.java_final_n_kovalchuk.cli.CommandLineArgs;
import global.goit.java_final_n_kovalchuk.cli.CommandLineParser;
import global.goit.java_final_n_kovalchuk.converter.SimpleFormatConverter;
import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import global.goit.java_final_n_kovalchuk.parser.csv.CsvParser;
import global.goit.java_final_n_kovalchuk.parser.json.JsonParser;
import global.goit.java_final_n_kovalchuk.parser.xml.JacksonXmlParser;
import global.goit.java_final_n_kovalchuk.validator.FileValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Main class.
 * Tests end-to-end application workflow including CLI argument parsing,
 * file validation, format conversion, and error handling.
 */
@DisplayName("Main Integration Tests")
class MainTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Main should successfully convert JSON to XML")
    void testMainJsonToXmlConversion() throws Exception {
        // Arrange
        File inputFile = createTestJsonFile(tempDir, "input.json");
        File outputFile = tempDir.resolve("output.xml").toFile();
        String[] args = {"--input", inputFile.getAbsolutePath(), "--output", outputFile.getAbsolutePath()};

        // Act
        Main.main(args);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
    }

    @Test
    @DisplayName("Main should successfully convert JSON to CSV")
    void testMainJsonToCsvConversion() throws Exception {
        // Arrange
        File inputFile = createTestJsonFile(tempDir, "input.json");
        File outputFile = tempDir.resolve("output.csv").toFile();
        String[] args = {"--input", inputFile.getAbsolutePath(), "--output", outputFile.getAbsolutePath()};

        // Act
        Main.main(args);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
    }

    @Test
    @DisplayName("Main should successfully convert XML to JSON")
    void testMainXmlToJsonConversion() throws Exception {
        // Arrange
        File inputFile = createTestXmlFile(tempDir, "input.xml");
        File outputFile = tempDir.resolve("output.json").toFile();
        String[] args = {"--input", inputFile.getAbsolutePath(), "--output", outputFile.getAbsolutePath()};

        // Act
        Main.main(args);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
    }

    @Test
    @DisplayName("Main should successfully convert XML to CSV")
    void testMainXmlToCsvConversion() throws Exception {
        // Arrange
        File inputFile = createTestXmlFile(tempDir, "input.xml");
        File outputFile = tempDir.resolve("output.csv").toFile();
        String[] args = {"--input", inputFile.getAbsolutePath(), "--output", outputFile.getAbsolutePath()};

        // Act
        Main.main(args);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
    }

    @Test
    @DisplayName("Main should successfully convert CSV to JSON")
    void testMainCsvToJsonConversion() throws Exception {
        // Arrange
        File inputFile = createTestCsvFile(tempDir, "input.csv");
        File outputFile = tempDir.resolve("output.json").toFile();
        String[] args = {"--input", inputFile.getAbsolutePath(), "--output", outputFile.getAbsolutePath()};

        // Act
        Main.main(args);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
    }

    @Test
    @DisplayName("Main should successfully convert CSV to XML")
    void testMainCsvToXmlConversion() throws Exception {
        // Arrange
        File inputFile = createTestCsvFile(tempDir, "input.csv");
        File outputFile = tempDir.resolve("output.xml").toFile();
        String[] args = {"--input", inputFile.getAbsolutePath(), "--output", outputFile.getAbsolutePath()};

        // Act
        Main.main(args);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
    }

    @Test
    @DisplayName("Main should preserve data integrity during conversion")
    void testMainPreservesDataIntegrity() throws Exception {
        // Arrange
        File inputFile = createTestJsonFile(tempDir, "input.json");
        File outputFile = tempDir.resolve("output.xml").toFile();
        String[] args = {"--input", inputFile.getAbsolutePath(), "--output", outputFile.getAbsolutePath()};

        // Act
        Main.main(args);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        
        // Verify data integrity
        JsonParser jsonParser = new JsonParser();
        List<DataRecord> originalRecords = jsonParser.parse(inputFile);
        
        JacksonXmlParser xmlParser = new JacksonXmlParser();
        List<DataRecord> convertedRecords = xmlParser.parse(outputFile);
        
        assertEquals(originalRecords.size(), convertedRecords.size(), "Record count should match");
        assertEquals(originalRecords.get(0).getField("name"), convertedRecords.get(0).getField("name"), "Name field should match");
    }

    @Test
    @DisplayName("Main should handle conversion to same format")
    void testMainConversionToSameFormat() throws Exception {
        // Arrange
        File inputFile = createTestJsonFile(tempDir, "input.json");
        File outputFile = tempDir.resolve("output.json").toFile();
        String[] args = {"--input", inputFile.getAbsolutePath(), "--output", outputFile.getAbsolutePath()};

        // Act
        Main.main(args);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
    }

    @Test
    @DisplayName("Main should handle files with unicode content")
    void testMainWithUnicodeContent() throws Exception {
        // Arrange
        File inputFile = createUnicodeJsonFile(tempDir, "unicode.json");
        File outputFile = tempDir.resolve("output.xml").toFile();
        String[] args = {"--input", inputFile.getAbsolutePath(), "--output", outputFile.getAbsolutePath()};

        // Act
        Main.main(args);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
    }

    @Test
    @DisplayName("Main should handle files with special characters in content")
    void testMainWithSpecialCharactersInContent() throws Exception {
        // Arrange
        File inputFile = createSpecialCharJsonFile(tempDir, "special.json");
        File outputFile = tempDir.resolve("output.xml").toFile();
        String[] args = {"--input", inputFile.getAbsolutePath(), "--output", outputFile.getAbsolutePath()};

        // Act
        Main.main(args);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
    }

    @Test
    @DisplayName("Main should handle large files")
    void testMainWithLargeFile() throws Exception {
        // Arrange
        File inputFile = createLargeJsonFile(tempDir, "large.json");
        File outputFile = tempDir.resolve("output.xml").toFile();
        String[] args = {"--input", inputFile.getAbsolutePath(), "--output", outputFile.getAbsolutePath()};

        // Act
        Main.main(args);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
    }

    @Test
    @DisplayName("Main should handle relative paths")
    void testMainWithRelativePaths() throws Exception {
        // Arrange
        File inputFile = createTestJsonFile(tempDir, "input.json");
        File outputFile = tempDir.resolve("output.xml").toFile();
        String[] args = {"--input", inputFile.getPath(), "--output", outputFile.getPath()};

        // Act
        Main.main(args);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
    }

    @Test
    @DisplayName("Main should handle absolute paths")
    void testMainWithAbsolutePaths() throws Exception {
        // Arrange
        File inputFile = createTestJsonFile(tempDir, "input.json");
        File outputFile = tempDir.resolve("output.xml").toFile();
        String[] args = {"--input", inputFile.getAbsolutePath(), "--output", outputFile.getAbsolutePath()};

        // Act
        Main.main(args);

        // Assert
        assertTrue(outputFile.exists(), "Output file should be created");
    }

    // ==================== Helper Methods ====================

    private File createTestJsonFile(Path dir, String filename) throws IOException {
        File file = dir.resolve(filename).toFile();
        String jsonContent = """
            {
              "name": "John Doe",
              "age": 30,
              "email": "john@example.com"
            }
            """;
        Files.writeString(file.toPath(), jsonContent);
        return file;
    }

    private File createTestXmlFile(Path dir, String filename) throws IOException {
        File file = dir.resolve(filename).toFile();
        String xmlContent = """
            <root>
                <name>John Doe</name>
                <age>30</age>
                <email>john@example.com</email>
            </root>
            """;
        Files.writeString(file.toPath(), xmlContent);
        return file;
    }

    private File createTestCsvFile(Path dir, String filename) throws IOException {
        File file = dir.resolve(filename).toFile();
        String csvContent = """
            name,age,email
            John Doe,30,john@example.com
            """;
        Files.writeString(file.toPath(), csvContent);
        return file;
    }

    private File createLargeJsonFile(Path dir, String filename) throws IOException {
        File file = dir.resolve(filename).toFile();
        StringBuilder jsonContent = new StringBuilder("[\n");
        for (int i = 0; i < 100; i++) {
            jsonContent.append("  {\"name\":\"Person").append(i).append("\",\"age\":").append(20 + i).append("}");
            if (i < 99) jsonContent.append(",");
            jsonContent.append("\n");
        }
        jsonContent.append("]");
        Files.writeString(file.toPath(), jsonContent.toString());
        return file;
    }

    private File createUnicodeJsonFile(Path dir, String filename) throws IOException {
        File file = dir.resolve(filename).toFile();
        String jsonContent = """
            {
              "name": "Привіт 世界 مرحبا",
              "city": "Київ"
            }
            """;
        Files.writeString(file.toPath(), jsonContent);
        return file;
    }

    private File createSpecialCharJsonFile(Path dir, String filename) throws IOException {
        File file = dir.resolve(filename).toFile();
        String jsonContent = """
            {
              "name": "John O'Connor",
              "description": "A person with \\"quotes\\" and, commas",
              "notes": "Line1\\nLine2\\nLine3"
            }
            """;
        Files.writeString(file.toPath(), jsonContent);
        return file;
    }
}
