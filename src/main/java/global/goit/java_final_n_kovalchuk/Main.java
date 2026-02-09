package global.goit.java_final_n_kovalchuk;

import global.goit.java_final_n_kovalchuk.cli.CommandLineArgs;
import global.goit.java_final_n_kovalchuk.cli.CommandLineParser;
import global.goit.java_final_n_kovalchuk.converter.SimpleFormatConverter;
import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.validator.FileValidator;

import java.io.File;

/**
 * Main entry point for CLI File Converter application.
 * Supports conversion between JSON, XML and CSV file formats.
 *
 * Stage 3: Full conversion functionality implemented using SimpleFormatConverter.
 */
public class Main {

    /**
     * Main method that parses command-line arguments and initiates file conversion.
     *
     * @param args command-line arguments: --input <input-file> --output <output-file> [--csv-mapping]
     */
    public static void main(String[] args) {
        try {
            // Parse command-line arguments
            CommandLineParser parser = new CommandLineParser();
            CommandLineArgs commandLineArgs = parser.parse(args);

            String inputFile = commandLineArgs.getInputFile();
            String outputFile = commandLineArgs.getOutputFile();
            boolean csvMapping = commandLineArgs.isCsvMapping();

            // Validate input file
            FileValidator.validateFile(inputFile);

            System.out.println("CLI File Converter - Stage 3");
            System.out.println("==============================");
            System.out.println("Input file: " + inputFile);
            System.out.println("Output file: " + outputFile);
            if (csvMapping) {
                System.out.println("CSV mapping: enabled (no headers)");
            }

            System.out.println();

            // Create converter and perform conversion
            SimpleFormatConverter converter = new SimpleFormatConverter();
            File input = new File(inputFile);
            File output = new File(outputFile);

            converter.convert(input, output, csvMapping);

            System.out.println();
            System.out.println("Conversion completed successfully!");

        } catch (FileConversionException e) {
            // Print clear error message without stack trace
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
