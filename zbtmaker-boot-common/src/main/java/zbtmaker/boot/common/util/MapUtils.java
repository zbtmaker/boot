package zbtmaker.boot.common.util;

import java.util.Map;

/**
 * @author zoubaitao
 * date 2022/07/09
 */
public class MapUtils {

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }
}
