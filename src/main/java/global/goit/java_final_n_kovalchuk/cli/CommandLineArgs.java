package global.goit.java_final_n_kovalchuk.cli;

/**
 * Data class representing parsed command-line arguments.
 * Contains input and output file paths provided by the user.
 */
public class CommandLineArgs {

    private final String inputFile;
    private final String outputFile;

    /**
     * Constructs a new CommandLineArgs with the specified input and output file paths.
     *
     * @param inputFile  the path to the input file
     * @param outputFile the path to the output file
     */
    public CommandLineArgs(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    /**
     * Gets the input file path.
     *
     * @return the input file path
     */
    public String getInputFile() {
        return inputFile;
    }

    /**
     * Gets the output file path.
     *
     * @return the output file path
     */
    public String getOutputFile() {
        return outputFile;
    }

    /**
     * Validates the command-line arguments.
     * Checks that both input and output file paths are not null or empty.
     *
     * @throws IllegalArgumentException if any argument is invalid
     */
    public void validate() {
        if (inputFile == null || inputFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Input file path cannot be null or empty");
        }
        if (outputFile == null || outputFile.trim().isEmpty()) {
            throw new IllegalArgumentException("Output file path cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        return "CommandLineArgs{" +
                "inputFile='" + inputFile + '\'' +
                ", outputFile='" + outputFile + '\'' +
                '}';
    }
}
