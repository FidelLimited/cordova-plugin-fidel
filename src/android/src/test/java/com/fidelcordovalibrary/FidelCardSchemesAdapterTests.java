package com.fidelcordovalibrary;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.FidelCardSchemesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import edu.emory.mathcs.backport.java.util.Arrays;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public final class FidelCardSchemesAdapterTests {
    private static final String CARD_SCHEMES_KEY = "CardScheme";
    private static final String VISA_CARD_SCHEME_KEY = "visa";
    private static final String MASTERCARD_SCHEME_KEY = "mastercard";
    private static final String AMEX_SCHEME_KEY = "americanExpress";

    private FidelCardSchemesAdapter sut = new FidelCardSchemesAdapter();

    //Adaptation tests
    @Test
    public void test_WhenAdaptingArrayWithVisa_ReturnSetWithVisaCardScheme() {
        assertCorrectConversionWithSchemes(Fidel.CardScheme.VISA);
    }

    @Test
    public void test_WhenAdaptingArrayWithMastercard_ReturnSetWithMastercardCardScheme() {
        assertCorrectConversionWithSchemes(Fidel.CardScheme.MASTERCARD);
    }

    @Test
    public void test_WhenAdaptingArrayWithAmericanExpress_ReturnSetWithAmericanExpressCardScheme() {
        assertCorrectConversionWithSchemes(Fidel.CardScheme.AMERICAN_EXPRESS);
    }

    @Test
    public void test_WhenAdaptingArrayWith2Schemes_ReturnSetWith2Schemes() {
        Fidel.CardScheme[] expectedSchemes = {
                Fidel.CardScheme.VISA,
                Fidel.CardScheme.AMERICAN_EXPRESS
        };
        assertCorrectConversionWithSchemes(expectedSchemes);
    }

    @Test
    public void test_WhenAdaptingArrayWith3Schemes_ReturnSetWith2Schemes() {
        Fidel.CardScheme[] expectedSchemes = {
                Fidel.CardScheme.VISA,
                Fidel.CardScheme.MASTERCARD,
                Fidel.CardScheme.AMERICAN_EXPRESS
        };
        assertCorrectConversionWithSchemes(expectedSchemes);
    }

    @Test
    public void test_WhenAdapting0Schemes_ReturnEmptySet() {
        Set<Fidel.CardScheme> result = sut.cardSchemesWithJSONArray(new JSONArray());
        assertEquals(0, result.size());
    }

    @Test
    public void test_WhenAdaptingNullSchemes_ReturnNullSet() {
        assertNull(sut.cardSchemesWithJSONArray(null));
    }

    @Test
    public void test_WhenAdaptingSchemeListWithInvalidValues_IgnoreTheInvalidValues() {
        try {
            float invalidValue = (float) Fidel.CardScheme.values().length + 10;
            JSONArray invalidDoubleValues = new JSONArray();
            invalidDoubleValues.put(invalidValue);
            invalidDoubleValues.put(Fidel.CardScheme.VISA.ordinal());
            Set<Fidel.CardScheme> result = sut.cardSchemesWithJSONArray(invalidDoubleValues);
            assertEquals(EnumSet.of(Fidel.CardScheme.VISA), result);
        }
        catch (JSONException e) {
            fail("Test failed with error " + e.getLocalizedMessage());
        }
    }

    @Test
    public void test_WhenAdaptingSchemeListWithValidFloatValue_AdaptValidFloatValue() {
        try {
            double validDoubleValue = (double) Fidel.CardScheme.MASTERCARD.ordinal();
            JSONArray invalidDoubleValues = new JSONArray();
            invalidDoubleValues.put(validDoubleValue);
            invalidDoubleValues.put(Fidel.CardScheme.VISA.ordinal());
            Set<Fidel.CardScheme> result = sut.cardSchemesWithJSONArray(invalidDoubleValues);
            assertEquals(EnumSet.of(Fidel.CardScheme.VISA, Fidel.CardScheme.MASTERCARD), result);
        }
        catch (JSONException e) {
            fail("Test failed with error " + e.getLocalizedMessage());
        }
    }

    //Helpers
    private void assertCorrectConversionWithSchemes(Fidel.CardScheme... schemes) {
        Integer[] schemeOrdinals = new Integer[schemes.length];
        JSONArray arrayStub = new JSONArray();
        int schemeNumber = 0;
        for (Fidel.CardScheme scheme :
                schemes) {
            schemeOrdinals[schemeNumber] = scheme.ordinal();
            arrayStub.put((Object) schemeOrdinals[schemeNumber]);
            schemeNumber++;
        }
        Set<Fidel.CardScheme> result = sut.cardSchemesWithJSONArray(arrayStub);
        assertEquals(EnumSet.copyOf(Arrays.asList(schemes)), result);
    }
}
