package global.goit.java_final_n_kovalchuk.parser.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import global.goit.java_final_n_kovalchuk.parser.FileParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Parser for JSON files.
 * Handles both simple JSON objects and arrays of objects.
 * Uses Jackson ObjectMapper for parsing.
 */
public class JsonParser implements FileParser<DataRecord> {

    private final ObjectMapper objectMapper;

    /**
     * Constructs a new JsonParser with a default ObjectMapper.
     */
    public JsonParser() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Constructs a new JsonParser with a custom ObjectMapper.
     *
     * @param objectMapper the ObjectMapper to use for parsing
     */
    public JsonParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Parses a JSON file and returns a list of DataRecords.
     * Handles both single JSON objects and arrays of objects.
     *
     * @param file the JSON file to parse
     * @return a list of DataRecords parsed from the file
     * @throws FileConversionException if parsing fails
     */
    @Override
    public List<DataRecord> parse(File file) throws FileConversionException {
        if (file == null) {
            throw new FileConversionException("File cannot be null");
        }

        if (!file.exists()) {
            throw new FileConversionException("File does not exist: " + file.getAbsolutePath());
        }

        if (!file.canRead()) {
            throw new FileConversionException("File cannot be read: " + file.getAbsolutePath());
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);

            List<DataRecord> records = new ArrayList<>();

            if (rootNode.isArray()) {
                // Handle JSON array
                Iterator<JsonNode> elements = rootNode.elements();
                while (elements.hasNext()) {
                    JsonNode element = elements.next();
                    records.add(convertJsonNodeToDataRecord(element));
                }
            } else if (rootNode.isObject()) {
                // Handle single JSON object
                records.add(convertJsonNodeToDataRecord(rootNode));
            } else {
                throw new FileConversionException(
                    "Invalid JSON format: expected object or array, got " + rootNode.getNodeType()
                );
            }

            return records;
        } catch (IOException e) {
            throw new FileConversionException(
                "Failed to parse JSON file: " + file.getAbsolutePath(),
                e
            );
        }
    }

    /**
     * Converts a JsonNode to a DataRecord.
     * Handles nested structures by converting them to strings.
     *
     * @param jsonNode the JsonNode to convert
     * @return a DataRecord containing the data from the JsonNode
     */
    private DataRecord convertJsonNodeToDataRecord(JsonNode jsonNode) {
        DataRecord record = new DataRecord();

        if (!jsonNode.isObject()) {
            return record;
        }

        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String key = entry.getKey();
            JsonNode valueNode = entry.getValue();

            Object value = convertJsonNodeToValue(valueNode);
            record.addField(key, value);
        }

        return record;
    }

    /**
     * Converts a JsonNode to an appropriate Java value.
     * Handles primitive types, arrays, and nested objects.
     *
     * @param jsonNode the JsonNode to convert
     * @return the converted Java value
     */
    private Object convertJsonNodeToValue(JsonNode jsonNode) {
        if (jsonNode.isTextual()) {
            return jsonNode.asText();
        } else if (jsonNode.isInt()) {
            return jsonNode.asInt();
        } else if (jsonNode.isLong()) {
            return jsonNode.asLong();
        } else if (jsonNode.isDouble()) {
            return jsonNode.asDouble();
        } else if (jsonNode.isBoolean()) {
            return jsonNode.asBoolean();
        } else if (jsonNode.isNull()) {
            return null;
        } else if (jsonNode.isArray()) {
            List<Object> list = new ArrayList<>();
            Iterator<JsonNode> elements = jsonNode.elements();
            while (elements.hasNext()) {
                list.add(convertJsonNodeToValue(elements.next()));
            }
            return list;
        } else if (jsonNode.isObject()) {
            // For nested objects, convert to a map representation
            DataRecord nestedRecord = convertJsonNodeToDataRecord(jsonNode);
            return nestedRecord.getFields();
        }

        return jsonNode.toString();
    }
}
