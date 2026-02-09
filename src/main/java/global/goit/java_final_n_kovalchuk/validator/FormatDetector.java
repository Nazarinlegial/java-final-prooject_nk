package global.goit.java_final_n_kovalchuk.validator;

import global.goit.java_final_n_kovalchuk.exception.InvalidInputException;

/**
 * Detector for file formats based on file extensions.
 * Supports JSON, XML, and CSV formats.
 */
public class FormatDetector {

    /**
     * Enum representing supported file formats.
     */
    public enum FileFormat {
        JSON,
        XML,
        CSV
    }

    /**
     * Detects the file format based on the file extension.
     *
     * @param filePath the path to the file
     * @return the detected FileFormat
     * @throws InvalidInputException if the file format is not supported
     */
    public static FileFormat detectFormat(String filePath) throws InvalidInputException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new InvalidInputException("File path cannot be null or empty");
        }

        String normalizedPath = filePath.trim().toLowerCase();
        String extension;

        // Find the last dot in the path
        int lastDotIndex = normalizedPath.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == normalizedPath.length() - 1) {
            throw new InvalidInputException(
                    "File '" + filePath + "' has no extension. Supported formats: .json, .xml, .csv"
            );
        }

        extension = normalizedPath.substring(lastDotIndex);

        return switch (extension) {
            case ".json" -> FileFormat.JSON;
            case ".xml" -> FileFormat.XML;
            case ".csv" -> FileFormat.CSV;
            default -> throw new InvalidInputException(
                    "Unsupported file format: '" + extension + "'. Supported formats: .json, .xml, .csv"
            );
        };
    }
}
