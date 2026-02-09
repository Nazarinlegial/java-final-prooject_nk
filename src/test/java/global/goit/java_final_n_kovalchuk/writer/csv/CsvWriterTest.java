package global.goit.java_final_n_kovalchuk.writer.csv;

import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import global.goit.java_final_n_kovalchuk.parser.csv.CsvParser;
import global.goit.java_final_n_kovalchuk.parser.json.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CsvWriter.
 */
class CsvWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void testWriteRecordsToCsv() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output.csv").toFile();
        CsvWriter writer = new CsvWriter();
        CsvParser parser = new CsvParser();

        DataRecord record1 = new DataRecord();
        record1.addField("name", "John Doe");
        record1.addField("age", "30");
        record1.addField("email", "john@example.com");

        DataRecord record2 = new DataRecord();
        record2.addField("name", "Jane Smith");
        record2.addField("age", "25");
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
        assertEquals("30", firstRecord.getField("age"));
        assertEquals("john@example.com", firstRecord.getField("email"));

        DataRecord secondRecord = parsedRecords.get(1);
        assertEquals("Jane Smith", secondRecord.getField("name"));
        assertEquals("25", secondRecord.getField("age"));
        assertEquals("jane@example.com", secondRecord.getField("email"));
    }

    @Test
    void testWriteWithSpecialCharacters() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_special.csv").toFile();
        CsvWriter writer = new CsvWriter();
        CsvParser parser = new CsvParser();

        DataRecord record = new DataRecord();
        record.addField("name", "John O'Connor");
        record.addField("email", "john.o'connor@example.com");
        record.addField("description", "A person with, a comma");

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());

        // Verify by parsing back
        List<DataRecord> parsedRecords = parser.parse(outputFile);
        assertEquals(1, parsedRecords.size());

        DataRecord parsedRecord = parsedRecords.get(0);
        assertEquals("John O'Connor", parsedRecord.getField("name"));
        assertEquals("john.o'connor@example.com", parsedRecord.getField("email"));
        assertEquals("A person with, a comma", parsedRecord.getField("description"));
    }

    @Test
    void testWriteWithNullValues() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_null.csv").toFile();
        CsvWriter writer = new CsvWriter();
        CsvParser parser = new CsvParser();

        DataRecord record1 = new DataRecord();
        record1.addField("name", "John Doe");
        record1.addField("age", null);
        record1.addField("email", "john@example.com");

        DataRecord record2 = new DataRecord();
        record2.addField("name", null);
        record2.addField("age", "25");
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
        assertEquals("25", secondRecord.getField("age"));
        assertEquals("jane@example.com", secondRecord.getField("email"));
    }

    @Test
    void testWriteWithDifferentFields() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_diff.csv").toFile();
        CsvWriter writer = new CsvWriter();
        CsvParser parser = new CsvParser();

        DataRecord record1 = new DataRecord();
        record1.addField("name", "John Doe");
        record1.addField("age", "30");

        DataRecord record2 = new DataRecord();
        record2.addField("name", "Jane Smith");
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
        assertEquals("30", firstRecord.getField("age"));
        assertNull(firstRecord.getField("email"));

        DataRecord secondRecord = parsedRecords.get(1);
        assertEquals("Jane Smith", secondRecord.getField("name"));
        assertEquals("jane@example.com", secondRecord.getField("email"));
        assertNull(secondRecord.getField("age"));
    }

    @Test
    void testWriteEmptyRecords() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_empty.csv").toFile();
        CsvWriter writer = new CsvWriter();

        List<DataRecord> records = new ArrayList<>();

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());

        String content = Files.readString(outputFile.toPath());
        // Should only have headers (empty list means no headers)
        assertTrue(content.isEmpty() || content.trim().isEmpty());
    }

    @Test
    void testWriteWithNullRecordsList() {
        // Arrange
        File outputFile = tempDir.resolve("output_null.csv").toFile();
        CsvWriter writer = new CsvWriter();

        // Act & Assert
        assertThrows(FileConversionException.class, () -> writer.write(null, outputFile));
    }

    @Test
    void testWriteWithNullFile() {
        // Arrange
        CsvWriter writer = new CsvWriter();
        List<DataRecord> records = List.of(new DataRecord());

        // Act & Assert
        assertThrows(FileConversionException.class, () -> writer.write(records, null));
    }

    @Test
    void testWriteCreatesParentDirectory() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("subdir/output.csv").toFile();
        CsvWriter writer = new CsvWriter();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        assertTrue(outputFile.getParentFile().exists());
    }

    @Test
    void testVerifyOutputFormat() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_format.csv").toFile();
        CsvWriter writer = new CsvWriter();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");
        record.addField("age", "30");

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        String content = Files.readString(outputFile.toPath());

        // Verify CSV format
        assertTrue(content.contains("name"));
        assertTrue(content.contains("age"));
        assertTrue(content.contains("John Doe"));
        assertTrue(content.contains("30"));
        assertTrue(content.contains("\n"));
    }

    @Test
    void testWriteWithNumericValues() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_numeric.csv").toFile();
        CsvWriter writer = new CsvWriter();
        CsvParser parser = new CsvParser();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");
        record.addField("age", 30);
        record.addField("salary", 50000.50);

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());

        // Verify by parsing back
        List<DataRecord> parsedRecords = parser.parse(outputFile);
        assertEquals(1, parsedRecords.size());

        DataRecord parsedRecord = parsedRecords.get(0);
        assertEquals("John Doe", parsedRecord.getField("name"));
        assertEquals("30", parsedRecord.getField("age"));
        assertEquals("50000.5", parsedRecord.getField("salary"));
    }

    @Test
    void testWriteWithQuotedValues() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_quoted.csv").toFile();
        CsvWriter writer = new CsvWriter();
        CsvParser parser = new CsvParser();

        DataRecord record = new DataRecord();
        record.addField("name", "John, Doe");
        record.addField("description", "A person with \"quotes\"");

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());

        // Verify by parsing back
        List<DataRecord> parsedRecords = parser.parse(outputFile);
        assertEquals(1, parsedRecords.size());

        DataRecord parsedRecord = parsedRecords.get(0);
        assertEquals("John, Doe", parsedRecord.getField("name"));
        assertEquals("A person with \"quotes\"", parsedRecord.getField("description"));
    }

    @Test
    void testWriteWithNestedMap() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_nested_map.csv").toFile();
        CsvWriter writer = new CsvWriter();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");
        record.addField("age", "30");
        
        // Create nested Map for address
        Map<String, Object> address = new HashMap<>();
        address.put("street", "123 Main St");
        address.put("city", "New York");
        address.put("zipcode", "10001");
        address.put("country", "USA");
        record.addField("address", address);

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        String content = Files.readString(outputFile.toPath());
        
        // Verify JSON format for nested Map (should contain curly braces and quotes)
        assertTrue(content.contains("address"), "CSV should contain address column");
        assertTrue(content.contains("{"), "JSON should contain opening curly brace");
        assertTrue(content.contains("}"), "JSON should contain closing curly brace");
        assertTrue(content.contains("\"street\""), "JSON should contain quoted street key");
        assertTrue(content.contains("\"123 Main St\""), "JSON should contain quoted street value");
        assertTrue(content.contains("\"city\""), "JSON should contain quoted city key");
        assertTrue(content.contains("\"New York\""), "JSON should contain quoted city value");
        assertTrue(content.contains("\"zipcode\""), "JSON should contain quoted zipcode key");
        assertTrue(content.contains("\"10001\""), "JSON should contain quoted zipcode value");
        assertTrue(content.contains("\"country\""), "JSON should contain quoted country key");
        assertTrue(content.contains("\"USA\""), "JSON should contain quoted country value");
    }

    @Test
    void testWriteWithNestedList() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_nested_list.csv").toFile();
        CsvWriter writer = new CsvWriter();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");
        record.addField("age", "30");
        
        // Create nested List for tags
        List<String> tags = new ArrayList<>();
        tags.add("developer");
        tags.add("java");
        tags.add("backend");
        record.addField("tags", tags);

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        String content = Files.readString(outputFile.toPath());
        
        // Verify JSON format for nested List (should contain square brackets)
        assertTrue(content.contains("tags"), "CSV should contain tags column");
        assertTrue(content.contains("["), "JSON should contain opening square bracket");
        assertTrue(content.contains("]"), "JSON should contain closing square bracket");
        assertTrue(content.contains("\"developer\""), "JSON should contain quoted developer tag");
        assertTrue(content.contains("\"java\""), "JSON should contain quoted java tag");
        assertTrue(content.contains("\"backend\""), "JSON should contain quoted backend tag");
    }

    @Test
    void testWriteWithComplexNestedStructures() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_complex_nested.csv").toFile();
        CsvWriter writer = new CsvWriter();

        DataRecord record = new DataRecord();
        record.addField("id", "1");
        record.addField("firstname", "Lucie");
        record.addField("lastname", "Caron");
        record.addField("email", "paul.bernier@club-internet.fr");
        record.addField("phone", "+33780559746");
        record.addField("birthday", "1972-09-28");
        record.addField("gender", "female");
        
        // Create nested Map for address (similar to persons.json structure)
        Map<String, Object> address = new HashMap<>();
        address.put("id", "1");
        address.put("street", "673, boulevard Laetitia Evrard");
        address.put("streetName", "chemin Nath Masson");
        address.put("buildingNumber", "13");
        address.put("city", "Meyer");
        address.put("zipcode", "63379");
        address.put("country", "Grenade");
        address.put("country_code", "GD");
        address.put("latitude", -50.305502);
        address.put("longitude", -164.550901);
        record.addField("address", address);
        
        record.addField("website", "http://poulain.fr");
        record.addField("image", "http://placeimg.com/640/480/people");

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        String content = Files.readString(outputFile.toPath());
        
        // Verify JSON format for nested address Map
        assertTrue(content.contains("address"), "CSV should contain address column");
        assertTrue(content.contains("{"), "JSON should contain opening curly brace");
        assertTrue(content.contains("}"), "JSON should contain closing curly brace");
        assertTrue(content.contains("\"street\""), "JSON should contain quoted street key");
        assertTrue(content.contains("\"673, boulevard Laetitia Evrard\""), "JSON should contain street value");
        assertTrue(content.contains("\"city\""), "JSON should contain quoted city key");
        assertTrue(content.contains("\"Meyer\""), "JSON should contain city value");
        assertTrue(content.contains("\"country\""), "JSON should contain quoted country key");
        assertTrue(content.contains("\"Grenade\""), "JSON should contain country value");
        assertTrue(content.contains("\"country_code\""), "JSON should contain quoted country_code key");
        assertTrue(content.contains("\"GD\""), "JSON should contain country_code value");
        assertTrue(content.contains("\"latitude\""), "JSON should contain quoted latitude key");
        assertTrue(content.contains("-50.305502"), "JSON should contain latitude value");
        assertTrue(content.contains("\"longitude\""), "JSON should contain quoted longitude key");
        assertTrue(content.contains("-164.550901"), "JSON should contain longitude value");
    }

    @Test
    void testWriteWithPersonsJsonData() throws Exception {
        // Arrange
        File personsJsonFile = new File("examples/persons.json");
        File outputFile = tempDir.resolve("output_persons.csv").toFile();
        
        JsonParser jsonParser = new JsonParser();
        CsvWriter writer = new CsvWriter();

        // Act
        List<DataRecord> records = jsonParser.parse(personsJsonFile);
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
        
        String content = Files.readString(outputFile.toPath());
        
        // Verify that the file contains expected columns from persons.json
        assertTrue(content.contains("id"), "CSV should contain id column");
        assertTrue(content.contains("firstname"), "CSV should contain firstname column");
        assertTrue(content.contains("lastname"), "CSV should contain lastname column");
        assertTrue(content.contains("email"), "CSV should contain email column");
        assertTrue(content.contains("phone"), "CSV should contain phone column");
        assertTrue(content.contains("birthday"), "CSV should contain birthday column");
        assertTrue(content.contains("gender"), "CSV should contain gender column");
        assertTrue(content.contains("address"), "CSV should contain address column");
        assertTrue(content.contains("website"), "CSV should contain website column");
        assertTrue(content.contains("image"), "CSV should contain image column");
        
        // Verify JSON format for nested address field
        assertTrue(content.contains("{"), "CSV should contain JSON objects (curly braces)");
        assertTrue(content.contains("}"), "CSV should contain JSON objects (curly braces)");
        assertTrue(content.contains("\"street\""), "CSV should contain JSON with street field");
        assertTrue(content.contains("\"city\""), "CSV should contain JSON with city field");
        assertTrue(content.contains("\"country\""), "CSV should contain JSON with country field");
        
        // Verify that we have data rows (should have 10 persons plus header)
        String[] lines = content.split("\n");
        assertTrue(lines.length > 1, "CSV should have at least one data row");
        
        // Verify some specific person data
        assertTrue(content.contains("Lucie"), "CSV should contain first person's firstname");
        assertTrue(content.contains("Caron"), "CSV should contain first person's lastname");
        assertTrue(content.contains("paul.bernier@club-internet.fr"), "CSV should contain first person's email");
    }

    @Test
    void testWriteWithoutHeaders() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_no_headers.csv").toFile();
        CsvWriter writer = new CsvWriter(false); // Create writer without headers

        DataRecord record1 = new DataRecord();
        record1.addField("name", "John Doe");
        record1.addField("age", "30");
        record1.addField("email", "john@example.com");

        DataRecord record2 = new DataRecord();
        record2.addField("name", "Jane Smith");
        record2.addField("age", "25");
        record2.addField("email", "jane@example.com");

        List<DataRecord> records = List.of(record1, record2);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);

        String content = Files.readString(outputFile.toPath());
        
        // Verify that the file does NOT contain header row
        // The first line should contain data, not field names
        String[] lines = content.split("\n");
        assertTrue(lines.length > 0, "CSV should have at least one line");
        
        String firstLine = lines[0].trim();
        // First line should contain data values, not field names
        assertTrue(firstLine.contains("John Doe"), "First line should contain first record's data");
        assertFalse(firstLine.contains("name,age,email"), "First line should NOT contain header row");
        
        // Verify that data is present
        assertTrue(content.contains("John Doe"));
        assertTrue(content.contains("Jane Smith"));
        assertTrue(content.contains("30"));
        assertTrue(content.contains("25"));
    }

    @Test
    void testWriteWithHeadersDefault() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_with_headers.csv").toFile();
        CsvWriter writer = new CsvWriter(); // Default constructor should write headers

        DataRecord record1 = new DataRecord();
        record1.addField("name", "John Doe");
        record1.addField("age", "30");

        List<DataRecord> records = List.of(record1);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);

        String content = Files.readString(outputFile.toPath());
        
        // Verify that the file contains header row
        String[] lines = content.split("\n");
        assertTrue(lines.length > 0, "CSV should have at least one line");
        
        String firstLine = lines[0].trim();
        // First line should contain field names
        assertTrue(firstLine.contains("name"), "First line should contain field name 'name'");
        assertTrue(firstLine.contains("age"), "First line should contain field name 'age'");
        
        // Verify that data is present in subsequent lines
        assertTrue(content.contains("John Doe"));
        assertTrue(content.contains("30"));
    }

    @Test
    void testWriteWithHeadersExplicit() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_headers_explicit.csv").toFile();
        CsvWriter writer = new CsvWriter(true); // Explicitly enable headers

        DataRecord record1 = new DataRecord();
        record1.addField("name", "John Doe");
        record1.addField("age", "30");

        List<DataRecord> records = List.of(record1);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);

        String content = Files.readString(outputFile.toPath());
        
        // Verify that the file contains header row
        String[] lines = content.split("\n");
        assertTrue(lines.length > 0, "CSV should have at least one line");
        
        String firstLine = lines[0].trim();
        // First line should contain field names
        assertTrue(firstLine.contains("name"), "First line should contain field name 'name'");
        assertTrue(firstLine.contains("age"), "First line should contain field name 'age'");
        
        // Verify that data is present in subsequent lines
        assertTrue(content.contains("John Doe"));
        assertTrue(content.contains("30"));
    }
}
