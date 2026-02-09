package global.goit.java_final_n_kovalchuk.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a data record containing key-value pairs.
 * This class is used to store data during file format conversion operations.
 * Each record can hold arbitrary fields with string keys and object values.
 */
public class DataRecord {

    private final Map<String, Object> fields;

    /**
     * Constructs a new empty DataRecord.
     */
    public DataRecord() {
        this.fields = new HashMap<>();
    }

    /**
     * Constructs a new DataRecord with the specified fields map.
     *
     * @param fields the map containing the record fields
     */
    public DataRecord(Map<String, Object> fields) {
        this.fields = new HashMap<>(fields);
    }

    /**
     * Gets the fields map for this record.
     *
     * @return the fields map
     */
    public Map<String, Object> getFields() {
        return new HashMap<>(fields);
    }

    /**
     * Adds a field to this record.
     *
     * @param key   the field name
     * @param value the field value
     */
    public void addField(String key, Object value) {
        this.fields.put(key, value);
    }

    /**
     * Gets a value from the fields map by key.
     *
     * @param key the key to look up
     * @return the value associated with the key, or null if not found
     */
    public Object getField(String key) {
        return fields.get(key);
    }

    /**
     * Gets a value from the fields map by key, converted to a String.
     *
     * @param key the key to look up
     * @return the value as a String, or null if not found
     */
    public String getFieldAsString(String key) {
        Object value = fields.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * Checks if this record contains a specific field.
     *
     * @param key the key to check
     * @return true if the field exists, false otherwise
     */
    public boolean hasField(String key) {
        return fields.containsKey(key);
    }

    /**
     * Gets the number of fields in this record.
     *
     * @return the number of fields
     */
    public int size() {
        return fields.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataRecord that = (DataRecord) o;
        return Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fields);
    }

    @Override
    public String toString() {
        return "DataRecord{" +
                "fields=" + fields +
                '}';
    }
}
