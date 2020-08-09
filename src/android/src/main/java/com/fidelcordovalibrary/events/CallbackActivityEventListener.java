package com.fidelcordovalibrary.events;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.fidel.sdk.Fidel;
import com.fidel.sdk.LinkResult;
import com.fidel.sdk.LinkResultError;
import com.fidelcordovalibrary.adapters.abstraction.DataConverter;
import com.fidelcordovalibrary.adapters.abstraction.DataProcessor;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;

public final class CallbackActivityEventListener
        extends CordovaPlugin
        implements CallbackInput {

    private final DataConverter<Object, JSONObject> linkResultConverter;
    private final DataProcessor<JSONObject> errorHandler;
    private CallbackContext callback;

    public CallbackActivityEventListener(DataConverter<Object, JSONObject> linkResultConverter,
                                         DataProcessor<JSONObject> errorHandler) {
        this.linkResultConverter = linkResultConverter;
        this.errorHandler = errorHandler;
    }

    @Override
    public void onActivityResult(//Activity activity,
                                 int requestCode,
                                 int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Fidel.FIDEL_LINK_CARD_REQUEST_CODE) {
            LinkResult result = data.getParcelableExtra(Fidel.FIDEL_LINK_CARD_RESULT_CARD);
            LinkResultError error = result.getError();
            //Log.i("i","In onActivityResult, result / error is: " + result + " / " + error);
            System.out.println("In onActivityResult, result / error is: " + result + " / " + error);
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

                //errorHandler.process(convertedError);
            }
        }
    }

    @Override
    public void callbackIsReady(CallbackContext callback) {
        this.callback = callback;
    }
}
