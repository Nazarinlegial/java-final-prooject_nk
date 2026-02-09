package global.goit.java_final_n_kovalchuk.cli;

import global.goit.java_final_n_kovalchuk.exception.InvalidInputException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CommandLineParser class.
 */
class CommandLineParserTest {

    private final CommandLineParser parser = new CommandLineParser();

    @Test
    void testParseValidArguments() throws InvalidInputException {
        String[] args = {"--input", "input.json", "--output", "output.xml"};
        
        CommandLineArgs result = parser.parse(args);
        
        assertEquals("input.json", result.getInputFile());
        assertEquals("output.xml", result.getOutputFile());
    }

    @Test
    void testParseValidArgumentsReversedOrder() throws InvalidInputException {
        String[] args = {"--output", "output.xml", "--input", "input.json"};
        
        CommandLineArgs result = parser.parse(args);
        
        assertEquals("input.json", result.getInputFile());
        assertEquals("output.xml", result.getOutputFile());
    }

    @Test
    void testParseNullArguments() {
        String[] args = null;
        
        InvalidInputException exception = assertThrows(
            InvalidInputException.class,
            () -> parser.parse(args)
        );
        
        assertEquals("No arguments provided. Usage: --input <input-file> --output <output-file> [--csv-mapping]", 
                     exception.getMessage());
    }

    @Test
    void testParseEmptyArguments() {
        String[] args = {};
        
        InvalidInputException exception = assertThrows(
            InvalidInputException.class,
            () -> parser.parse(args)
        );
        
        assertEquals("No arguments provided. Usage: --input <input-file> --output <output-file> [--csv-mapping]", 
                     exception.getMessage());
    }

    @Test
    void testParseMissingInputFlag() {
        String[] args = {"--output", "output.xml"};
        
        InvalidInputException exception = assertThrows(
            InvalidInputException.class,
            () -> parser.parse(args)
        );
        
        assertEquals("Missing --input flag. Usage: --input <input-file> --output <output-file> [--csv-mapping]",
                     exception.getMessage());
    }

    @Test
    void testParseMissingOutputFlag() {
        String[] args = {"--input", "input.json"};
        
        InvalidInputException exception = assertThrows(
            InvalidInputException.class,
            () -> parser.parse(args)
        );
        
        assertEquals("Missing --output flag. Usage: --input <input-file> --output <output-file> [--csv-mapping]",
                     exception.getMessage());
    }

    @Test
    void testParseMissingInputValue() {
        String[] args = {"--input", "--output"};
        
        InvalidInputException exception = assertThrows(
            InvalidInputException.class,
            () -> parser.parse(args)
        );
        
        assertEquals("Missing --output flag. Usage: --input <input-file> --output <output-file> [--csv-mapping]",
                     exception.getMessage());
    }

    @Test
    void testParseMissingOutputValue() {
        String[] args = {"--input", "input.json", "--output"};
        
        InvalidInputException exception = assertThrows(
            InvalidInputException.class,
            () -> parser.parse(args)
        );
        
        assertEquals("Missing value for --output flag. Usage: --output <output-file>", 
                     exception.getMessage());
    }

    @Test
    void testParseInputValueAtEnd() {
        String[] args = {"--output", "output.xml", "--input"};
        
        InvalidInputException exception = assertThrows(
            InvalidInputException.class,
            () -> parser.parse(args)
        );
        
        assertEquals("Missing value for --input flag. Usage: --input <input-file>", 
                     exception.getMessage());
    }

    @Test
    void testParseInputValueIsFlag() {
        String[] args = {"--input", "--output", "output.xml"};
        
        // Parser treats --output as the input file value, then misses --output flag
        InvalidInputException exception = assertThrows(
            InvalidInputException.class,
            () -> parser.parse(args)
        );
        
        assertEquals("Missing --output flag. Usage: --input <input-file> --output <output-file> [--csv-mapping]",
                     exception.getMessage());
    }

    @Test
    void testParseWithExtraArguments() throws InvalidInputException {
        String[] args = {"--input", "input.json", "--output", "output.xml", "--extra", "value"};
        
        CommandLineArgs result = parser.parse(args);
        
        assertEquals("input.json", result.getInputFile());
        assertEquals("output.xml", result.getOutputFile());
    }

    @Test
    void testParseWithFilePathsContainingSpaces() throws InvalidInputException {
        String[] args = {"--input", "path/to/input file.json", "--output", "path/to/output file.xml"};
        
        CommandLineArgs result = parser.parse(args);
        
        assertEquals("path/to/input file.json", result.getInputFile());
        assertEquals("path/to/output file.xml", result.getOutputFile());
    }

    @Test
    void testParseWithRelativePaths() throws InvalidInputException {
        String[] args = {"--input", "../data/input.json", "--output", "./output/result.xml"};
        
        CommandLineArgs result = parser.parse(args);
        
        assertEquals("../data/input.json", result.getInputFile());
        assertEquals("./output/result.xml", result.getOutputFile());
    }

    @Test
    void testParseWithAbsolutePaths() throws InvalidInputException {
        String[] args = {"--input", "/home/user/data/input.json", "--output", "/home/user/output/result.xml"};
        
        CommandLineArgs result = parser.parse(args);
        
        assertEquals("/home/user/data/input.json", result.getInputFile());
        assertEquals("/home/user/output/result.xml", result.getOutputFile());
    }
}
