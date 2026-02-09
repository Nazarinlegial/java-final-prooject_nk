package global.goit.java_final_n_kovalchuk.writer;

import global.goit.java_final_n_kovalchuk.exception.FileConversionException;

import java.io.File;
import java.util.List;

/**
 * Interface for writing data records to files.
 * Implementations will handle specific file formats (JSON, XML, CSV).
 *
 * @param <T> the type of data records to write
 */
public interface FileWriter<T> {

    /**
     * Writes a list of data records to a file.
     *
     * @param records the list of data records to write
     * @param file    the file to write to
     * @throws FileConversionException if writing fails
     */
    void write(List<T> records, File file) throws FileConversionException;
}
