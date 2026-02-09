package global.goit.java_final_n_kovalchuk.parser.csv;

import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CsvParser.
 */
class CsvParserTest {

    @TempDir
    Path tempDir;

    @Test
    void testParseCsvWithHeaders() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test.csv").toFile();
        CsvParser parser = new CsvParser();

        // Create test CSV file
        String csvContent = """
            name,age,email
            John Doe,30,john@example.com
            Jane Smith,25,jane@example.com
            """;
        Files.writeString(testFile.toPath(), csvContent);

        // Act
        List<DataRecord> records = parser.parse(testFile);

        // Assert
        assertNotNull(records);
        assertEquals(2, records.size());

        DataRecord firstRecord = records.get(0);
        assertEquals("John Doe", firstRecord.getField("name"));
        assertEquals("30", firstRecord.getField("age"));
        assertEquals("john@example.com", firstRecord.getField("email"));

        DataRecord secondRecord = records.get(1);
        assertEquals("Jane Smith", secondRecord.getField("name"));
        assertEquals("25", secondRecord.getField("age"));
        assertEquals("jane@example.com", secondRecord.getField("email"));
    }

    @Test
    void testParseCsvWithQuotedValues() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test_quoted.csv").toFile();
        CsvParser parser = new CsvParser();

        // Create test CSV file with quoted values
        String csvContent = """
            name,description
            "John Doe","A person with, a comma"
            "Jane Smith","A person with \"quotes\""
            """;
        Files.writeString(testFile.toPath(), csvContent);

        // Act
        List<DataRecord> records = parser.parse(testFile);

        // Assert
        assertNotNull(records);
        assertEquals(2, records.size());

        DataRecord firstRecord = records.get(0);
        assertEquals("John Doe", firstRecord.getField("name"));
        assertEquals("A person with, a comma", firstRecord.getField("description"));

        DataRecord secondRecord = records.get(1);
        assertEquals("Jane Smith", secondRecord.getField("name"));
        assertEquals("A person with \"quotes\"", secondRecord.getField("description"));
    }

    @Test
    void testParseCsvWithEmptyValues() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test_empty.csv").toFile();
        CsvParser parser = new CsvParser();

        // Create test CSV file with empty values
        String csvContent = """
            name,age,email
            John Doe,30,
            ,25,jane@example.com
            """;
        Files.writeString(testFile.toPath(), csvContent);

        // Act
        List<DataRecord> records = parser.parse(testFile);

        // Assert
        assertNotNull(records);
        assertEquals(2, records.size());

        DataRecord firstRecord = records.get(0);
        assertEquals("John Doe", firstRecord.getField("name"));
        assertEquals("30", firstRecord.getField("age"));
        assertNull(firstRecord.getField("email"));

        DataRecord secondRecord = records.get(1);
        assertNull(secondRecord.getField("name"));
        assertEquals("25", secondRecord.getField("age"));
        assertEquals("jane@example.com", secondRecord.getField("email"));
    }

    @Test
    void testParseCsvWithSpecialCharacters() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test_special.csv").toFile();
        CsvParser parser = new CsvParser();

        // Create test CSV file with special characters
        String csvContent = """
            name,email
            "John O'Connor","john.o'connor@example.com"
            "Jane;Smith","jane.smith@example.com"
            """;
        Files.writeString(testFile.toPath(), csvContent);

        // Act
        List<DataRecord> records = parser.parse(testFile);

        // Assert
        assertNotNull(records);
        assertEquals(2, records.size());

        DataRecord firstRecord = records.get(0);
        assertEquals("John O'Connor", firstRecord.getField("name"));
        assertEquals("john.o'connor@example.com", firstRecord.getField("email"));

        DataRecord secondRecord = records.get(1);
        assertEquals("Jane;Smith", secondRecord.getField("name"));
        assertEquals("jane.smith@example.com", secondRecord.getField("email"));
    }

    @Test
    void testParseMalformedCsv() {
        // Arrange
        File testFile = tempDir.resolve("test_malformed.csv").toFile();
        CsvParser parser = new CsvParser();

        // Create malformed CSV file
        String csvContent = """
            name,age,email
            John Doe,30,john@example.com
            Jane Smith,25
            """; // Missing email field
        try {
            Files.writeString(testFile.toPath(), csvContent);
        } catch (Exception e) {
            fail("Failed to create test file: " + e.getMessage());
        }

        // Act & Assert - should still parse, just with empty value for missing field
        assertDoesNotThrow(() -> {
            List<DataRecord> records = parser.parse(testFile);
            assertNotNull(records);
            assertEquals(2, records.size());
        });
    }

    @Test
    void testParseNullFile() {
        // Arrange
        CsvParser parser = new CsvParser();

        // Act & Assert
        assertThrows(FileConversionException.class, () -> parser.parse(null));
    }

    @Test
    void testParseNonExistentFile() {
        // Arrange
        File nonExistentFile = new File("non_existent_file.csv");
        CsvParser parser = new CsvParser();

        // Act & Assert
        assertThrows(FileConversionException.class, () -> parser.parse(nonExistentFile));
    }

    @Test
    void testParseEmptyCsvFile() {
        // Arrange
        File testFile = tempDir.resolve("test_empty_file.csv").toFile();
        CsvParser parser = new CsvParser();

        // Create empty CSV file
        try {
            Files.writeString(testFile.toPath(), "");
        } catch (Exception e) {
            fail("Failed to create test file: " + e.getMessage());
        }

        // Act & Assert
        assertThrows(FileConversionException.class, () -> parser.parse(testFile));
    }

    @Test
    void testParseCsvWithOnlyHeaders() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test_headers_only.csv").toFile();
        CsvParser parser = new CsvParser();

        // Create CSV file with only headers
        String csvContent = "name,age,email\n";
        Files.writeString(testFile.toPath(), csvContent);

        // Act
        List<DataRecord> records = parser.parse(testFile);

        // Assert
        assertNotNull(records);
        assertEquals(0, records.size());
    }

    @Test
    void testParseCsvWithDifferentColumnCount() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test_diff_columns.csv").toFile();
        CsvParser parser = new CsvParser();

        // Create CSV file with different column counts
        String csvContent = """
            name,age,email
            John Doe,30,john@example.com
            Jane,25
            Bob,40,bob@example.com,extra
            """;
        Files.writeString(testFile.toPath(), csvContent);

        // Act
        List<DataRecord> records = parser.parse(testFile);

        // Assert
        assertNotNull(records);
        assertEquals(3, records.size());

        DataRecord firstRecord = records.get(0);
        assertEquals("John Doe", firstRecord.getField("name"));
        assertEquals("30", firstRecord.getField("age"));
        assertEquals("john@example.com", firstRecord.getField("email"));

        DataRecord secondRecord = records.get(1);
        assertEquals("Jane", secondRecord.getField("name"));
        assertEquals("25", secondRecord.getField("age"));
        assertNull(secondRecord.getField("email"));

        DataRecord thirdRecord = records.get(2);
        assertEquals("Bob", thirdRecord.getField("name"));
        assertEquals("40", thirdRecord.getField("age"));
        assertEquals("bob@example.com", thirdRecord.getField("email"));
    }
}
