package org.tumasov.rmusicplayer.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
    public static JSONObject parseJSON(String raw) throws JSONException {
        return new JSONObject(raw);
    }

    public static JSONArray parseJSONArray(String raw) throws JSONException {
        return new JSONArray(raw);
    }
}
