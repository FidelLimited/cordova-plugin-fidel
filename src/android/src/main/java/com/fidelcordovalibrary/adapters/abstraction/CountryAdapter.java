package com.fidelcordovalibrary.adapters.abstraction;

import com.fidel.sdk.Fidel;

import org.json.JSONArray;

public interface CountryAdapter {
    Fidel.Country countryWithInteger(int integer);
    Fidel.Country[] parseAllowedCountries(JSONArray inputArray);
}
