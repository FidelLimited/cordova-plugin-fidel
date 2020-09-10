package com.fidelcordovalibrary.adapters;

import com.fidel.sdk.LinkResultErrorCode;
import com.fidelcordovalibrary.adapters.abstraction.DataConverter;
import com.fidelcordovalibrary.adapters.abstraction.ObjectFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Iterator;

public final class JSONObjectDataConverter implements DataConverter<Object, JSONObject> {

    private ObjectFactory<JSONObject> jsonObjectFactory;
    public JSONObjectDataConverter(ObjectFactory<JSONObject> jsonObjectFactory) {
        this.jsonObjectFactory = jsonObjectFactory;
    }
    @Override
    public JSONObject getConvertedDataFor(Object data) {
        if (data == null) {
            return null;
        }
        JSONObject createdJsonObject = jsonObjectFactory.create();
        try {
            for (Field field: data.getClass().getDeclaredFields()) {
                if (field.getType() == String.class) {
                    createdJsonObject.put(field.getName(), (String)field.get(data));
                }
                else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                    createdJsonObject.put(field.getName(), (boolean)field.get(data));
                }
                else if (field.getType() == int.class) {
                    createdJsonObject.put(field.getName(), (int)field.get(data));
                }
                else if (field.getType() == LinkResultErrorCode.class) {
                    LinkResultErrorCode errorCode = (LinkResultErrorCode)field.get(data);
                    createdJsonObject.put(field.getName(), errorCode.toString().toLowerCase());
                }
                else if (field.getType() == JSONObject.class) {
                    JSONObject jsonObjectToPut = this.getJsonObjectFor((JSONObject)field.get(data));
                    createdJsonObject.put(field.getName(), jsonObjectToPut);
                }
            }
            return createdJsonObject;
        }
        catch (Exception e) {
            return createdJsonObject;
        }
    }

    private JSONObject getJsonObjectFor(JSONObject json) {
        Iterator<String> jsonKeyIterator = json.keys();
        JSONObject map = jsonObjectFactory.create();
        while (jsonKeyIterator.hasNext()) {
            String key = jsonKeyIterator.next();
            try {
                Object value = json.get(key);
                Class valueClass = value.getClass();
                if (valueClass == String.class) {
                    map.put(key, (String)value);
                }
                else if (valueClass == boolean.class || valueClass == Boolean.class) {
                    map.put(key, (boolean)value);
                }
                else if (valueClass == int.class || valueClass == Integer.class) {
                    map.put(key, (int)value);
                }
                else if (valueClass == JSONObject.class) {
                    JSONObject mapToPut = this.getJsonObjectFor((JSONObject)value);
                    map.put(key, mapToPut);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
