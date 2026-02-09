package global.goit.java_final_n_kovalchuk.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for DataField class.
 * Tests all methods including constructors, getters, setters, equals, hashCode, and toString.
 */
@DisplayName("DataField Unit Tests")
class DataFieldTest {

    @Test
    @DisplayName("Constructor should create DataField with name and value")
    void testConstructorWithNameAndValue() {
        // Arrange & Act
        DataField field = new DataField("name", "John");

        // Assert
        assertEquals("name", field.getName());
        assertEquals("John", field.getValue());
    }

    @Test
    @DisplayName("Constructor should handle null value")
    void testConstructorWithNullValue() {
        // Arrange & Act
        DataField field = new DataField("age", null);

        // Assert
        assertEquals("age", field.getName());
        assertNull(field.getValue());
    }

    @Test
    @DisplayName("Constructor should handle null name")
    void testConstructorWithNullName() {
        // Arrange & Act
        DataField field = new DataField(null, "value");

        // Assert
        assertNull(field.getName());
        assertEquals("value", field.getValue());
    }

    @Test
    @DisplayName("Constructor should handle both null name and value")
    void testConstructorWithBothNull() {
        // Arrange & Act
        DataField field = new DataField(null, null);

        // Assert
        assertNull(field.getName());
        assertNull(field.getValue());
    }

    @Test
    @DisplayName("Constructor should handle different value types")
    void testConstructorWithDifferentValueTypes() {
        // Arrange & Act
        DataField stringField = new DataField("name", "Alice");
        DataField intField = new DataField("age", 30);
        DataField doubleField = new DataField("price", 19.99);
        DataField booleanField = new DataField("active", true);

        // Assert
        assertEquals("Alice", stringField.getValue());
        assertEquals(30, intField.getValue());
        assertEquals(19.99, doubleField.getValue());
        assertEquals(true, booleanField.getValue());
    }

    @Test
    @DisplayName("Constructor should handle empty strings")
    void testConstructorWithEmptyStrings() {
        // Arrange & Act
        DataField field = new DataField("", "");

        // Assert
        assertEquals("", field.getName());
        assertEquals("", field.getValue());
    }

    @Test
    @DisplayName("Getter should return correct name")
    void testGetName() {
        // Arrange
        DataField field = new DataField("username", "john_doe");

        // Act
        String name = field.getName();

        // Assert
        assertEquals("username", name);
    }

    @Test
    @DisplayName("Setter should update name")
    void testSetName() {
        // Arrange
        DataField field = new DataField("oldName", "value");

        // Act
        field.setName("newName");

        // Assert
        assertEquals("newName", field.getName());
    }

    @Test
    @DisplayName("Setter should set name to null")
    void testSetNameToNull() {
        // Arrange
        DataField field = new DataField("name", "value");

        // Act
        field.setName(null);

        // Assert
        assertNull(field.getName());
    }

    @Test
    @DisplayName("Getter should return correct value")
    void testGetValue() {
        // Arrange
        DataField field = new DataField("email", "test@example.com");

        // Act
        Object value = field.getValue();

        // Assert
        assertEquals("test@example.com", value);
    }

    @Test
    @DisplayName("Setter should update value")
    void testSetValue() {
        // Arrange
        DataField field = new DataField("age", 25);

        // Act
        field.setValue(26);

        // Assert
        assertEquals(26, field.getValue());
    }

    @Test
    @DisplayName("Setter should set value to null")
    void testSetValueToNull() {
        // Arrange
        DataField field = new DataField("name", "Alice");

        // Act
        field.setValue(null);

        // Assert
        assertNull(field.getValue());
    }

    @Test
    @DisplayName("getValueAsString should return string representation of value")
    void testGetValueAsString() {
        // Arrange
        DataField stringField = new DataField("name", "John");
        DataField intField = new DataField("age", 30);
        DataField doubleField = new DataField("price", 19.99);
        DataField booleanField = new DataField("active", true);

        // Act
        String stringValue = stringField.getValueAsString();
        String intValue = intField.getValueAsString();
        String doubleValue = doubleField.getValueAsString();
        String booleanValue = booleanField.getValueAsString();

        // Assert
        assertEquals("John", stringValue);
        assertEquals("30", intValue);
        assertEquals("19.99", doubleValue);
        assertEquals("true", booleanValue);
    }

    @Test
    @DisplayName("getValueAsString should return null when value is null")
    void testGetValueAsStringWithNullValue() {
        // Arrange
        DataField field = new DataField("name", null);

        // Act
        String valueAsString = field.getValueAsString();

        // Assert
        assertNull(valueAsString);
    }

    @Test
    @DisplayName("equals should return true for same object")
    void testEqualsSameObject() {
        // Arrange
        DataField field1 = new DataField("name", "John");
        DataField field2 = new DataField("name", "John");

        // Act & Assert
        assertEquals(field1, field2);
    }

    @Test
    @DisplayName("equals should return false for null")
    void testEqualsNull() {
        // Arrange
        DataField field = new DataField("name", "John");

        // Act & Assert
        assertNotEquals(field, null);
    }

    @Test
    @DisplayName("equals should return false for different class")
    void testEqualsDifferentClass() {
        // Arrange
        DataField field = new DataField("name", "John");
        String other = "John";

        // Act & Assert
        assertNotEquals(field, other);
    }

