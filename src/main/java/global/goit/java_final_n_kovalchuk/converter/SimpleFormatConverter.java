package global.goit.java_final_n_kovalchuk.converter;

import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.exception.InvalidInputException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import global.goit.java_final_n_kovalchuk.parser.FileParser;
import global.goit.java_final_n_kovalchuk.parser.csv.CsvParser;
import global.goit.java_final_n_kovalchuk.parser.json.JsonParser;
import global.goit.java_final_n_kovalchuk.parser.xml.JacksonXmlParser;
import global.goit.java_final_n_kovalchuk.validator.FormatDetector;
import global.goit.java_final_n_kovalchuk.writer.FileWriter;
import global.goit.java_final_n_kovalchuk.writer.csv.CsvWriter;
import global.goit.java_final_n_kovalchuk.writer.json.JsonWriter;
import global.goit.java_final_n_kovalchuk.writer.xml.XmlWriter;

import java.io.File;
import java.util.List;

/**
 * Simple implementation of FormatConverter interface.
 * This class handles file format conversions between JSON, XML, and CSV formats.
 * 
 * The conversion process is straightforward:
 * 1. Detect input and output formats
 * 2. Parse the input file using the appropriate parser
 * 3. Write the data to the output file using the appropriate writer
 */
public class SimpleFormatConverter implements FormatConverter {

    /**
     * Converts a file from one format to another.
     * 
     * This method automatically detects the input and output formats based on
     * file extensions, then performs the conversion using the appropriate
     * parser and writer.
     *
     * @param inputFile  the input file to convert
     * @param outputFile the output file to write the converted data to
     * @throws FileConversionException if conversion fails
     */
    @Override
    public void convert(File inputFile, File outputFile) throws FileConversionException {
        try {
            // Detect input and output formats
            FormatDetector.FileFormat inputFormat = FormatDetector.detectFormat(inputFile.getPath());
            FormatDetector.FileFormat outputFormat = FormatDetector.detectFormat(outputFile.getPath());

            // Create parser based on input format
            FileParser<DataRecord> parser = createParser(inputFormat);

            // Parse input file
            List<DataRecord> records = parser.parse(inputFile);

            // Create writer based on output format
            FileWriter<DataRecord> writer = createWriter(outputFormat);

            // Write to output file
            writer.write(records, outputFile);

            // Print success message
            System.out.println("Conversion successful: " + inputFile.getName() + " -> " + outputFile.getName());
            System.out.println("Converted " + records.size() + " record(s)");

        } catch (InvalidInputException e) {
            throw new FileConversionException("Format detection failed: " + e.getMessage(), e);
        }
    }

    /**
     * Creates the appropriate parser based on the file format.
     * 
     * @param format the file format
     * @return a parser for the specified format
     * @throws FileConversionException if the format is not supported
     */
    private FileParser<DataRecord> createParser(FormatDetector.FileFormat format) throws FileConversionException {
        switch (format) {
            case JSON:
                return new JsonParser();
            case CSV:
                return new CsvParser();
            case XML:
                return new JacksonXmlParser();
            default:
                throw new FileConversionException("Unsupported input format: " + format);
        }
    }

    /**
     * Creates the appropriate writer based on the file format.
     * 
     * @param format the file format
     * @return a writer for the specified format
     * @throws FileConversionException if the format is not supported
     */
    private FileWriter<DataRecord> createWriter(FormatDetector.FileFormat format) throws FileConversionException {
        switch (format) {
            case JSON:
                return new JsonWriter();
            case CSV:
                return new CsvWriter();
            case XML:
                return new XmlWriter();
            default:
                throw new FileConversionException("Unsupported output format: " + format);
        }
    }
}
