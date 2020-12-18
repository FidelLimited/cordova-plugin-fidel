package com.fidelcordovalibrary;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.FidelCountryAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FidelCountryAdapterTests {
    private FidelCountryAdapter sut;

    @Before
    public final void setUp() {
        sut = new FidelCountryAdapter();
    }

    @After
    public final void tearDown() {
        sut = null;
    }

    @Test
    public void test_WhenCountryOrdinalNumberIsTooHigh_ReturnNullCountry() {
        assertNull(sut.countryWithInteger(1239));
    }

    @Test
    public void test_WhenCountryOrdinalNumberIsValid_ReturnCountryFromThatOrdinalNumber() {
        Fidel.Country[] countries = Fidel.Country.values();
        for (int countryIndex = 0; countryIndex < countries.length; countryIndex++) {
            assertEquals(sut.countryWithInteger(countryIndex), countries[countryIndex]);
        }
    }



}
