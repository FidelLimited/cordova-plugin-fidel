package com.fidelcordovalibrary;

import android.app.Activity;
import android.content.Intent;

import com.fidel.sdk.Fidel;
import com.fidel.sdk.LinkResult;
import com.fidel.sdk.LinkResultError;
import com.fidel.sdk.view.EnterCardDetailsActivity;
import com.fidelcordovalibrary.adapters.FidelCardSchemesAdapter;
import com.fidelcordovalibrary.adapters.FidelCountryAdapter;
import com.fidelcordovalibrary.adapters.FidelOptionsAdapter;
import com.fidelcordovalibrary.adapters.FidelSetupAdapter;
import com.fidelcordovalibrary.adapters.JSONObjectDataConverter;
import com.fidelcordovalibrary.adapters.abstraction.DataConverter;
import com.fidelcordovalibrary.adapters.abstraction.DataProcessor;
import com.fidelcordovalibrary.adapters.abstraction.ObjectFactory;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FidelPlugin extends CordovaPlugin {

    private DataProcessor<JSONObject> setupProcessor;
    private FidelOptionsAdapter optionsAdapter;
    private DataConverter<Object, JSONObject> linkResultConverter;
    private CallbackContext callback;
    private static final String OPEN_FORM = "openForm";
    private static final String SETUP = "setup";
    private static final String SET_OPTIONS = "setOptions";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        setupProcessor = new FidelSetupAdapter();
        FidelCountryAdapter countryAdapter = new FidelCountryAdapter();
        FidelCardSchemesAdapter cardSchemesAdapter =
                new FidelCardSchemesAdapter();
        optionsAdapter = new FidelOptionsAdapter(countryAdapter, cardSchemesAdapter, cordova.getActivity().getApplicationContext());
        linkResultConverter = new JSONObjectDataConverter(new ObjectFactory<JSONObject>() {
            @Override
            public JSONObject create() {
                return new JSONObject();
            }
        });

    }

    public void setCallbackContext(CallbackContext callbackContext) {
        callback = callbackContext;
    }


    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
        setCallbackContext(callbackContext);
        switch (action) {
            case OPEN_FORM:
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    openForm();
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

    private void openForm() {
        final Activity activity = cordova.getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, EnterCardDetailsActivity.class);
            cordova.startActivityForResult((CordovaPlugin) this, intent, Fidel.FIDEL_LINK_CARD_REQUEST_CODE);
        }
    }

    private void setup(JSONArray jsonArray) {
        try {
            JSONObject setupObject = jsonArray.getJSONObject(0);
            setupProcessor.process(setupObject);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setOptions(JSONArray jsonArray) {
        try {
            JSONObject optionsObject = jsonArray.getJSONObject(0);
            optionsAdapter.process(optionsObject);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
