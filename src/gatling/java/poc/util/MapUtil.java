package poc.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {

    public static Map<String, String> transform(Map<String, Object> map) {
        Map<String, String> transformedMap = new HashMap<>();
        map.forEach((k,v) -> {
            if(v != null) transformedMap.put(k,v.toString());
        });
        return transformedMap;
    }
}
