package com.fidelcordovalibrary.fakes;

//import com.facebook.react.bridge.Callback;
//import com.facebook.react.bridge.ReadableMap;
import com.fidelcordovalibrary.events.CallbackInput;

import org.apache.cordova.CallbackContext;

public final class CallbackSpy implements CallbackInput {

    public Boolean didInvoke = false;
//    public ReadableMap receivedResultMap;
//    public ReadableMap receivedErrorMap;
    private CallbackContext callback;

    @Override
    public void callbackIsReady(CallbackContext callbackContext) {
        this.callback = callbackContext;
    }

//    @Override
//    public void invoke(Object... args) {
//        receivedErrorMap = (ReadableMap)args[0];
//        receivedResultMap = (ReadableMap)args[1];
//        didInvoke = true;
//    }
}
