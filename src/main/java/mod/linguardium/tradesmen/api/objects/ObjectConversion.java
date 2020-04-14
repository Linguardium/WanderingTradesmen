package mod.linguardium.tradesmen.api.objects;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.List;
import java.util.Map;


public class ObjectConversion {
    /*
        retrieved from https://stackoverflow.com/questions/33632420/nashorn-scriptobjectmirror-js-java-type-conversion
        posted by user Igor: https://stackoverflow.com/users/1306536/igor
     */
    @SuppressWarnings("removal")
    public static Object toJavaObject(Object scriptObj) {
        if (scriptObj instanceof ScriptObjectMirror) {
            ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) scriptObj;
            if (scriptObjectMirror.isArray()) {
                List<Object> list = Lists.newArrayList();
                for (Map.Entry<String, Object> entry : scriptObjectMirror.entrySet()) {
                    list.add(toJavaObject(entry.getValue()));
                }
                return list;
            } else {
                Map<String, Object> map = Maps.newHashMap();
                for (Map.Entry<String, Object> entry : scriptObjectMirror.entrySet()) {
                    map.put(entry.getKey(), toJavaObject(entry.getValue()));
                }
                return map;
            }
        } else {
            return scriptObj;
        }
    }

}
