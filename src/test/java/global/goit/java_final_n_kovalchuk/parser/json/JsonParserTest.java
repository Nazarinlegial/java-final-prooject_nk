package global.goit.java_final_n_kovalchuk.parser.json;

import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JsonParser.
 */
class JsonParserTest {

    @TempDir
    Path tempDir;

    @Test
    void testParseSimpleJsonObject() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test_simple.json").toFile();
        JsonParser parser = new JsonParser();

        // Create test JSON file
        String jsonContent = """
            {
              "name": "John Doe",
              "age": 30,
              "email": "john@example.com"
            }
            """;
        java.nio.file.Files.writeString(testFile.toPath(), jsonContent);

        // Act
        List<DataRecord> records = parser.parse(testFile);

        // Assert
        assertNotNull(records);
        assertEquals(1, records.size());

        DataRecord record = records.get(0);
        assertEquals("John Doe", record.getField("name"));
        assertEquals(30, record.getField("age"));
        assertEquals("john@example.com", record.getField("email"));
    }

    @Test
    void testParseJsonArray() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test_array.json").toFile();
        JsonParser parser = new JsonParser();

        // Create test JSON file
        String jsonContent = """
            [
              {
                "name": "John Doe",
                "age": 30,
                "email": "john@example.com"
              },
              {
                "name": "Jane Smith",
                "age": 25,
                "email": "jane@example.com"
              }
            ]
            """;
        java.nio.file.Files.writeString(testFile.toPath(), jsonContent);

        // Act
        List<DataRecord> records = parser.parse(testFile);

        // Assert
        assertNotNull(records);
        assertEquals(2, records.size());

        DataRecord firstRecord = records.get(0);
        assertEquals("John Doe", firstRecord.getField("name"));
        assertEquals(30, firstRecord.getField("age"));

        DataRecord secondRecord = records.get(1);
        assertEquals("Jane Smith", secondRecord.getField("name"));
        assertEquals(25, secondRecord.getField("age"));
    }

    @Test
    void testParseNestedJsonStructure() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test_nested.json").toFile();
        JsonParser parser = new JsonParser();

        // Create test JSON file with nested structure
        String jsonContent = """
            {
              "name": "John Doe",
              "age": 30,
              "address": {
                "street": "123 Main St",
                "city": "New York"
              },
              "hobbies": ["reading", "swimming"]
            }
            """;
        java.nio.file.Files.writeString(testFile.toPath(), jsonContent);

        // Act
        List<DataRecord> records = parser.parse(testFile);

        // Assert
        assertNotNull(records);
        assertEquals(1, records.size());

        DataRecord record = records.get(0);
        assertEquals("John Doe", record.getField("name"));
        assertEquals(30, record.getField("age"));

        // Nested object should be converted to Map
        Object address = record.getField("address");
        assertNotNull(address);
        assertTrue(address instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> addressMap = (Map<String, Object>) address;
        assertEquals("123 Main St", addressMap.get("street"));
        assertEquals("New York", addressMap.get("city"));

        // Array should be converted to List
        Object hobbies = record.getField("hobbies");
        assertNotNull(hobbies);
        assertTrue(hobbies instanceof List);
    }

    @Test
    void testParseInvalidJson() {
        // Arrange
        File testFile = tempDir.resolve("test_invalid.json").toFile();
        JsonParser parser = new JsonParser();

        // Create invalid JSON file
        String jsonContent = """
            {
              "name": "John Doe",
              "age": 30,
              "email": "john@example.com"
            """; // Missing closing brace
        try {
            java.nio.file.Files.writeString(testFile.toPath(), jsonContent);
        } catch (Exception e) {
            fail("Failed to create test file: " + e.getMessage());
        }

        // Act & Assert
        assertThrows(FileConversionException.class, () -> parser.parse(testFile));
    }

    @Test
    void testParseNullFile() {
        // Arrange
        JsonParser parser = new JsonParser();

        // Act & Assert
        assertThrows(FileConversionException.class, () -> parser.parse(null));
    }

    @Test
    void testParseNonExistentFile() {
        // Arrange
        File nonExistentFile = new File("non_existent_file.json");
        JsonParser parser = new JsonParser();

        // Act & Assert
        assertThrows(FileConversionException.class, () -> parser.parse(nonExistentFile));
    }

    @Test
    void testParseEmptyJsonObject() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test_empty.json").toFile();
        JsonParser parser = new JsonParser();

        // Create empty JSON object
        String jsonContent = "{}";
        java.nio.file.Files.writeString(testFile.toPath(), jsonContent);

        // Act
        List<DataRecord> records = parser.parse(testFile);

        // Assert
        assertNotNull(records);
        assertEquals(1, records.size());
        assertEquals(0, records.get(0).size());
    }

    @Test
    void testParseJsonWithNullValues() throws Exception {
        // Arrange
        File testFile = tempDir.resolve("test_null.json").toFile();
        JsonParser parser = new JsonParser();

        // Create JSON with null values
        String jsonContent = """
            {
              "name": "John Doe",
              "age": null,
              "email": "john@example.com"
            }
            """;
        java.nio.file.Files.writeString(testFile.toPath(), jsonContent);

        // Act
        List<DataRecord> records = parser.parse(testFile);

        // Assert
        assertNotNull(records);
        assertEquals(1, records.size());

        DataRecord record = records.get(0);
        assertEquals("John Doe", record.getField("name"));
        assertNull(record.getField("age"));
        assertEquals("john@example.com", record.getField("email"));
    }
}
