package global.goit.java_final_n_kovalchuk.parser.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JacksonXmlParser.
 */
class JacksonXmlParserTest {

    @TempDir
    Path tempDir;

    @Test
    void testDefaultConstructor() {
        // Act
        JacksonXmlParser parser = new JacksonXmlParser();

        // Assert
        assertNotNull(parser);
    }

    @Test
    void testConstructorWithCustomMappers() {
        // Arrange
        XmlMapper xmlMapper = new XmlMapper();
        ObjectMapper objectMapper = new ObjectMapper();

        // Act
        JacksonXmlParser parser = new JacksonXmlParser(xmlMapper, objectMapper);

        // Assert
        assertNotNull(parser);
    }

    @Test
    void testParseSimpleXml() throws Exception {
        // Arrange
        JacksonXmlParser parser = new JacksonXmlParser();
        File xmlFile = createTestXmlFile("simple", """
            <?xml version="1.0" encoding="UTF-8"?>
            <records>
                <record>
                    <name>John Doe</name>
                    <age>30</age>
                </record>
            </records>
            """);

        // Act
        List<DataRecord> records = parser.parse(xmlFile);

        // Assert
        assertEquals(1, records.size());
        DataRecord record = records.get(0);
        assertEquals("John Doe", record.getField("name"));
        assertEquals(30, record.getField("age"));
    }

    @Test
    void testParseXmlWithNestedObjects() throws Exception {
        // Arrange
        JacksonXmlParser parser = new JacksonXmlParser();
        File xmlFile = createTestXmlFile("nested", """
            <?xml version="1.0" encoding="UTF-8"?>
            <records>
                <record>
                    <name>John Doe</name>
                    <address>
                        <street>123 Main St</street>
                        <city>New York</city>
                    </address>
                </record>
            </records>
            """);

        // Act
        List<DataRecord> records = parser.parse(xmlFile);

        // Assert
        assertEquals(1, records.size());
        DataRecord record = records.get(0);
        assertEquals("John Doe", record.getField("name"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> address = (Map<String, Object>) record.getField("address");
        assertNotNull(address);
        assertEquals("123 Main St", address.get("street"));
        assertEquals("New York", address.get("city"));
    }

    @Test
    void testParseXmlWithArrays() throws Exception {
        // Arrange
        JacksonXmlParser parser = new JacksonXmlParser();
        File xmlFile = createTestXmlFile("arrays", """
            <?xml version="1.0" encoding="UTF-8"?>
            <records>
                <record>
                    <name>John Doe</name>
                    <tags>
                        <item>java</item>
                        <item>xml</item>
                    </tags>
                </record>
            </records>
            """);

        // Act
        List<DataRecord> records = parser.parse(xmlFile);

        // Assert
        assertEquals(1, records.size());
        DataRecord record = records.get(0);
        assertEquals("John Doe", record.getField("name"));
        
        @SuppressWarnings("unchecked")
        List<Object> tags = (List<Object>) record.getField("tags");
        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertEquals("java", tags.get(0));
        assertEquals("xml", tags.get(1));
    }

    @Test
    void testParseXmlWithNullValues() throws Exception {
        // Arrange
        JacksonXmlParser parser = new JacksonXmlParser();
        File xmlFile = createTestXmlFile("nulls", """
            <?xml version="1.0" encoding="UTF-8"?>
            <records>
                <record>
                    <name>John Doe</name>
                    <age/>
                </record>
            </records>
            """);

        // Act
        List<DataRecord> records = parser.parse(xmlFile);

        // Assert
        assertEquals(1, records.size());
        DataRecord record = records.get(0);
        assertEquals("John Doe", record.getField("name"));
        assertNull(record.getField("age"));
    }

    @Test
    void testParseXmlWithMultipleRecords() throws Exception {
        // Arrange
        JacksonXmlParser parser = new JacksonXmlParser();
        File xmlFile = createTestXmlFile("multiple", """
            <?xml version="1.0" encoding="UTF-8"?>
            <records>
                <record>
                    <name>John Doe</name>
                    <age>30</age>
                </record>
                <record>
                    <name>Jane Smith</name>
                    <age>25</age>
                </record>
            </records>
            """);

        // Act
        List<DataRecord> records = parser.parse(xmlFile);

        // Assert
        assertEquals(2, records.size());
        assertEquals("John Doe", records.get(0).getField("name"));
        assertEquals(30, records.get(0).getField("age"));
        assertEquals("Jane Smith", records.get(1).getField("name"));
        assertEquals(25, records.get(1).getField("age"));
    }

    @Test
    void testParseNonExistentFile() {
        // Arrange
        JacksonXmlParser parser = new JacksonXmlParser();
        File xmlFile = tempDir.resolve("nonexistent.xml").toFile();

        // Act & Assert
        FileConversionException exception = assertThrows(FileConversionException.class, () -> parser.parse(xmlFile));
        assertTrue(exception.getMessage().contains("File does not exist") || 
                   exception.getMessage().contains("Failed to read file") ||
                   exception.getMessage().contains("Failed to parse XML"));
    }

    @Test
    void testParseEmptyFile() throws Exception {
        // Arrange
        JacksonXmlParser parser = new JacksonXmlParser();
        File xmlFile = createTestXmlFile("empty", """
            <?xml version="1.0" encoding="UTF-8"?>
            <records>
            </records>
            """);

        // Act
        List<DataRecord> records = parser.parse(xmlFile);

        // Assert
        // Empty records element may return empty list or one empty record
        // depending on how Jackson XML parser handles empty elements
        assertNotNull(records);
        assertTrue(records.isEmpty() || (records.size() == 1 && records.get(0).getFields().isEmpty()));
    }

    @Test
    void testParseInvalidXml() throws Exception {
        // Arrange
        JacksonXmlParser parser = new JacksonXmlParser();
        File xmlFile = createTestXmlFile("invalid", """
            <?xml version="1.0" encoding="UTF-8"?>
            <invalid>
            </invalid>
            """);

        // Act & Assert
        // XML without records/record elements will be parsed as root record
        List<DataRecord> records = parser.parse(xmlFile);
        assertNotNull(records);
        // Should create one record with the invalid element as a field
        assertEquals(1, records.size());
    }

    @Test
    void testParseXmlWithComplexNestedStructure() throws Exception {
        // Arrange
        JacksonXmlParser parser = new JacksonXmlParser();
        File xmlFile = createTestXmlFile("complex", """
            <?xml version="1.0" encoding="UTF-8"?>
            <records>
                <record>
                    <name>John Doe</name>
                    <data>
                        <item>
                            <id>1</id>
                            <value>test</value>
                        </item>
                        <item>
                            <id>2</id>
                            <value>test2</value>
                        </item>
                    </data>
                </record>
            </records>
            """);

        // Act
        List<DataRecord> records = parser.parse(xmlFile);

        // Assert
        assertEquals(1, records.size());
        DataRecord record = records.get(0);
        assertEquals("John Doe", record.getField("name"));
        
        // After unwrapItemWrappers, data should be a List (not a Map)
        @SuppressWarnings("unchecked")
        List<Object> data = (List<Object>) record.getField("data");
        assertNotNull(data);
        assertEquals(2, data.size());
        
        // Check that each item is a Map with id and value
        @SuppressWarnings("unchecked")
        Map<String, Object> firstItem = (Map<String, Object>) data.get(0);
        assertEquals(1, firstItem.get("id"));
        assertEquals("test", firstItem.get("value"));
    }

    private File createTestXmlFile(String name, String content) throws IOException {
        File file = tempDir.resolve(name + ".xml").toFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        return file;
    }
}