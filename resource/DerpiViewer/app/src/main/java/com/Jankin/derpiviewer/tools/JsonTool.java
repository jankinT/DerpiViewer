package com.Jankin.derpiviewer.tools;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonTool {
    public static String jsonGetString(JSONObject json, String key) throws JSONException {
        if (json.isNull(key)) {
            return null;
        } else {
            return json.getString(key);
        }
    }

    public static void jsonPutString(JSONObject json, String key, String value) throws JSONException {
        if(value == null) {
            json.put(key, JSONObject.NULL);
        } else {
            json.put(key, value);
        }
    }
}
