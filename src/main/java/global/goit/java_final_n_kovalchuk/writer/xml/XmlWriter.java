package global.goit.java_final_n_kovalchuk.writer.xml;

import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import global.goit.java_final_n_kovalchuk.writer.FileWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Writer for XML files.
 * Writes a list of DataRecords to XML format using StAX (XMLStreamWriter).
 * Each DataRecord becomes a child element of the root "records" element.
 * Output is formatted with proper indentation for readability.
 */
public class XmlWriter implements FileWriter<DataRecord> {

    private static final String ROOT_ELEMENT = "records";
    private static final String RECORD_ELEMENT = "record";
    private static final String INDENT = "  ";
    private static final String NEWLINE = "\n";

    /**
     * Constructs a new XmlWriter.
     */
    public XmlWriter() {
    }

    /**
     * Writes a list of DataRecords to an XML file.
     * The root element is "records", and each DataRecord becomes a "record" child element.
     *
     * @param records the list of DataRecords to write
     * @param file    the file to write to
     * @throws FileConversionException if writing fails
     */
    @Override
    public void write(List<DataRecord> records, File file) throws FileConversionException {
        if (records == null) {
            throw new FileConversionException("Records list cannot be null");
        }

        if (file == null) {
            throw new FileConversionException("File cannot be null");
        }

        try {
            // Ensure parent directory exists
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new FileConversionException(
                        "Failed to create parent directory: " + parentDir.getAbsolutePath()
                    );
                }
            }

            XMLOutputFactory factory = XMLOutputFactory.newInstance();

            try (FileOutputStream fos = new FileOutputStream(file)) {
                XMLStreamWriter writer = factory.createXMLStreamWriter(fos, "UTF-8");

                // Write XML declaration
                writer.writeStartDocument("UTF-8", "1.0");
                writer.writeCharacters(NEWLINE);

                // Write root element
                writer.writeStartElement(ROOT_ELEMENT);
                writer.writeCharacters(NEWLINE);

                // Write each record
                for (DataRecord record : records) {
                    writeRecord(writer, record);
                }

                // Close root element
                writer.writeEndElement();
                writer.writeCharacters(NEWLINE);

                writer.writeEndDocument();
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            throw new FileConversionException(
                "Failed to write XML file: " + file.getAbsolutePath(),
                e
            );
        } catch (XMLStreamException e) {
            throw new FileConversionException(
                "Failed to generate XML for file: " + file.getAbsolutePath(),
                e
            );
        }
    }

    /**
     * Writes a single DataRecord as a "record" element.
     *
     * @param writer the XMLStreamWriter
     * @param record the DataRecord to write
     * @throws XMLStreamException if writing fails
     */
    private void writeRecord(XMLStreamWriter writer, DataRecord record) throws XMLStreamException {
        writer.writeCharacters(INDENT);
        writer.writeStartElement(RECORD_ELEMENT);
        writer.writeCharacters(NEWLINE);

        Map<String, Object> fields = record.getFields();
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            writeField(writer, entry.getKey(), entry.getValue());
        }

        writer.writeCharacters(INDENT);
        writer.writeEndElement();
        writer.writeCharacters(NEWLINE);
    }

    /**
     * Writes a single field as an element.
     * Handles null values by writing an empty element.
     * Supports recursive serialization for Map and List types.
     *
     * @param writer the XMLStreamWriter
     * @param name   the field name (element name)
     * @param value  the field value
     * @throws XMLStreamException if writing fails
     */
    private void writeField(XMLStreamWriter writer, String name, Object value) throws XMLStreamException {
        writer.writeCharacters(INDENT + INDENT);
        writer.writeStartElement(name);

        if (value != null) {
            if (value instanceof Map) {
                // Handle nested objects (Map)
                writer.writeCharacters(NEWLINE);
                writeMap(writer, (Map<String, Object>) value, INDENT + INDENT + INDENT);
                writer.writeCharacters(INDENT + INDENT);
            } else if (value instanceof List) {
                // Handle arrays (List)
                writer.writeCharacters(NEWLINE);
                writeList(writer, (List<Object>) value, INDENT + INDENT + INDENT);
                writer.writeCharacters(INDENT + INDENT);
            } else {
                // Handle primitive values
                writer.writeCharacters(value.toString());
            }
        }

        writer.writeEndElement();
        writer.writeCharacters(NEWLINE);
    }

    /**
     * Writes a Map (nested object) as child elements.
     * Recursively handles nested Map and List values.
     *
     * @param writer the XMLStreamWriter
     * @param map    the Map to write
     * @param indent the current indentation level
     * @throws XMLStreamException if writing fails
     */
    private void writeMap(XMLStreamWriter writer, Map<String, Object> map, String indent) throws XMLStreamException {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            writer.writeCharacters(indent);
            writer.writeStartElement(entry.getKey());

            Object value = entry.getValue();
            if (value != null) {
                if (value instanceof Map) {
                    // Recursively handle nested Map
                    writer.writeCharacters(NEWLINE);
                    writeMap(writer, (Map<String, Object>) value, indent + INDENT);
                    writer.writeCharacters(indent);
                } else if (value instanceof List) {
                    // Recursively handle nested List
                    writer.writeCharacters(NEWLINE);
                    writeList(writer, (List<Object>) value, indent + INDENT);
                    writer.writeCharacters(indent);
                } else {
                    // Handle primitive values
                    writer.writeCharacters(value.toString());
                }
            }

            writer.writeEndElement();
            writer.writeCharacters(NEWLINE);
        }
    }

    /**
     * Writes a List (array) as child elements named "item".
     * Recursively handles nested Map and List values.
     *
     * @param writer the XMLStreamWriter
     * @param list   the List to write
     * @param indent the current indentation level
     * @throws XMLStreamException if writing fails
     */
    private void writeList(XMLStreamWriter writer, List<Object> list, String indent) throws XMLStreamException {
        for (Object item : list) {
            writer.writeCharacters(indent);
            writer.writeStartElement("item");

            if (item != null) {
                if (item instanceof Map) {
                    // Recursively handle nested Map
                    writer.writeCharacters(NEWLINE);
                    writeMap(writer, (Map<String, Object>) item, indent + INDENT);
                    writer.writeCharacters(indent);
                } else if (item instanceof List) {
                    // Recursively handle nested List
                    writer.writeCharacters(NEWLINE);
                    writeList(writer, (List<Object>) item, indent + INDENT);
                    writer.writeCharacters(indent);
                } else {
                    // Handle primitive values
                    writer.writeCharacters(item.toString());
                }
            }

            writer.writeEndElement();
            writer.writeCharacters(NEWLINE);
        }
    }
}
