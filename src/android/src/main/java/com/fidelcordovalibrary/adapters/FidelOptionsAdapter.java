package com.fidelcordovalibrary.adapters;

import android.graphics.Bitmap;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.abstraction.CardSchemesAdapter;
import com.fidelcordovalibrary.adapters.abstraction.ConstantsProvider;
import com.fidelcordovalibrary.adapters.abstraction.CountryAdapter;
import com.fidelcordovalibrary.adapters.abstraction.DataOutput;
import com.fidelcordovalibrary.adapters.abstraction.DataProcessor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public final class FidelOptionsAdapter implements DataProcessor<JSONObject>, DataOutput<Bitmap>, ConstantsProvider {

    public static final String BANNER_IMAGE_KEY = "bannerImage";
    public static final String AUTO_SCAN_KEY = "autoScan";
    public static final String COMPANY_NAME_KEY = "companyName";
    public static final String PROGRAM_NAME_KEY = "programName";
    public static final String DELETE_INSTRUCTIONS_KEY = "deleteInstructions";
    public static final String PRIVACY_URL_KEY = "privacyUrl";
    public static final String TERMS_CONDITIONS_URL_KEY = "termsConditionsUrl";
    public static final String META_DATA_KEY = "metaData";
    public static final String COUNTRY_KEY = "country";
    public static final String CARD_SCHEMES_KEY = "supportedCardSchemes";
    public static final List<String> OPTION_KEYS = Collections.unmodifiableList(
            Arrays.asList(
                    BANNER_IMAGE_KEY,
                    AUTO_SCAN_KEY,
                    COMPANY_NAME_KEY,
                    PROGRAM_NAME_KEY,
                    DELETE_INSTRUCTIONS_KEY,
                    PRIVACY_URL_KEY,
                    TERMS_CONDITIONS_URL_KEY,
                    META_DATA_KEY,
                    COUNTRY_KEY,
                    CARD_SCHEMES_KEY
            ));

    private final DataProcessor<JSONObject> imageAdapter;
    private final CountryAdapter countryAdapter;
    private final CardSchemesAdapter cardSchemesAdapter;

    public FidelOptionsAdapter(DataProcessor<JSONObject> imageAdapter,
                               CountryAdapter countryAdapter,
                               CardSchemesAdapter cardSchemesAdapter) {
        this.imageAdapter = imageAdapter;
        this.countryAdapter = countryAdapter;
        this.cardSchemesAdapter = cardSchemesAdapter;
    }

    @Override
    public void process(JSONObject data) {
        System.out.println("In process, data is " + data);
        if (valueIsValidFor(data, BANNER_IMAGE_KEY)) {
            try {
                imageAdapter.process(data.getJSONObject(BANNER_IMAGE_KEY));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (valueIsValidFor(data, AUTO_SCAN_KEY)) {
            try {
                Fidel.autoScan = data.getBoolean(AUTO_SCAN_KEY);
            }
            catch (JSONException e) {
                Fidel.autoScan = false;
            }
        }
        if (valueIsValidFor(data, COMPANY_NAME_KEY)) {
            try {
                Fidel.companyName = data.getString(COMPANY_NAME_KEY);
            }
            catch (JSONException e) {
                Fidel.companyName = "";
            }
        }
        if (valueIsValidFor(data, PROGRAM_NAME_KEY)) {
            try {
                Fidel.programName = data.getString(PROGRAM_NAME_KEY);
            }
            catch (JSONException e) {
                Fidel.programName = "our";
            }
        }
        if (valueIsValidFor(data, DELETE_INSTRUCTIONS_KEY)) {
            try {
                Fidel.deleteInstructions = data.getString(DELETE_INSTRUCTIONS_KEY);
            }
            catch (JSONException e) {
                Fidel.deleteInstructions = "";
            }
        }
        if (valueIsValidFor(data, PRIVACY_URL_KEY)) {
            try {
                Fidel.privacyURL = data.getString(PRIVACY_URL_KEY);
            }
            catch (JSONException e) {
                Fidel.privacyURL = null;
            }
        }
        if (valueIsValidFor(data, TERMS_CONDITIONS_URL_KEY)) {
            try {
                Fidel.termsConditionsURL = data.getString(TERMS_CONDITIONS_URL_KEY);
            }
            catch (JSONException e) {
                Fidel.termsConditionsURL = null;
            }
        }
        if (valueIsValidFor(data, META_DATA_KEY)) {
            try {
                JSONObject metaDataMap = data.getJSONObject(META_DATA_KEY);
                if (metaDataMap != null) {
                    Fidel.metaData = metaDataMap;
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                Fidel.metaData = null;
            }
        }
        if (valueIsValidFor(data, COUNTRY_KEY)) {
            try {
                int countryInt = data.getInt(COUNTRY_KEY);
                Fidel.country = countryAdapter.countryWithInteger(countryInt);
            }
            catch (JSONException e) {
                Fidel.country = null;
            }
        }
        if (data.has(CARD_SCHEMES_KEY)) {
            try {
                Fidel.supportedCardSchemes = cardSchemesAdapter.cardSchemesWithReadableArray(data.getJSONArray(CARD_SCHEMES_KEY));
            }
            catch (JSONException e) {
                Fidel.supportedCardSchemes = EnumSet.allOf(Fidel.CardScheme.class);
            }
        }
    }

//    private JSONObject getJSONWithMap(ReadableMap metaDataMap) {
//        return new JSONObject(metaDataMap.toHashMap());
//    }

    private boolean valueIsValidFor(JSONObject map, String key) {
        try {
            return (map.has(key) && !map.isNull(key) && !map.get(key).equals(""));
        }
        catch (JSONException e) {
            return false;
        }
    }

    @Override
    public void output(Bitmap data) {
        Fidel.bannerImage = data;
    }


    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> constants = countryAdapter.getConstants();
        constants.putAll(cardSchemesAdapter.getConstants());
        return constants;
    }
}
