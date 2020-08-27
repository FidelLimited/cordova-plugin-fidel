package com.fidelcordovalibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.fidel.sdk.Fidel;
import com.fidel.sdk.LinkResult;
import com.fidel.sdk.LinkResultError;
import com.fidel.sdk.view.EnterCardDetailsActivity;
import com.fidelcordovalibrary.adapters.FidelCardSchemesAdapter;
import com.fidelcordovalibrary.adapters.FidelCountryAdapter;
import com.fidelcordovalibrary.adapters.FidelOptionsAdapter;
import com.fidelcordovalibrary.adapters.FidelSetupAdapter;
import com.fidelcordovalibrary.adapters.ImageFromReadableMapAdapter;
import com.fidelcordovalibrary.adapters.WritableMapDataConverter;
import com.fidelcordovalibrary.adapters.abstraction.ConstantsProvider;
import com.fidelcordovalibrary.adapters.abstraction.CountryAdapter;
import com.fidelcordovalibrary.adapters.abstraction.DataConverter;
import com.fidelcordovalibrary.adapters.abstraction.DataProcessor;
import com.fidelcordovalibrary.adapters.abstraction.ObjectFactory;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FidelPlugin extends CordovaPlugin {

    private DataProcessor<JSONObject> setupProcessor;
    private FidelOptionsAdapter optionsAdapter;
    private List<ConstantsProvider> constantsProviderList;
    private DataConverter<Object, JSONObject> linkResultConverter;
    private CallbackContext callback;

    public FidelPlugin() {
        super();
    }

    private static final String OPEN_FORM = "openForm";
    private static final String SETUP = "setup";
    private static final String SET_OPTIONS = "setOptions";

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
        Context context = this.cordova.getActivity().getApplicationContext();
        initialize(callbackContext, context);
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

    public void initialize(CallbackContext callbackContext, Context context) {
        setupProcessor = new FidelSetupAdapter();
        ImageFromReadableMapAdapter imageAdapter =
                new ImageFromReadableMapAdapter(context);
        CountryAdapter countryAdapter =
                new FidelCountryAdapter();
        FidelCardSchemesAdapter cardSchemesAdapter =
                new FidelCardSchemesAdapter();
        optionsAdapter = new FidelOptionsAdapter(imageAdapter, countryAdapter, cardSchemesAdapter);
        imageAdapter.bitmapOutput = optionsAdapter;
        constantsProviderList =
                new ArrayList<>();
        constantsProviderList.add(optionsAdapter);
        linkResultConverter = new WritableMapDataConverter(new ObjectFactory<JSONObject>() {
            @Override
            public JSONObject create() {
                return new JSONObject();
            }
        });
        callback = callbackContext;
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == Fidel.FIDEL_LINK_CARD_REQUEST_CODE) {
             LinkResult result = data.getParcelableExtra(Fidel.FIDEL_LINK_CARD_RESULT_CARD);
             LinkResultError error = result.getError();
             if (error == null) {
                 JSONObject convertedLinkResult = linkResultConverter.getConvertedDataFor(result);
                 callback.success(convertedLinkResult);
             }
             else {
                 JSONObject convertedError = linkResultConverter.getConvertedDataFor(error);
                 callback.error(convertedError);
             }
         }
    }

    private void openForm(CallbackContext callback) {
        final Activity activity = cordova.getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, EnterCardDetailsActivity.class);
            cordova.startActivityForResult((CordovaPlugin) this, intent, Fidel.FIDEL_LINK_CARD_REQUEST_CODE);
        }
    }

    private void setup(JSONArray map) {
        try {
            JSONObject setupObject = map.getJSONObject(0);
            setupProcessor.process(setupObject);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setOptions(JSONArray map) {
        try {
            JSONObject optionsObject = map.getJSONObject(0);
            optionsAdapter.process(optionsObject);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public Map<String, Object> getConstants() {
    return constantsProviderList.get(0).getConstants();
    }
}
