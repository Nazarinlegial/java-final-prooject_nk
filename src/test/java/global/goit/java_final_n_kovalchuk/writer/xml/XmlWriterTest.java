package global.goit.java_final_n_kovalchuk.writer.xml;

import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import global.goit.java_final_n_kovalchuk.parser.json.JsonParser;
import global.goit.java_final_n_kovalchuk.parser.xml.JacksonXmlParser;
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
 * Unit tests for XmlWriter.
 */
class XmlWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void testWriteRecordsToXml() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output.xml").toFile();
        XmlWriter writer = new XmlWriter();
        JacksonXmlParser parser = new JacksonXmlParser();

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
        assertEquals(30, firstRecord.getField("age"));
        assertEquals("john@example.com", firstRecord.getField("email"));

        DataRecord secondRecord = parsedRecords.get(1);
        assertEquals("Jane Smith", secondRecord.getField("name"));
        assertEquals(25, secondRecord.getField("age"));
        assertEquals("jane@example.com", secondRecord.getField("email"));
    }

    @Test
    void testWriteNestedStructures() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_nested.xml").toFile();
        XmlWriter writer = new XmlWriter();
        JacksonXmlParser parser = new JacksonXmlParser();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");
        record.addField("age", "30");
        record.addField("street", "123 Main St");
        record.addField("city", "New York");

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
        assertEquals(30, parsedRecord.getField("age"));
        assertEquals("123 Main St", parsedRecord.getField("street"));
        assertEquals("New York", parsedRecord.getField("city"));
    }

    @Test
    void testWriteWithNullValues() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_null.xml").toFile();
        XmlWriter writer = new XmlWriter();
        JacksonXmlParser parser = new JacksonXmlParser();

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
        assertEquals(25, secondRecord.getField("age"));
        assertEquals("jane@example.com", secondRecord.getField("email"));
    }

    @Test
    void testWriteEmptyRecords() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_empty.xml").toFile();
        XmlWriter writer = new XmlWriter();

        List<DataRecord> records = new ArrayList<>();

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());

        String content = Files.readString(outputFile.toPath());
        assertTrue(content.contains("<records>"));
        assertTrue(content.contains("</records>"));
    }

    @Test
    void testWriteWithNullRecordsList() {
        // Arrange
        File outputFile = tempDir.resolve("output_null.xml").toFile();
        XmlWriter writer = new XmlWriter();

        // Act & Assert
        assertThrows(FileConversionException.class, () -> writer.write(null, outputFile));
    }

    @Test
    void testWriteWithNullFile() {
        // Arrange
        XmlWriter writer = new XmlWriter();
        List<DataRecord> records = List.of(new DataRecord());

        // Act & Assert
        assertThrows(FileConversionException.class, () -> writer.write(records, null));
    }

    @Test
    void testWriteCreatesParentDirectory() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("subdir/output.xml").toFile();
        XmlWriter writer = new XmlWriter();

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
        File outputFile = tempDir.resolve("output_format.xml").toFile();
        XmlWriter writer = new XmlWriter();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");
        record.addField("age", "30");

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        String content = Files.readString(outputFile.toPath());

        // Verify XML format
        assertTrue(content.contains("<?xml"));
        assertTrue(content.contains("<records>"));
        assertTrue(content.contains("</records>"));
        assertTrue(content.contains("<record>"));
        assertTrue(content.contains("</record>"));
        assertTrue(content.contains("<name>"));
        assertTrue(content.contains("</name>"));
        assertTrue(content.contains("<age>"));
        assertTrue(content.contains("</age>"));
        assertTrue(content.contains("John Doe"));
        assertTrue(content.contains("30"));

        // Verify indentation
        assertTrue(content.contains("  "));
        assertTrue(content.contains("\n"));
    }

    @Test
    void testWriteWithSpecialCharacters() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_special.xml").toFile();
        XmlWriter writer = new XmlWriter();
        JacksonXmlParser parser = new JacksonXmlParser();

        DataRecord record = new DataRecord();
        record.addField("name", "John & Jane");
        record.addField("description", "Price: < $100");

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());

        // Verify by parsing back
        List<DataRecord> parsedRecords = parser.parse(outputFile);
        assertEquals(1, parsedRecords.size());

        DataRecord parsedRecord = parsedRecords.get(0);
        assertEquals("John & Jane", parsedRecord.getField("name"));
        assertEquals("Price: < $100", parsedRecord.getField("description"));
    }

    @Test
    void testWriteWithNumericValues() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_numeric.xml").toFile();
        XmlWriter writer = new XmlWriter();
        JacksonXmlParser parser = new JacksonXmlParser();

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
        assertEquals(30, parsedRecord.getField("age"));
        assertEquals(50000.5, parsedRecord.getField("salary"));
    }

    @Test
    void testWriteWithDifferentFields() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_diff.xml").toFile();
        XmlWriter writer = new XmlWriter();
        JacksonXmlParser parser = new JacksonXmlParser();

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
        assertEquals(30, firstRecord.getField("age"));
        assertNull(firstRecord.getField("email"));

        DataRecord secondRecord = parsedRecords.get(1);
        assertEquals("Jane Smith", secondRecord.getField("name"));
        assertEquals("jane@example.com", secondRecord.getField("email"));
        assertNull(secondRecord.getField("age"));
    }

    @Test
    void testWriteWithNestedMap() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_nested_map.xml").toFile();
        XmlWriter writer = new XmlWriter();

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
        
        // Verify XML structure contains nested address elements
        assertTrue(content.contains("<?xml"));
        assertTrue(content.contains("<records>"));
        assertTrue(content.contains("<record>"));
        assertTrue(content.contains("<address>"));
        assertTrue(content.contains("</address>"));
        assertTrue(content.contains("<street>"));
        assertTrue(content.contains("123 Main St"));
        assertTrue(content.contains("<city>"));
        assertTrue(content.contains("New York"));
        assertTrue(content.contains("<zipcode>"));
        assertTrue(content.contains("10001"));
        assertTrue(content.contains("<country>"));
        assertTrue(content.contains("USA"));
        assertTrue(content.contains("</records>"));
    }

    @Test
    void testWriteWithNestedList() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_nested_list.xml").toFile();
        XmlWriter writer = new XmlWriter();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");
        record.addField("age", "30");

        // Create nested List for tags
        List<Object> tags = new ArrayList<>();
        tags.add("developer");
        tags.add("java");
        tags.add("xml");

        record.addField("tags", tags);

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());

        String content = Files.readString(outputFile.toPath());
        
        // Verify XML structure contains item elements for list
        assertTrue(content.contains("<?xml"));
        assertTrue(content.contains("<records>"));
        assertTrue(content.contains("<record>"));
        assertTrue(content.contains("<tags>"));
        assertTrue(content.contains("</tags>"));
        assertTrue(content.contains("<item>"));
        assertTrue(content.contains("developer"));
        assertTrue(content.contains("java"));
        assertTrue(content.contains("xml"));
        assertTrue(content.contains("</records>"));
    }

    @Test
    void testWriteWithDeeplyNestedStructures() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_deeply_nested.xml").toFile();
        XmlWriter writer = new XmlWriter();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");

        // Create deeply nested structure: Map containing List, which contains Map
        Map<String, Object> metadata = new HashMap<>();
        
        List<Object> items = new ArrayList<>();
        
        Map<String, Object> item1 = new HashMap<>();
        item1.put("id", "1");
        item1.put("value", "first");
        items.add(item1);
        
        Map<String, Object> item2 = new HashMap<>();
        item2.put("id", "2");
        item2.put("value", "second");
        items.add(item2);
        
        metadata.put("items", items);
        metadata.put("count", "2");

        record.addField("metadata", metadata);

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());

        String content = Files.readString(outputFile.toPath());
        
        // Verify XML structure contains deeply nested elements
        assertTrue(content.contains("<?xml"));
        assertTrue(content.contains("<records>"));
        assertTrue(content.contains("<record>"));
        assertTrue(content.contains("<metadata>"));
        assertTrue(content.contains("<items>"));
        assertTrue(content.contains("<item>"));
        assertTrue(content.contains("<id>"));
        assertTrue(content.contains("1"));
        assertTrue(content.contains("2"));
        assertTrue(content.contains("<value>"));
        assertTrue(content.contains("first"));
        assertTrue(content.contains("second"));
        assertTrue(content.contains("<count>"));
        assertTrue(content.contains("2"));
        assertTrue(content.contains("</records>"));
    }

    @Test
    void testWriteWithPersonsJsonData() throws Exception {
        // Arrange
        File personsJsonFile = new File("examples/persons.json");
        File outputFile = tempDir.resolve("persons_output.xml").toFile();
        
        JsonParser jsonParser = new JsonParser();
        XmlWriter writer = new XmlWriter();

        // Act
        List<DataRecord> records = jsonParser.parse(personsJsonFile);
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);

        String content = Files.readString(outputFile.toPath());
        
        // Verify XML structure
        assertTrue(content.contains("<?xml"));
        assertTrue(content.contains("<records>"));
        assertTrue(content.contains("<record>"));

        // Verify first person's data is present
        assertTrue(content.contains("Lucie"));
        assertTrue(content.contains("Caron"));

        // Verify address field (nested Map) is present
        assertTrue(content.contains("<address>"));
        assertTrue(content.contains("<street>"));
        assertTrue(content.contains("<city>"));

        // Verify multiple records are present
        assertTrue(content.contains("Zacharie"));
        assertTrue(content.contains("Guyot"));
        assertTrue(content.contains("Benjamin"));
        assertTrue(content.contains("Herve"));
    }

    @Test
    void testWriteWithNestedListInList() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_list_in_list.xml").toFile();
        XmlWriter writer = new XmlWriter();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");

        // Create List containing nested List
        List<Object> outerList = new ArrayList<>();
        
        List<Object> innerList1 = new ArrayList<>();
        innerList1.add("a");
        innerList1.add("b");
        
        List<Object> innerList2 = new ArrayList<>();
        innerList2.add("c");
        innerList2.add("d");
        
        outerList.add(innerList1);
        outerList.add(innerList2);

        record.addField("nestedLists", outerList);

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());

        String content = Files.readString(outputFile.toPath());
        
        // Verify XML structure contains nested lists
        assertTrue(content.contains("<?xml"));
        assertTrue(content.contains("<records>"));
        assertTrue(content.contains("<record>"));
        assertTrue(content.contains("<nestedLists>"));
        assertTrue(content.contains("<item>"));
        assertTrue(content.contains("a"));
        assertTrue(content.contains("b"));
        assertTrue(content.contains("c"));
        assertTrue(content.contains("d"));
        assertTrue(content.contains("</records>"));
    }

    @Test
    void testWriteWithInvalidCharacters() throws Exception {
        // Arrange
        File outputFile = tempDir.resolve("output_invalid_chars.xml").toFile();
        XmlWriter writer = new XmlWriter();

        DataRecord record = new DataRecord();
        record.addField("name", "John Doe");
        record.addField("description", "Test with special chars: < > & \" '");

        List<DataRecord> records = List.of(record);

        // Act
        writer.write(records, outputFile);

        // Assert
        assertTrue(outputFile.exists());

        String content = Files.readString(outputFile.toPath());
        
        // Verify special characters are properly escaped
        assertTrue(content.contains("<?xml"));
        assertTrue(content.contains("<records>"));
        assertTrue(content.contains("<record>"));
        assertTrue(content.contains("<name>"));
        assertTrue(content.contains("John Doe"));
        assertTrue(content.contains("<description>"));
        assertTrue(content.contains("</records>"));
    }
}
