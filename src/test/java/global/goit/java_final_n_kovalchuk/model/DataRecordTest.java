package global.goit.java_final_n_kovalchuk.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit tests for DataRecord class.
 */
class DataRecordTest {

    @Test
    void testConstructorAndGetters() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John");
        fields.put("age", "30");
        
        DataRecord record = new DataRecord(fields);
        
        assertEquals(2, record.getFields().size());
        assertEquals("John", record.getField("name"));
        assertEquals("30", record.getField("age"));
    }

    @Test
    void testConstructorWithEmptyFields() {
        DataRecord record = new DataRecord(new HashMap<>());
        
        assertNotNull(record.getFields());
        assertTrue(record.getFields().isEmpty());
    }

    @Test
    void testGetField() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Alice");
        fields.put("city", "Kyiv");
        
        DataRecord record = new DataRecord(fields);
        
        assertEquals("Alice", record.getField("name"));
        assertEquals("Kyiv", record.getField("city"));
    }

    @Test
    void testGetFieldNonExistent() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Bob");
        
        DataRecord record = new DataRecord(fields);
        
        assertNull(record.getField("nonexistent"));
    }

    @Test
    void testGetFieldEmptyFieldName() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Charlie");
        
        DataRecord record = new DataRecord(fields);
        
        assertNull(record.getField(""));
    }

    @Test
    void testGetFieldNullFieldName() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Diana");
        
        DataRecord record = new DataRecord(fields);
        
        assertNull(record.getField(null));
    }

    @Test
    void testMultipleFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("firstName", "John");
        fields.put("lastName", "Doe");
        fields.put("age", "25");
        fields.put("city", "New York");
        
        DataRecord record = new DataRecord(fields);
        
        assertEquals(4, record.getFields().size());
        assertEquals("John", record.getField("firstName"));
        assertEquals("Doe", record.getField("lastName"));
        assertEquals("25", record.getField("age"));
        assertEquals("New York", record.getField("city"));
    }

    @Test
    void testFieldsWithNullValues() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", null);
        fields.put("age", null);
        
        DataRecord record = new DataRecord(fields);
        
        assertEquals(2, record.getFields().size());
        assertNull(record.getField("name"));
        assertNull(record.getField("age"));
    }

    @Test
    void testFieldsWithEmptyValues() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "");
        fields.put("age", "");
        
        DataRecord record = new DataRecord(fields);
        
        assertEquals(2, record.getFields().size());
        assertEquals("", record.getField("name"));
        assertEquals("", record.getField("age"));
    }

    @Test
    void testDuplicateFieldNames() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Alice");
        fields.put("name", "Bob");
        
        DataRecord record = new DataRecord(fields);
        
        // Should return second value for duplicate field names (HashMap behavior)
        assertEquals("Bob", record.getField("name"));
    }

    @Test
    void testFieldNamesAreCaseSensitive() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("Name", "Alice");
        fields.put("name", "Bob");
        
        DataRecord record = new DataRecord(fields);
        
        assertEquals("Alice", record.getField("Name"));
        assertEquals("Bob", record.getField("name"));
    }

    @Test
    void testGetFieldAsString() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John");
        fields.put("age", 30);
        
        DataRecord record = new DataRecord(fields);
        
        assertEquals("John", record.getFieldAsString("name"));
        assertEquals("30", record.getFieldAsString("age"));
    }

    @Test
    void testGetFieldAsStringWithNullValue() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", null);
        
        DataRecord record = new DataRecord(fields);
        
        assertNull(record.getFieldAsString("name"));
    }

    @Test
    void testHasField() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John");
        fields.put("age", 30);
        
        DataRecord record = new DataRecord(fields);
        
        assertTrue(record.hasField("name"));
        assertTrue(record.hasField("age"));
        assertFalse(record.hasField("nonexistent"));
    }

    @Test
    void testHasFieldWithNullKey() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John");
        
        DataRecord record = new DataRecord(fields);
        
        assertFalse(record.hasField(null));
    }

    @Test
    void testSize() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John");
        fields.put("age", 30);
        fields.put("city", "Kyiv");
        
        DataRecord record = new DataRecord(fields);
        
        assertEquals(3, record.size());
    }

    @Test
    void testSizeWithEmptyRecord() {
        DataRecord record = new DataRecord(new HashMap<>());
        
        assertEquals(0, record.size());
    }

    @Test
    void testAddField() {
        DataRecord record = new DataRecord(new HashMap<>());
        
        record.addField("name", "John");
        record.addField("age", 30);
        
        assertEquals(2, record.size());
        assertEquals("John", record.getField("name"));
        assertEquals(30, record.getField("age"));
    }

    @Test
    void testAddFieldWithNullValue() {
        DataRecord record = new DataRecord(new HashMap<>());
        
        record.addField("name", null);
        
        assertEquals(1, record.size());
        assertNull(record.getField("name"));
    }

    @Test
    void testAddFieldOverwritesExisting() {
        DataRecord record = new DataRecord(new HashMap<>());
        
        record.addField("name", "John");
        record.addField("name", "Jane");
        
        assertEquals(1, record.size());
        assertEquals("Jane", record.getField("name"));
    }

    @Test
    void testEqualsSameObject() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John");
        DataRecord record = new DataRecord(fields);
        
        assertEquals(record, record);
    }

    @Test
    void testEqualsEqualObjects() {
        Map<String, Object> fields1 = new HashMap<>();
        fields1.put("name", "John");
        DataRecord record1 = new DataRecord(fields1);
        
        Map<String, Object> fields2 = new HashMap<>();
        fields2.put("name", "John");
        DataRecord record2 = new DataRecord(fields2);
        
        assertEquals(record1, record2);
    }

    @Test
    void testEqualsDifferentObjects() {
        Map<String, Object> fields1 = new HashMap<>();
        fields1.put("name", "John");
        DataRecord record1 = new DataRecord(fields1);
        
        Map<String, Object> fields2 = new HashMap<>();
        fields2.put("name", "Jane");
        DataRecord record2 = new DataRecord(fields2);
        
        assertNotEquals(record1, record2);
    }

    @Test
    void testEqualsWithNull() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John");
        DataRecord record = new DataRecord(fields);
        
        assertNotEquals(record, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John");
        DataRecord record = new DataRecord(fields);
        
        assertNotEquals(record, "John");
    }

    @Test
    void testHashCodeConsistency() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John");
        DataRecord record = new DataRecord(fields);
        
        int hashCode1 = record.hashCode();
        int hashCode2 = record.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeEqualObjects() {
        Map<String, Object> fields1 = new HashMap<>();
        fields1.put("name", "John");
        DataRecord record1 = new DataRecord(fields1);
        
        Map<String, Object> fields2 = new HashMap<>();
        fields2.put("name", "John");
        DataRecord record2 = new DataRecord(fields2);
        
        assertEquals(record1.hashCode(), record2.hashCode());
    }

    @Test
    void testHashCodeDifferentObjects() {
        Map<String, Object> fields1 = new HashMap<>();
        fields1.put("name", "John");
        DataRecord record1 = new DataRecord(fields1);
        
        Map<String, Object> fields2 = new HashMap<>();
        fields2.put("name", "Jane");
        DataRecord record2 = new DataRecord(fields2);
        
        assertNotEquals(record1.hashCode(), record2.hashCode());
    }

    @Test
    void testToString() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John");
        fields.put("age", 30);
        DataRecord record = new DataRecord(fields);
        
        String result = record.toString();
        
        assertTrue(result.contains("DataRecord{"));
        assertTrue(result.contains("fields="));
    }

    @Test
    void testToStringWithEmptyRecord() {
        DataRecord record = new DataRecord(new HashMap<>());
        
        String result = record.toString();
        
        assertTrue(result.contains("DataRecord{"));
        assertTrue(result.contains("fields={}"));
    }

    @Test
    void testGetFieldReturnsObject() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "John");
        fields.put("age", 30);
        fields.put("active", true);
        
        DataRecord record = new DataRecord(fields);
        
        assertEquals("John", record.getField("name"));
        assertEquals(30, record.getField("age"));
        assertEquals(true, record.getField("active"));
    }

    @Test
    void testGetFieldAsStringConvertsToString() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("age", 30);
        
        DataRecord record = new DataRecord(fields);
        
        assertEquals("30", record.getFieldAsString("age"));
    }

    @Test
    void testGetFieldAsStringWithNullReturnsNull() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", null);
        
        DataRecord record = new DataRecord(fields);
        
        assertNull(record.getFieldAsString("name"));
    }

    @Test
    void testGetFieldsReturnsCopy() {
        Map<String, Object> originalFields = new HashMap<>();
        originalFields.put("name", "John");
        
        DataRecord record = new DataRecord(originalFields);
        
        Map<String, Object> returnedFields = record.getFields();
        
        // Modify returned map
        returnedFields.put("age", 30);
        
        // Original should not be affected
        assertFalse(originalFields.containsKey("age"));
    }

    @Test
    void testConstructorDefensiveCopy() {
        Map<String, Object> originalFields = new HashMap<>();
        originalFields.put("name", "John");
        
        DataRecord record = new DataRecord(originalFields);
        
        // Modify original map
        originalFields.put("age", 30);
        
        // Record should not be affected
        assertFalse(record.hasField("age"));
        assertEquals(1, record.size());
    }
}
