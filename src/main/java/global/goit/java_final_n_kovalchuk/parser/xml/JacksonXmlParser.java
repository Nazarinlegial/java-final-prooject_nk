package global.goit.java_final_n_kovalchuk.parser.xml;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import global.goit.java_final_n_kovalchuk.exception.FileConversionException;
import global.goit.java_final_n_kovalchuk.model.DataRecord;
import global.goit.java_final_n_kovalchuk.parser.FileParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Parser for XML files using Jackson XML library.
 * This parser uses XmlMapper to parse XML into Map structures,
 * which preserves nested objects and arrays.
 * 
 * The XML structure expected:
 * <records>
 *   <record>
 *     <field1>value1</field1>
 *     <field2>value2</field2>
 *     <data>
 *       <item>
 *         <nestedField>nestedValue</nestedField>
 *       </item>
 *     </data>
 *   </record>
 * </records>
 * 
 * This parser correctly handles:
 * - Nested objects (represented as Map)
 * - Arrays (represented as List)
 * - Null values (empty elements)
 * - Primitive types (String, Integer, Double, Boolean)
 */
public class JacksonXmlParser implements FileParser<DataRecord> {

    private final XmlMapper xmlMapper;
    private final ObjectMapper objectMapper;

    private static final String ROOT_ELEMENT = "records";
    private static final String RECORD_ELEMENT = "record";

    /**
     * Constructs a new JacksonXmlParser with default XmlMapper and ObjectMapper.
     * Configures XmlMapper to convert empty XML elements to null.
     */
    public JacksonXmlParser() {
        this.xmlMapper = new XmlMapper();
        // Configure XmlMapper to convert empty XML elements to null
        this.xmlMapper.enable(FromXmlParser.Feature.EMPTY_ELEMENT_AS_NULL);
        
        // Configure ObjectMapper to keep all values as String
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Constructs a new JacksonXmlParser with custom XmlMapper and ObjectMapper.
     *
     * @param xmlMapper    the XmlMapper to use for XML parsing
     * @param objectMapper the ObjectMapper to use for value conversion
     */
    public JacksonXmlParser(XmlMapper xmlMapper, ObjectMapper objectMapper) {
        this.xmlMapper = xmlMapper;
        this.objectMapper = objectMapper;
    }

    /**
     * Parses an XML file and returns a list of DataRecords.
     * Uses Jackson XML library to parse the XML into Map structures,
     * preserving nested objects and arrays.
     *
     * @param file the XML file to parse
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
            // Parse XML to Map using Jackson XML
            @SuppressWarnings("unchecked")
            Map<String, Object> rootMap = xmlMapper.readValue(file, Map.class);

            // Unwrap item wrappers to restore array structure
            // This converts { "data": { "item": [...] } } to { "data": [...] }
            @SuppressWarnings("unchecked")
            Map<String, Object> unwrappedMap = (Map<String, Object>) unwrapItemWrappers(rootMap);

            List<DataRecord> records = new ArrayList<>();

            // Check if root contains "records" element
            if (unwrappedMap.containsKey(ROOT_ELEMENT)) {
                Object recordsObj = unwrappedMap.get(ROOT_ELEMENT);
                if (recordsObj instanceof Map) {
                    // Single record inside records
                    @SuppressWarnings("unchecked")
                    Map<String, Object> recordMap = (Map<String, Object>) recordsObj;
                    records.add(convertMapToDataRecord(recordMap));
                } else if (recordsObj instanceof List) {
                    // Multiple records inside records
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> recordList = (List<Map<String, Object>>) recordsObj;
                    for (Map<String, Object> recordMap : recordList) {
                        if (recordMap != null) {
                            records.add(convertMapToDataRecord(recordMap));
                        }
                    }
                }
            } else if (unwrappedMap.containsKey(RECORD_ELEMENT)) {
                // Direct record element at root
                Object recordObj = unwrappedMap.get(RECORD_ELEMENT);
                if (recordObj instanceof Map) {
                    // Single record
                    @SuppressWarnings("unchecked")
                    Map<String, Object> recordMap = (Map<String, Object>) recordObj;
                    records.add(convertMapToDataRecord(recordMap));
                } else if (recordObj instanceof List) {
                    // Multiple records
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> recordList = (List<Map<String, Object>>) recordObj;
                    for (Map<String, Object> recordMap : recordList) {
                        if (recordMap != null) {
                            records.add(convertMapToDataRecord(recordMap));
                        }
                    }
                }
            } else {
                // Root itself is a record
                records.add(convertMapToDataRecord(unwrappedMap));
            }

            return records;
        } catch (IOException e) {
            throw new FileConversionException(
                "Failed to parse XML file: " + file.getAbsolutePath(),
                e
            );
        }
    }

    /**
     * Converts a Map to a DataRecord.
     * Handles nested structures (Map, List) and primitive types.
     *
     * @param map the Map to convert
     * @return a DataRecord containing the data from the Map
     */
    private DataRecord convertMapToDataRecord(Map<String, Object> map) {
        DataRecord record = new DataRecord();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Convert value to appropriate type
            Object convertedValue = convertValue(value);
            record.addField(key, convertedValue);
        }

        return record;
    }

