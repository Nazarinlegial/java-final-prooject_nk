package global.goit.java_final_n_kovalchuk.cli;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CommandLineArgs class.
 */
class CommandLineArgsTest {

    @Test
    void testConstructorAndGetters() {
        CommandLineArgs args = new CommandLineArgs("input.json", "output.xml");
        
        assertEquals("input.json", args.getInputFile());
        assertEquals("output.xml", args.getOutputFile());
    }

    @Test
    void testValidateSuccess() {
        CommandLineArgs args = new CommandLineArgs("input.json", "output.xml");
        
        assertDoesNotThrow(() -> args.validate());
    }

    @Test
    void testValidateNullInput() {
        CommandLineArgs args = new CommandLineArgs(null, "output.xml");
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> args.validate()
        );
        
        assertEquals("Input file path cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateEmptyInput() {
        CommandLineArgs args = new CommandLineArgs("", "output.xml");
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> args.validate()
        );
        
        assertEquals("Input file path cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateWhitespaceInput() {
        CommandLineArgs args = new CommandLineArgs("   ", "output.xml");
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> args.validate()
        );
        
        assertEquals("Input file path cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateNullOutput() {
        CommandLineArgs args = new CommandLineArgs("input.json", null);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> args.validate()
        );
        
        assertEquals("Output file path cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateEmptyOutput() {
        CommandLineArgs args = new CommandLineArgs("input.json", "");
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> args.validate()
        );
        
        assertEquals("Output file path cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateWhitespaceOutput() {
        CommandLineArgs args = new CommandLineArgs("input.json", "   ");
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> args.validate()
        );
        
        assertEquals("Output file path cannot be null or empty", exception.getMessage());
    }

    @Test
    void testToString() {
        CommandLineArgs args = new CommandLineArgs("input.json", "output.xml");
        String expected = "CommandLineArgs{inputFile='input.json', outputFile='output.xml', csvMapping=false}";
        
        assertEquals(expected, args.toString());
    }
}
