package org.tumasov.rmusicplayer.helpers;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JSONUtils {
    public static JSONObject parseJSON(String raw) throws JSONException {
        return new JSONObject(raw);
    }

    public static JSONArray parseJSONArray(String raw) throws JSONException {
        return new JSONArray(raw);
    }

    public static <T> T getObjectFromJSON(JSONObject json, Class<T> clazz) {
        try {
            T object = clazz.newInstance();
            for (Field field: clazz.getDeclaredFields()) {
                String fieldName = field.getName();
                if (!json.has(fieldName)) {
                    Log.w("JSONUtils", "Json object don't contains field: " + fieldName + "! Skipping...");
                }
                Class fieldType = field.getType();
                String methodName = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
                Method setter = object.getClass().getMethod(methodName, fieldType);
                setter.setAccessible(true);
                setter.invoke(object, json.get(fieldName));
            }
            return object;
        } catch (Exception e) {
            Log.e("JSONUtils", "Cannot deserialize JSON to " + clazz.getSimpleName() + ": " + e.getMessage());
            return null;
        }
    }

    public static <T> T getObjectFromJSON(String raw, Class<T> clazz) throws JSONException {
        return getObjectFromJSON(parseJSON(raw), clazz);
    }
}