    /**
     * Converts a value to an appropriate Java type.
     * Handles nested structures (Map, List) and primitive types.
     *
     * @param value the value to convert
     * @return the converted value
     */
    private Object convertValue(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Map) {
            // Convert nested Map to Map<String, Object>
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            return convertMap(map);
        } else if (value instanceof List) {
            // Convert List to List<Object>
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) value;
            return convertList(list);
        } else if (value instanceof String) {
            String strValue = (String) value;
            
            // Check for null string representation
            if ("null".equalsIgnoreCase(strValue.trim())) {
                return null;
            }
            
            // Check for empty string (from empty XML elements)
            if (strValue.isEmpty() || strValue.trim().isEmpty()) {
                return null;
            }
            
            // Try to convert to numeric types
            try {
                if (strValue.contains(".")) {
                    return Double.parseDouble(strValue);
                } else {
                    long longValue = Long.parseLong(strValue);
                    // Check if it fits in int range
                    if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                        return (int) longValue;
                    }
                    return longValue;
                }
            } catch (NumberFormatException e) {
                // Not a number, return as string
                return strValue;
            }
        } else if (value instanceof Integer || value instanceof Long || 
                   value instanceof Double || value instanceof Boolean) {
            // Already a primitive type
            return value;
        }

        // Default: return as string
        return value.toString();
    }

    /**
     * Converts a Map to a Map<String, Object> recursively.
     *
     * @param map the Map to convert
     * @return the converted Map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> convertMap(Map<String, Object> map) {
        Map<String, Object> result = new java.util.HashMap<>();
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            result.put(key, convertValue(value));
        }
        
        return result;
    }

    /**
     * Converts a List to a List<Object> recursively.
     *
     * @param list the List to convert
     * @return the converted List
     */
    @SuppressWarnings("unchecked")
    private List<Object> convertList(List<Object> list) {
        List<Object> result = new ArrayList<>();
        
        for (Object item : list) {
            result.add(convertValue(item));
        }
        
        return result;
    }

    /**
     * Unwraps item wrappers to restore array structure after XML parsing.
     * This method recursively processes Maps and Lists to convert
     * { "fieldName": { "item": [...] } } to { "fieldName": [...] }.
     * This is needed because XmlWriter wraps array items in <item> tags,
     * and Jackson XML parser interprets this as an object with an "item" field.
     *
     * @param obj the object to process (Map, List, or primitive)
     * @return the processed object with item wrappers unwrapped
     */
    private Object unwrapItemWrappers(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            Map<String, Object> result = new java.util.HashMap<>();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                // Check if this is an item wrapper: a Map with single key "item" containing a List
                if (value instanceof Map) {
                    Map<String, Object> valueMap = (Map<String, Object>) value;
                    if (valueMap.size() == 1 && valueMap.containsKey("item")) {
                        Object itemValue = valueMap.get("item");
                        if (itemValue instanceof List) {
                            // This is an item wrapper, unwrap it
                            List<Object> unwrappedList = new ArrayList<>();
                            for (Object item : (List<Object>) itemValue) {
                                unwrappedList.add(unwrapItemWrappers(item));
                            }
                            result.put(key, unwrappedList);
                            continue;
                        }
                    }
                }

                // Recursively process the value
                result.put(key, unwrapItemWrappers(value));
            }

            return result;
        } else if (obj instanceof List) {
            List<Object> list = (List<Object>) obj;
            List<Object> result = new ArrayList<>();

            for (Object item : list) {
                result.add(unwrapItemWrappers(item));
            }

            return result;
        }

        // Primitive value, return as is
        return obj;
    }
}