    @Test
    @DisplayName("equals should be symmetric")
    void testEqualsSymmetric() {
        // Arrange
        DataField field1 = new DataField("name", "John");
        DataField field2 = new DataField("name", "John");

        // Act & Assert
        assertEquals(field1.equals(field2), field2.equals(field1));
    }

    @Test
    @DisplayName("equals should be transitive")
    void testEqualsTransitive() {
        // Arrange
        DataField field1 = new DataField("name", "John");
        DataField field2 = new DataField("name", "John");
        DataField field3 = new DataField("name", "John");

        // Act & Assert
        assertEquals(field1, field2);
        assertEquals(field2, field3);
        assertEquals(field1, field3);
    }

    @Test
    @DisplayName("hashCode should be consistent")
    void testHashCodeConsistency() {
        // Arrange
        DataField field = new DataField("name", "John");

        // Act
        int hashCode1 = field.hashCode();
        int hashCode2 = field.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("hashCode should be equal for equal objects")
    void testHashCodeEqualObjects() {
        // Arrange
        DataField field1 = new DataField("name", "John");
        DataField field2 = new DataField("name", "John");

        // Act & Assert
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    @DisplayName("hashCode should be different for different objects")
    void testHashCodeDifferentObjects() {
        // Arrange
        DataField field1 = new DataField("name", "John");
        DataField field2 = new DataField("name", "Jane");

        // Act & Assert
        assertNotEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    @DisplayName("toString should return formatted string representation")
    void testToString() {
        // Arrange
        DataField field = new DataField("name", "John");

        // Act
        String result = field.toString();

        // Assert
        assertEquals("DataField{name='name', value=John}", result);
    }

    @Test
    @DisplayName("toString should handle null value")
    void testToStringWithNullValue() {
        // Arrange
        DataField field = new DataField("name", null);

        // Act
        String result = field.toString();

        // Assert
        assertEquals("DataField{name='name', value=null}", result);
    }

    @Test
    @DisplayName("toString should handle null name")
    void testToStringWithNullName() {
        // Arrange
        DataField field = new DataField(null, "John");

        // Act
        String result = field.toString();

        // Assert
        assertEquals("DataField{name='null', value=John}", result);
    }

    @Test
    @DisplayName("toString should handle both null")
    void testToStringWithBothNull() {
        // Arrange
        DataField field = new DataField(null, null);

        // Act
        String result = field.toString();

        // Assert
        assertEquals("DataField{name='null', value=null}", result);
    }

    @Test
    @DisplayName("toString should handle special characters in value")
    void testToStringWithSpecialCharacters() {
        // Arrange
        DataField field = new DataField("description", "A person with \"quotes\" and, commas");

        // Act
        String result = field.toString();

        // Assert
        assertEquals("DataField{name='description', value=A person with \"quotes\" and, commas}", result);
    }

    @Test
    @DisplayName("toString should handle numeric values")
    void testToStringWithNumericValue() {
        // Arrange
        DataField field = new DataField("price", 19.99);

        // Act
        String result = field.toString();

        // Assert
        assertEquals("DataField{name='price', value=19.99}", result);
    }

    @Test
    @DisplayName("toString should handle boolean values")
    void testToStringWithBooleanValue() {
        // Arrange
        DataField field = new DataField("active", true);

        // Act
        String result = field.toString();

        // Assert
        assertEquals("DataField{name='active', value=true}", result);
    }

    @Test
    @DisplayName("equals and hashCode should work together")
    void testEqualsAndHashCodeContract() {
        // Arrange
        DataField field1 = new DataField("name", "John");
        DataField field2 = new DataField("name", "John");
        DataField field3 = new DataField("name", "Jane");

        // Act & Assert
        assertEquals(field1, field2);
        assertEquals(field1.hashCode(), field2.hashCode());
        assertNotEquals(field1, field3);
        assertNotEquals(field1.hashCode(), field3.hashCode());
    }

    @Test
    @DisplayName("Constructor should handle very long strings")
    void testConstructorWithLongStrings() {
        // Arrange
        String longName = "a".repeat(1000);
        String longValue = "b".repeat(1000);

        // Act
        DataField field = new DataField(longName, longValue);

        // Assert
        assertEquals(longName, field.getName());
        assertEquals(longValue, field.getValue());
    }

    @Test
    @DisplayName("Constructor should handle unicode characters")
    void testConstructorWithUnicode() {
        // Arrange
        DataField field = new DataField("name", "Привіт 世界 مرحبا");

        // Act
        String value = field.getValueAsString();

        // Assert
        assertEquals("Привіт 世界 مرحبا", value);
    }

    @Test
    @DisplayName("getValueAsString should handle object with custom toString")
    void testGetValueAsStringWithCustomObject() {
        // Arrange
        CustomObject customObj = new CustomObject("custom_value");

        // Act
        DataField field = new DataField("custom", customObj);

        // Assert
        assertEquals("custom_value", field.getValueAsString());
    }

    @Test
    @DisplayName("equals should handle objects with same name but different value types")
    void testEqualsDifferentValueTypes() {
        // Arrange
        DataField field1 = new DataField("age", "30");
        DataField field2 = new DataField("age", 30);

        // Act & Assert
        assertNotEquals(field1, field2);
    }

    /**
     * Helper class for testing getValueAsString with custom objects
     */
    private static class CustomObject {
        private final String value;

        public CustomObject(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
