package com.fidelcordovalibrary.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.abstraction.CardSchemesAdapter;
import com.fidelcordovalibrary.adapters.abstraction.CountryAdapter;
import com.fidelcordovalibrary.adapters.abstraction.DataProcessor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.Iterator;

public final class FidelOptionsAdapter implements DataProcessor<JSONObject> {

    public static final String BANNER_IMAGE_KEY = "bannerImageName";
    public static final String AUTO_SCAN_KEY = "autoScan";
    public static final String COMPANY_NAME_KEY = "companyName";
    public static final String PROGRAM_NAME_KEY = "programName";
    public static final String DELETE_INSTRUCTIONS_KEY = "deleteInstructions";
    public static final String PRIVACY_URL_KEY = "privacyUrl";
    public static final String TERMS_CONDITIONS_URL_KEY = "termsConditionsUrl";
    public static final String META_DATA_KEY = "metaData";
    public static final String ALLOWED_COUNTRIES_KEY = "allowedCountries";
    public static final String CARD_SCHEMES_KEY = "supportedCardSchemes";

    private final CountryAdapter countryAdapter;
    private final CardSchemesAdapter cardSchemesAdapter;
    private Context context;

    public FidelOptionsAdapter(CountryAdapter countryAdapter,
                               CardSchemesAdapter cardSchemesAdapter,
                               Context context) {
        this.countryAdapter = countryAdapter;
        this.cardSchemesAdapter = cardSchemesAdapter;
        this.context = context;
    }

    @Override
    public void process(JSONObject data) {
        if (valueIsValidFor(data, BANNER_IMAGE_KEY)) {
            try {
                String imageToDisplay = data.getString(BANNER_IMAGE_KEY);
                String uri = "@drawable/" + imageToDisplay;
                int imageResource = this.context.getResources().getIdentifier(uri, null, this.context.getPackageName());
                Fidel.bannerImage = BitmapFactory.decodeResource(this.context.getResources(),
                        imageResource);
            }
            catch (JSONException e) {
                Fidel.bannerImage = null;
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
                JSONObject json = data.getJSONObject(META_DATA_KEY);
                if (json != null) {
                    Fidel.metaData = json;
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                Fidel.metaData = null;
            }
        }
        if (valueIsValidFor(data, ALLOWED_COUNTRIES_KEY)) {
            try {
                Fidel.allowedCountries = countryAdapter.parseAllowedCountries(data.getJSONArray(ALLOWED_COUNTRIES_KEY));
            }
            catch (JSONException e) {
                Fidel.allowedCountries = new Fidel.Country[]{Fidel.Country.UNITED_KINGDOM, Fidel.Country.IRELAND, Fidel.Country.UNITED_STATES, Fidel.Country.SWEDEN, Fidel.Country.JAPAN, Fidel.Country.CANADA};
            }
        }
        if (data.has(CARD_SCHEMES_KEY)) {
            try {
                Fidel.supportedCardSchemes = cardSchemesAdapter.cardSchemesWithJSONArray(data.getJSONArray(CARD_SCHEMES_KEY));
            }
            catch (JSONException e) {
                Fidel.supportedCardSchemes = EnumSet.allOf(Fidel.CardScheme.class);
            }
        }
    }

    private boolean valueIsValidFor(JSONObject inputJsonObject, String key) {
        try {
            Object value = inputJsonObject.get(key);
            if (value.getClass() == JSONObject.class) {
                JSONObject result = (JSONObject) value;
                Iterator<String> jsonKeyIterator = result.keys();
                while (jsonKeyIterator.hasNext()) {
                    String nestedKey = jsonKeyIterator.next();
                    Object nestedValue = result.get(nestedKey);
                    return (nestedValue != null && !nestedValue.equals(""));
                }
            }
            return (inputJsonObject.has(key) && !inputJsonObject.isNull(key) && !inputJsonObject.get(key).equals(""));
        }
        catch (JSONException e) {
            return false;
        }
    }

}
