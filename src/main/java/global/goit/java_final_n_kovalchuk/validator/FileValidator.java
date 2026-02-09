package global.goit.java_final_n_kovalchuk.validator;

import global.goit.java_final_n_kovalchuk.exception.InvalidInputException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Validator for file operations.
 * Checks file existence, readability, and format.
 */
public class FileValidator {

    /**
     * Validates the input file and returns its format.
     * Checks if the file exists, is readable, and has a supported format.
     *
     * @param filePath the path to the file to validate
     * @return the detected FileFormat of the file
     * @throws InvalidInputException if the file is invalid or cannot be read
     */
    public static FormatDetector.FileFormat validateFile(String filePath) throws InvalidInputException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new InvalidInputException("File path cannot be null or empty");
        }

        Path path = Paths.get(filePath.trim());
        File file = path.toFile();

        // Check if file exists
        if (!file.exists()) {
            throw new InvalidInputException(
                    "File does not exist: '" + filePath + "'"
            );
        }

        // Check if it's a file (not a directory)
        if (!file.isFile()) {
            throw new InvalidInputException(
                    "Path is not a file: '" + filePath + "'"
            );
        }

        // Check if file is readable
        if (!Files.isReadable(path)) {
            throw new InvalidInputException(
                    "File is not readable: '" + filePath + "'. Check file permissions."
            );
        }

        // Check if file is not empty
        if (file.length() == 0) {
            throw new InvalidInputException(
                    "File is empty: '" + filePath + "'"
            );
        }

        // Detect and return the format
        return FormatDetector.detectFormat(filePath);
    }
}
