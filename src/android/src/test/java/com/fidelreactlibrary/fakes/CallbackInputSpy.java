package com.fidelcordovalibrary.fakes;

import com.facebook.react.bridge.Callback;
import com.fidelcordovalibrary.events.CallbackInput;

public final class CallbackInputSpy implements CallbackInput {
    public Callback receivedCallback;
    @Override
    public void callbackIsReady(Callback callback) {
        receivedCallback = callback;
    }
}
