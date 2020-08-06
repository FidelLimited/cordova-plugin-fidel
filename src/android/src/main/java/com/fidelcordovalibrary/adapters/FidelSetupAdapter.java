package com.fidelcordovalibrary.adapters;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.abstraction.DataProcessor;

import org.json.JSONObject;

public final class FidelSetupAdapter implements DataProcessor<JSONObject> {
    public static final String API_KEY = "apiKey";
    public static final String PROGRAM_ID_KEY = "programId";

    @Override
    public void process(JSONObject data) {
        try {
            Fidel.apiKey = data.getString(API_KEY);
            Fidel.programId = data.getString(PROGRAM_ID_KEY);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
