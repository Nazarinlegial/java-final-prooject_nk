package global.goit.java_final_n_kovalchuk.writer.json;

import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import global.goit.java_final_n_kovalchuk.parser.json.JsonParser;
import global.goit.java_final_n_kovalchuk.writer.json.JsonWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JsonWriter.
 */
class JsonWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void testWriteSingleRecord() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_single.json").toFile();
        JsonWriter writer = new JsonWriter();
        JsonParser parser = new JsonParser();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");
        record.addField("age", 30);
        record.addField("email", "john@example.com");

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);

        // Verify by parsing back
        List<DataRecord> parsedRecords = parser.parse(outputFile);
        assertEquals(1, parsedRecords.size());

        DataRecord parsedRecord = parsedRecords.get(0);
        assertEquals("John Doe", parsedRecord.getField("name"));
        assertEquals(30, parsedRecord.getField("age"));
        assertEquals("john@example.com", parsedRecord.getField("email"));
    }

    @Test
    void testWriteMultipleRecords() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_multiple.json").toFile();
        JsonWriter writer = new JsonWriter();
        JsonParser parser = new JsonParser();

        DataRecord record1 = new DataRecord();
        record1.addField("name", "John Doe");
        record1.addField("age", 30);
        record1.addField("email", "john@example.com");

        DataRecord record2 = new DataRecord();
        record2.addField("name", "Jane Smith");
        record2.addField("age", 25);
        record2.addField("email", "jane@example.com");

        List<DataRecord> records = List.of(record1, record2);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);

        // Verify by parsing back
        List<DataRecord> parsedRecords = parser.parse(outputFile);
        assertEquals(2, parsedRecords.size());

        DataRecord firstRecord = parsedRecords.get(0);
        assertEquals("John Doe", firstRecord.getField("name"));
        assertEquals(30, firstRecord.getField("age"));

        DataRecord secondRecord = parsedRecords.get(1);
        assertEquals("Jane Smith", secondRecord.getField("name"));
        assertEquals(25, secondRecord.getField("age"));
        assertEquals("jane@example.com", secondRecord.getField("email"));
    }

    @Test
    void testWriteNestedStructures() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_nested.json").toFile();
        JsonWriter writer = new JsonWriter();
        JsonParser parser = new JsonParser();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");
        record.addField("age", 30);

        // Add nested structure
        List<String> hobbies = List.of("reading", "swimming");
        record.addField("hobbies", hobbies);

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);

        // Verify by parsing back
        List<DataRecord> parsedRecords = parser.parse(outputFile);
        assertEquals(1, parsedRecords.size());

        DataRecord parsedRecord = parsedRecords.get(0);
        assertEquals("John Doe", parsedRecord.getField("name"));
        assertEquals(30, parsedRecord.getField("age"));

        Object parsedHobbies = parsedRecord.getField("hobbies");
        assertNotNull(parsedHobbies);
        assertTrue(parsedHobbies instanceof List);
    }

    @Test
    void testWriteWithNullValues() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_null.json").toFile();
        JsonWriter writer = new JsonWriter();
        JsonParser parser = new JsonParser();

        DataRecord record1 = new DataRecord();
        record1.addField("name", "John Doe");
        record1.addField("age", null);
        record1.addField("email", "john@example.com");

        DataRecord record2 = new DataRecord();
        record2.addField("name", null);
        record2.addField("age", 25);
        record2.addField("email", "jane@example.com");

        List<DataRecord> records = List.of(record1, record2);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());

        // Verify by parsing back
        List<DataRecord> parsedRecords = parser.parse(outputFile);
        assertEquals(2, parsedRecords.size());

        DataRecord firstRecord = parsedRecords.get(0);
        assertEquals("John Doe", firstRecord.getField("name"));
        assertNull(firstRecord.getField("age"));
        assertEquals("john@example.com", firstRecord.getField("email"));

        DataRecord secondRecord = parsedRecords.get(1);
        assertNull(secondRecord.getField("name"));
        assertEquals(25, secondRecord.getField("age"));
        assertEquals("jane@example.com", secondRecord.getField("email"));
    }

    @Test
    void testWriteWithNullRecordsList() throws IOException {
        // Arrange
        File outputFile = tempDir.resolve("output_empty.json").toFile();
        JsonWriter writer = new JsonWriter();

        List<DataRecord> records = new ArrayList<>();

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());

        String content = Files.readString(outputFile.toPath());
        // Jackson with INDENT_OUTPUT writes empty array as "[ ]" (with space)
        assertTrue(content.contains("[") && content.contains("]"));
    }

    @Test
    void testWriteWithNullRecordsListThrows() {
        // Arrange
        File outputFile = tempDir.resolve("output_null.json").toFile();
        JsonWriter writer = new JsonWriter();

        // Act & Assert
        assertThrows(FileConversionException.class, () -> writer.write(null, outputFile));
    }

    @Test
    void testWriteWithNullFile() {
        // Arrange
        JsonWriter writer = new JsonWriter();
        List<DataRecord> records = List.of(new DataRecord());

        // Act & Assert
        assertThrows(FileConversionException.class, () -> writer.write(records, null));
    }

    @Test
    void testWriteCreatesParentDirectory() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("subdir/output.json").toFile();
        JsonWriter writer = new JsonWriter();
        JsonParser parser = new JsonParser();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        assertTrue(outputFile.getParentFile().exists());

        // Verify by parsing back
        List<DataRecord> parsedRecords = parser.parse(outputFile);
        assertEquals(1, parsedRecords.size());
        assertEquals("John Doe", parsedRecords.get(0).getField("name"));
    }

    @Test
    void testVerifyOutputFormat() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_format.json").toFile();
        JsonWriter writer = new JsonWriter();
        JsonParser parser = new JsonParser();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");
        record.addField("age", 30);

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);

        String content = Files.readString(outputFile.toPath());

        // Verify JSON is properly formatted with indentation
        assertTrue(content.contains("\n"));
        assertTrue(content.contains("  "));
        assertTrue(content.contains("\"name\""));
        assertTrue(content.contains("\"age\""));
        assertTrue(content.contains("John Doe"));
        assertTrue(content.contains("30"));
    }

    @Test
    void testWriteWithCustomObjectMapper() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_custom.json").toFile();
        com.fasterxml.jackson.databind.ObjectMapper customMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        JsonWriter writer = new JsonWriter(customMapper);
        JsonParser parser = new JsonParser();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");
        record.addField("age", 30);

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());

        // Verify by parsing back
        List<DataRecord> parsedRecords = parser.parse(outputFile);
        assertEquals(1, parsedRecords.size());
        assertEquals("John Doe", parsedRecords.get(0).getField("name"));
        assertEquals(30, parsedRecords.get(0).getField("age"));
    }
}
