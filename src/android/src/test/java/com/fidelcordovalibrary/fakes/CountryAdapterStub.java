package com.fidelcordovalibrary.fakes;

import com.fidel.sdk.Fidel;
import com.fidel.sdk.Fidel.Country;
import com.fidelcordovalibrary.adapters.abstraction.CountryAdapter;

import org.json.JSONArray;

public class CountryAdapterStub implements CountryAdapter {

    @Override
    public Country countryWithInteger(int integer) {
        return null;
    }

    @Override
    public Country[] parseAllowedCountries(JSONArray inputArray) {
        return new Fidel.Country[]{Fidel.Country.UNITED_KINGDOM, Fidel.Country.JAPAN, Fidel.Country.CANADA};
    }
}
