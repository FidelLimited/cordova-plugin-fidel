package com.fidelcordovalibrary.fakes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONObjectStub {

    public List<String> keyNamesCheckedFor = new ArrayList<>();
    public HashMap<String, Object> hashMapToReturn;
    public int intToReturn;

    public static JSONObject mapWithNoKey() {
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

    public static JSONObject mapWithExistingKey(String existingKey) {
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

    public static JSONObject mapWithExistingKeyButNoValue(String existingKey) {
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

    public static JSONObject mapWithKeyAndValue(String existingKey, Object value) {
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
