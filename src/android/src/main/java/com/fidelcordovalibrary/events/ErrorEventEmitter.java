package com.fidelcordovalibrary.events;

import com.fidelcordovalibrary.adapters.abstraction.DataProcessor;
import android.content.Context;

import org.json.JSONObject;

public final class ErrorEventEmitter implements DataProcessor<JSONObject> {

    private Context context;

    public ErrorEventEmitter(Context context) {
        this.context = context;
    }

   @Override
   public void process(JSONObject data) {
       //TODO: see if this class is needed
   }
}
