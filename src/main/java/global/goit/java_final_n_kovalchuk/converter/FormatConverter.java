package global.goit.java_final_n_kovalchuk.converter;

import global.goit.java_final_n_kovalchuk.exception.FileConversionException;

import java.io.File;

/**
 * Interface for converting files between different formats.
 * Implementations will handle the conversion process from one format to another.
 */
public interface FormatConverter {

    /**
     * Converts a file from one format to another.
     *
     * @param inputFile  the input file to convert
     * @param outputFile the output file to write the converted data to
     * @throws FileConversionException if conversion fails
     */
    void convert(File inputFile, File outputFile) throws FileConversionException;
}
