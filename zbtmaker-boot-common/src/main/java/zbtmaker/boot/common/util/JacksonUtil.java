package zbtmaker.boot.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author zoubaitao
 * date 2022/02/26
 */
public class JacksonUtil {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    private final static Logger LOGGER = LoggerFactory.getLogger(JacksonUtil.class);

    public static String toString(Object object) {
        if (object == null) {
            return "null";
        }
        try {
            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return MAPPER.writeValueAsString(object);
        } catch (Exception ex) {
            LOGGER.error("convert object to string error", ex);
        }
        return "null";
    }

    public static <T> T parseObject(String value, Class<T> clazz) {
        try {
            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return MAPPER.readValue(value, clazz);
        } catch (Exception ex) {
            LOGGER.error("parse object:{} error", value, ex);
        }
        return null;
    }

    public static <T> List<T> parseList(String value, Class<T> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(value);
            List<T> result = new ArrayList<>();
            for (JsonNode node : jsonNode) {
                result.add(MAPPER.treeToValue(node, clazz));
            }
            return result;
        } catch (Exception ex) {
            LOGGER.error("parse str:{} to list error", value, ex);
        }

        return new ArrayList<>();
    }

    public static <T> Set<T> parseSet(String value, Class<T> clazz) {
        Set<T> result = new HashSet<>();
        try {
            JsonNode jsonNode = MAPPER.readTree(value);
            for (JsonNode node : jsonNode) {
                result.add(MAPPER.treeToValue(node, clazz));
            }
            return result;
        } catch (Exception ex) {
            LOGGER.error("parse str:{} to list error", value, ex);
        }
        return new HashSet<>();
    }
}
