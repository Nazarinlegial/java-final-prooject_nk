package global.goit.java_final_n_kovalchuk.writer.csv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import global.goit.java_final_n_kovalchuk.writer.FileWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Writer for CSV files.
 * Writes a list of DataRecords to CSV format with optional headers.
 * Uses OpenCSV for writing with support for special characters and quoting.
 * Handles nested data structures (Map and List) by serializing them as JSON.
 */
public class CsvWriter implements FileWriter<DataRecord> {

    private final ObjectMapper objectMapper;
    private final boolean writeHeaders;

    /**
     * Constructs a new CsvWriter with headers enabled by default.
     */
    public CsvWriter() {
        this(true);
    }

    /**
     * Constructs a new CsvWriter with specified header writing behavior.
     *
     * @param writeHeaders whether to write CSV headers (true = with headers, false = without headers)
     */
    public CsvWriter(boolean writeHeaders) {
        this.objectMapper = new ObjectMapper();
        this.writeHeaders = writeHeaders;
    }

    /**
     * Writes a list of DataRecords to a CSV file.
     * Optionally writes headers (field names) in the first row based on writeHeaders flag.
     * Field names are extracted from the first record or from all records to ensure all columns are included.
     *
     * @param records list of DataRecords to write
     * @param file    file to write to
     * @throws FileConversionException if writing fails
     */
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

            // Collect all unique field names from all records
            List<String> headers = collectAllFieldNames(records);

            try (java.io.FileWriter fileWriter = new java.io.FileWriter(file);
                 CSVWriter csvWriter = new CSVWriter(fileWriter)) {

                // Write header row if writeHeaders is enabled
                if (writeHeaders) {
                    String[] headerArray = headers.toArray(new String[0]);
                    csvWriter.writeNext(headerArray);
                }

                // Write data rows
                for (DataRecord record : records) {
                    String[] row = new String[headers.size()];
                    Map<String, Object> fields = record.getFields();

                    for (int i = 0; i < headers.size(); i++) {
                        String header = headers.get(i);
                        Object value = fields.get(header);
                        row[i] = formatValue(value);
                    }

                    csvWriter.writeNext(row);
                }
            }
        } catch (IOException e) {
            throw new FileConversionException(
                "Failed to write CSV file: " + file.getAbsolutePath(),
                e
            );
        }
    }

    /**
     * Formats a value for CSV output.
     * For nested structures (Map and List), serializes them as JSON.
     * For primitive values, returns their string representation.
     *
     * @param value value to format
     * @return formatted string representation
     */
    private String formatValue(Object value) {
        if (value == null) {
            return "";
        }

        // Check if value is a nested structure (Map or List)
        if (value instanceof Map || value instanceof List) {
            try {
                return objectMapper.writeValueAsString(value);
            } catch (Exception e) {
                // Fallback to toString() if JSON serialization fails
                return value.toString();
            }
        }

        // For primitive values, return their string representation
        return value.toString();
    }

    /**
     * Collects all unique field names from all records.
     * Uses TreeSet to maintain alphabetical order.
     *
     * @param records list of DataRecords
     * @return a list of all unique field names in alphabetical order
     */
    private List<String> collectAllFieldNames(List<DataRecord> records) {
        TreeSet<String> fieldNames = new TreeSet<>();

        for (DataRecord record : records) {
            Map<String, Object> fields = record.getFields();
            fieldNames.addAll(fields.keySet());
        }

        return new ArrayList<>(fieldNames);
    }
}
