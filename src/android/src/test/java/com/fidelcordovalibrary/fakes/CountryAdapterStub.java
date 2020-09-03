package com.fidelcordovalibrary.fakes;

import com.fidel.sdk.Fidel.Country;
import com.fidelcordovalibrary.adapters.abstraction.CountryAdapter;

import java.util.Map;

import javax.annotation.Nonnull;

public final class CountryAdapterStub implements CountryAdapter {

    public Country countryToReturn;
    public int countryIntegerReceived;

    @Override
    public Country countryWithInteger(int integer) {
        countryIntegerReceived = integer;
        return countryToReturn;
    }
}
