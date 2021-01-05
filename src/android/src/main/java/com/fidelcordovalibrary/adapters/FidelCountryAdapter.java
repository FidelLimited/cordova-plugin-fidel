package com.fidelcordovalibrary.adapters;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.abstraction.CountryAdapter;

import org.json.JSONArray;
import org.json.JSONException;

public class FidelCountryAdapter implements CountryAdapter {
    @Override
    public Fidel.Country countryWithInteger(int integer) {
        if (integer < Fidel.Country.values().length) {
            return Fidel.Country.values()[integer];
        }
        return null;
    }

    @Override
    public Fidel.Country[] parseAllowedCountries(JSONArray inputArray) {
        try {
            Fidel.Country[] countries = new Fidel.Country[inputArray.length()];
            for (int i = 0; i < inputArray.length(); i++) {
                countries[i] = countryWithInteger(inputArray.getInt(i));
            }
            return countries;
        }
        catch (JSONException e) {
            return new Fidel.Country[]{Fidel.Country.UNITED_KINGDOM, Fidel.Country.IRELAND, Fidel.Country.UNITED_STATES, Fidel.Country.SWEDEN, Fidel.Country.JAPAN, Fidel.Country.CANADA};

        }

    }
}
