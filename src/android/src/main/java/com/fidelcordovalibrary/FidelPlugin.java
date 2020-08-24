package com.fidelcordovalibrary;

import android.app.Activity;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fidel.sdk.Fidel;
import com.fidel.sdk.LinkResult;
import com.fidel.sdk.LinkResultError;
import com.fidel.sdk.view.EnterCardDetailsActivity;
import com.fidelcordovalibrary.adapters.abstraction.CountryAdapter;
import com.fidelcordovalibrary.adapters.abstraction.ConstantsProvider;
import com.fidelcordovalibrary.adapters.abstraction.DataProcessor;
import com.fidelcordovalibrary.events.CallbackInput;
import com.fidelcordovalibrary.adapters.FidelOptionsAdapter;
import com.fidelcordovalibrary.adapters.FidelSetupAdapter;
import com.fidelcordovalibrary.adapters.ImageFromReadableMapAdapter;
import com.fidelcordovalibrary.adapters.FidelCountryAdapter;
import com.fidelcordovalibrary.adapters.FidelCardSchemesAdapter;
import com.fidelcordovalibrary.adapters.WritableMapDataConverter;
import com.fidelcordovalibrary.adapters.abstraction.ObjectFactory;
import com.fidelcordovalibrary.events.CallbackActivityEventListener;
import com.fidelcordovalibrary.events.ErrorEventEmitter;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.Nullable;

public class FidelPlugin extends CordovaPlugin {

    private CallbackInput callbackInput;
    private DataProcessor<JSONObject> setupProcessor;
    private FidelOptionsAdapter optionsAdapter;
    private List<ConstantsProvider> constantsProviderList;
    private WritableMapDataConverter linkResultConverter;
    private CallbackContext callback;

    public FidelPlugin() {
        super();
    }

    private static final String OPEN_FORM = "openForm";
    private static final String SETUP = "setup";
    private static final String SET_OPTIONS = "setOptions";

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
        Log.i("i","In execute, action is: " + action);
        initialize(callbackContext);
        switch (action) {
            case OPEN_FORM:
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    openForm(callbackContext);
                }
            });
            return true;                
            case SETUP:
                setup(args);
                return true;
            case SET_OPTIONS:
                setOptions(args);
                return true;
        }
        return false;
    }

    private void initialize(CallbackContext callbackContext) {
        this.setupProcessor = new FidelSetupAdapter();
        Context context = this.cordova.getActivity().getApplicationContext();
        ImageFromReadableMapAdapter imageAdapter =
                new ImageFromReadableMapAdapter(context);
        CountryAdapter countryAdapter =
                new FidelCountryAdapter();
        FidelCardSchemesAdapter cardSchemesAdapter =
                new FidelCardSchemesAdapter();
        this.optionsAdapter = new FidelOptionsAdapter(imageAdapter, countryAdapter, cardSchemesAdapter);
        imageAdapter.bitmapOutput = this.optionsAdapter;
        this.constantsProviderList =
                new ArrayList<>();
        constantsProviderList.add(optionsAdapter);
        this.linkResultConverter =
                new WritableMapDataConverter(new ObjectFactory<JSONObject>() {
                    @Override
                    public JSONObject create() {
                        return new JSONObject();
                    }
                });
        ErrorEventEmitter errorEventEmitter =
                new ErrorEventEmitter(context);
        this.callbackInput =
                new CallbackActivityEventListener(linkResultConverter, errorEventEmitter);
        this.callback = callbackContext;
    }

    @Override
    public Bundle onSaveInstanceState() {
        Log.i("i", "In onSaveInstanceState, activity killed");
        return null;
    }

    @Override
    public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {
        Log.i("i", "In onRestoreStateForActivityResult");
    }

    @Override
    public void onActivityResult(//Activity activity,
                                 int requestCode,
                                 int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("i","In onActivityResult of main class, requestCode is: " + requestCode);
         if (requestCode == Fidel.FIDEL_LINK_CARD_REQUEST_CODE) {
             LinkResult result = data.getParcelableExtra(Fidel.FIDEL_LINK_CARD_RESULT_CARD);
             LinkResultError error = result.getError();
             Log.i("i","In onActivityResult, result / error is: " + result + " / " + error);
             if (error == null) {
                 JSONObject convertedLinkResult = linkResultConverter.getConvertedDataFor(result);
                 callback.success(convertedLinkResult);
             }
             else {
                 JSONObject convertedError = linkResultConverter.getConvertedDataFor(error);
                 //TODO: If this works, create ErrorProcessor interface with process method and implement it in ErrorEmitter with input params data + callback
                 if (convertedError == null) {
                     callback.error("CardLinkFailed");
                 }
                 else {
                     System.out.println("Callback is " + callback);
                     callback.error(convertedError);
                 }
             }
         }
    }

    private void openForm(CallbackContext callback) {
        final Activity activity = cordova.getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, EnterCardDetailsActivity.class);
            cordova.startActivityForResult((CordovaPlugin) this, intent, Fidel.FIDEL_LINK_CARD_REQUEST_CODE);
        }
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
            //optionsProcessor.process(optionsObject);
            optionsAdapter.process(optionsObject);
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
