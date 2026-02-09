package global.goit.java_final_n_kovalchuk.writer.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import global.goit.java_final_n_kovalchuk.writer.FileWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Writer for JSON files.
 * Handles both single records and arrays of records.
 * Uses Jackson ObjectMapper for writing with pretty printing enabled.
 */
public class JsonWriter implements FileWriter<DataRecord> {

    private final ObjectMapper objectMapper;

    /**
     * Constructs a new JsonWriter with a default ObjectMapper configured for pretty printing.
     */
    public JsonWriter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Constructs a new JsonWriter with a custom ObjectMapper.
     *
     * @param objectMapper the ObjectMapper to use for writing
     */
    public JsonWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Writes a list of DataRecords to a JSON file.
     * If the list contains a single record, it writes a JSON object.
     * If the list contains multiple records, it writes a JSON array.
     *
     * @param records the list of DataRecords to write
     * @param file    the file to write to
     * @throws FileConversionException if writing fails
     */
    @Override
    public void write(List<DataRecord> records, File file) throws FileConversionException {
        if (records == null) {
            throw new FileConversionException("Records list cannot be null");
        }

        if (file == null) {
            throw new FileConversionException("File cannot be null");
        }

        try {
            // Ensure parent directory exists
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new FileConversionException(
                        "Failed to create parent directory: " + parentDir.getAbsolutePath()
                    );
                }
            }

            // Convert DataRecords to a format suitable for JSON serialization
            Object output;
            if (records.size() == 1) {
                output = convertDataRecordToMap(records.get(0));
            } else {
                output = records.stream()
                    .map(this::convertDataRecordToMap)
                    .toArray();
            }

            // Write to file
            objectMapper.writeValue(file, output);
        } catch (IOException e) {
            throw new FileConversionException(
                "Failed to write JSON file: " + file.getAbsolutePath(),
                e
            );
        }
    }

    /**
     * Converts a DataRecord to a Map for JSON serialization.
     *
     * @param record the DataRecord to convert
     * @return a Map representation of the DataRecord
     */
    private Map<String, Object> convertDataRecordToMap(DataRecord record) {
        return record.getFields();
    }
}
