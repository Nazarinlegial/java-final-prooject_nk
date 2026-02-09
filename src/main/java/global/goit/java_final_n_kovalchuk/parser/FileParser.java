package global.goit.java_final_n_kovalchuk.parser;

import global.goit.java_final_n_kovalchuk.exception.FileConversionException;

import java.io.File;
import java.util.List;

/**
 * Interface for parsing files into data records.
 * Implementations will handle specific file formats (JSON, XML, CSV).
 *
 * @param <T> the type of data records to parse
 */
public interface FileParser<T> {

    /**
     * Parses a file and returns a list of data records.
     *
     * @param file the file to parse
     * @return a list of parsed data records
     * @throws FileConversionException if parsing fails
     */
    List<T> parse(File file) throws FileConversionException;
}
