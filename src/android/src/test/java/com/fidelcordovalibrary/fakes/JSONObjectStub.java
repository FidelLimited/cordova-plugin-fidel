package com.fidelcordovalibrary.fakes;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectStub {

    public static JSONObject JSONObjectWithNoKey() {
        JSONObject object = new JSONObject();
        try {
            object.put("", "");
            return object;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return object;
        }
    }

    public static JSONObject JSONObjectWithExistingKey(String existingKey) {
        JSONObject object = new JSONObject();
        try {
            object.put(existingKey, "");
            return object;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return object;
        }
    }

    public static JSONObject JSONObjectWithExistingKeyButNoValue(String existingKey) {
        JSONObject object = new JSONObject();
        try {
            object.put(existingKey, existingKey);
            return object;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return object;
        }
    }

    public static JSONObject JSONObjectWithKeyAndValue(String existingKey, Object value) {
        JSONObject object = new JSONObject();
        try {
            object.put(existingKey, value);
            return object;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return object;
        }
    }
}
