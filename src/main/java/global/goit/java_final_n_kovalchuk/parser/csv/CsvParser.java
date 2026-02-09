package global.goit.java_final_n_kovalchuk.parser.csv;

import com.opencsv.CSVReader;
import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import global.goit.java_final_n_kovalchuk.parser.FileParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for CSV files.
 * Reads CSV files with headers and maps each row to a DataRecord.
 * Uses OpenCSV for parsing with support for quoted values and special characters.
 */
public class CsvParser implements FileParser<DataRecord> {

    /**
     * Constructs a new CsvParser.
     */
    public CsvParser() {
    }

    /**
     * Parses a CSV file and returns a list of DataRecords.
     * The first row is treated as headers, and subsequent rows are data.
     *
     * @param file the CSV file to parse
     * @return a list of DataRecords parsed from the file
     * @throws FileConversionException if parsing fails
     */
    @Override
    public List<DataRecord> parse(File file) throws FileConversionException {
        if (file == null) {
            throw new FileConversionException("File cannot be null");
        }

        if (!file.exists()) {
            throw new FileConversionException("File does not exist: " + file.getAbsolutePath());
        }

        if (!file.canRead()) {
            throw new FileConversionException("File cannot be read: " + file.getAbsolutePath());
        }

        try (Reader reader = new FileReader(file);
             CSVReader csvReader = new CSVReader(reader)) {

            // Read header row
            String[] headers = csvReader.readNext();

            if (headers == null) {
                throw new FileConversionException("CSV file is empty: " + file.getAbsolutePath());
            }

            List<DataRecord> records = new ArrayList<>();

            // Read data rows
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                DataRecord record = new DataRecord();

                for (int i = 0; i < headers.length; i++) {
                    String header = headers[i].trim();
                    String value = (i < row.length) ? row[i] : "";

                    // Handle empty values
                    if (value.isEmpty()) {
                        record.addField(header, null);
                    } else {
                        record.addField(header, value);
                    }
                }

                records.add(record);
            }

            return records;
        } catch (IOException e) {
            throw new FileConversionException(
                "Failed to parse CSV file: " + file.getAbsolutePath(),
                e
            );
        } catch (com.opencsv.exceptions.CsvValidationException e) {
            throw new FileConversionException(
                "Invalid CSV format in file: " + file.getAbsolutePath(),
                e
            );
        }
    }
}
