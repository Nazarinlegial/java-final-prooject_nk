package global.goit.java_final_n_kovalchuk.cli;

import global.goit.java_final_n_kovalchuk.exception.InvalidInputException;

/**
 * Parser for command-line arguments.
 * Parses and validates --input, --output, and --csv-mapping flags.
 */
public class CommandLineParser {

    private static final String INPUT_FLAG = "--input";
    private static final String OUTPUT_FLAG = "--output";
    private static final String CSV_MAPPING_FLAG = "--csv-mapping";

    /**
     * Parses command-line arguments and returns a CommandLineArgs object.
     *
     * @param args command-line arguments
     * @return a CommandLineArgs object containing parsed arguments
     * @throws InvalidInputException if arguments are invalid
     */
    public CommandLineArgs parse(String[] args) throws InvalidInputException {
        if (args == null || args.length == 0) {
            throw new InvalidInputException(
                    "No arguments provided. Usage: --input <input-file> --output <output-file> [--csv-mapping]"
            );
        }

        String inputFile = null;
        String outputFile = null;
        boolean csvMapping = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (INPUT_FLAG.equals(arg)) {
                if (i + 1 >= args.length) {
                    throw new InvalidInputException(
                            "Missing value for --input flag. Usage: --input <input-file>"
                    );
                }
                inputFile = args[i + 1];
                i++; // Skip next argument as it's the value
            } else if (OUTPUT_FLAG.equals(arg)) {
                if (i + 1 >= args.length) {
                    throw new InvalidInputException(
                            "Missing value for --output flag. Usage: --output <output-file>"
                    );
                }
                outputFile = args[i + 1];
                i++; // Skip next argument as it's the value
            } else if (CSV_MAPPING_FLAG.equals(arg)) {
                csvMapping = true;
            }
        }

        if (inputFile == null) {
            throw new InvalidInputException(
                    "Missing --input flag. Usage: --input <input-file> --output <output-file> [--csv-mapping]"
            );
        }

        if (outputFile == null) {
            throw new InvalidInputException(
                    "Missing --output flag. Usage: --input <input-file> --output <output-file> [--csv-mapping]"
            );
        }

        return new CommandLineArgs(inputFile, outputFile, csvMapping);
    }
}
