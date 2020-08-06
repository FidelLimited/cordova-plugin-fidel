package com.fidelcordovalibrary;

import android.app.Activity;
import android.util.Log;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.abstraction.ConstantsProvider;
import com.fidelcordovalibrary.adapters.abstraction.DataProcessor;
import com.fidelcordovalibrary.events.CallbackInput;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

public class FidelPlugin extends CordovaPlugin {

    private final CallbackInput callbackInput;
    private final DataProcessor<JSONObject> setupProcessor;
    private final DataProcessor<JSONObject> optionsProcessor;
    private final List<ConstantsProvider> constantsProviderList;
  
    public FidelPlugin(DataProcessor<JSONObject> setupProcessor,
                    DataProcessor<JSONObject> optionsProcessor,
                    List<ConstantsProvider> constantsProviderList,
                    CallbackInput callbackInput) {
        this.setupProcessor = setupProcessor;
        this.optionsProcessor = optionsProcessor;
        this.callbackInput = callbackInput;
        this.constantsProviderList = constantsProviderList;
    }

    private static final String OPEN_FORM = "openForm";
    private static final String SETUP = "setup";
    private static final String SET_OPTIONS = "setOptions";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action) {
            case OPEN_FORM:
                openForm(callbackContext);
                break;
            case SETUP:
                setup(args);
                break;
            case SET_OPTIONS:
                setOptions(args);
                break;
        }
        return false;
    }

    private void openForm(CallbackContext callback) {
        final Activity activity = cordova.getActivity();
        if (activity != null) {
            Fidel.present(activity);
        }
        Log.i("i","callback is: " + callback);
        callbackInput.callbackIsReady(callback);
    }

    private void setup(JSONArray map) {
        try {
            JSONObject setupObject = map.getJSONObject(0);
            setupProcessor.process(setupObject);
        }
        catch (JSONException e) {
            e.printStackTrace();//TODO: Find a way to send a developer-friendly error back to Cordova
        }
    }

    private void setOptions(JSONArray map) {
        try {
            JSONObject optionsObject = map.getJSONObject(0);
            optionsProcessor.process(optionsObject);
        }
        catch (JSONException e) {
            e.printStackTrace();//TODO: Find a way to send a developer-friendly error back to Cordova
        }
    }

    @Nullable
    public Map<String, Object> getConstants() {
    return constantsProviderList.get(0).getConstants();
    }
}
