package global.goit.java_final_n_kovalchuk.model;

import java.util.Objects;

/**
 * Represents a single data field with a name and value.
 * This class is used to represent individual fields within a DataRecord.
 */
public class DataField {

    private String name;
    private Object value;

    /**
     * Constructs a new DataField with the specified name and value.
     *
     * @param name  the field name
     * @param value the field value
     */
    public DataField(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the field name.
     *
     * @return the field name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the field name.
     *
     * @param name the new field name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the field value.
     *
     * @return the field value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the field value.
     *
     * @param value the new field value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Gets the field value as a String.
     *
     * @return the field value as a String, or null if the value is null
     */
    public String getValueAsString() {
        return value != null ? value.toString() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataField dataField = (DataField) o;
        return Objects.equals(name, dataField.name) && Objects.equals(value, dataField.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "DataField{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
