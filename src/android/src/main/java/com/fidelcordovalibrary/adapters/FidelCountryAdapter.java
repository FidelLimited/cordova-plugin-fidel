package com.fidelcordovalibrary.adapters;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.abstraction.CountryAdapter;

import java.util.HashMap;
import java.util.Map;

public final class FidelCountryAdapter implements CountryAdapter {

    @Override
    public Fidel.Country countryWithInteger(int integer) {
        if (integer < Fidel.Country.values().length) {
            return Fidel.Country.values()[integer];
        }
        return null;
    }
}
