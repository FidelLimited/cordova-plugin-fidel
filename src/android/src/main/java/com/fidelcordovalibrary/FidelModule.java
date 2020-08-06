
package com.fidelcordovalibrary;

import android.app.Activity;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.abstraction.ConstantsProvider;
import com.fidelcordovalibrary.adapters.abstraction.DataProcessor;
import com.fidelcordovalibrary.events.CallbackInput;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

;

public class FidelModule extends ReactContextBaseJavaModule {

  private final CallbackInput callbackInput;
  private final DataProcessor<ReadableMap> setupProcessor;
  private final DataProcessor<ReadableMap> optionsProcessor;
  private final List<ConstantsProvider> constantsProviderList;

  public FidelModule(ReactApplicationContext reactContext,
                     DataProcessor<JSONObject> setupProcessor,
                     DataProcessor<JSONObject> optionsProcessor,
                     List<ConstantsProvider> constantsProviderList,
                     CallbackInput callbackInput) {
    super(reactContext);
    this.setupProcessor = setupProcessor;
    this.optionsProcessor = optionsProcessor;
    this.callbackInput = callbackInput;
    this.constantsProviderList = constantsProviderList;
  }

  @Override
  public String getName() {
    return "NativeFidelBridge";
  }

  @ReactMethod
  public void openForm(Callback callback) {
    final Activity activity = getCurrentActivity();
    if (activity != null) {
        Fidel.present(activity);
    }
    Log.i("i","callback is: " + callback);
    callbackInput.callbackIsReady(callback);
  }

  @ReactMethod
  public void setup(ReadableMap map) {
    setupProcessor.process(map);
  }

  @ReactMethod
  public void setOptions(ReadableMap map) {
    optionsProcessor.process(map);
  }

  @Nullable
  @Override
  public Map<String, Object> getConstants() {
    return constantsProviderList.get(0).getConstants();
  }
}