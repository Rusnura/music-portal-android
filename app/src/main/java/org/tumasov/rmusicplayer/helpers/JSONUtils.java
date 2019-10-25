package org.tumasov.rmusicplayer.helpers;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
    public static JSONObject parseJSON(String raw) throws JSONException {
        return new JSONObject(raw);
    }
}
