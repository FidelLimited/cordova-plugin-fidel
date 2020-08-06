package com.fidelcordovalibrary.adapters;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.abstraction.CardSchemesAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class FidelCardSchemesAdapter implements CardSchemesAdapter {

    private static final String EXPORTED_CARD_SCHEME_KEY = "CardScheme";

    private static final String VISA_CARD_SCHEME_VALUE = "visa";
    private static final String MASTERCARD_SCHEME_VALUE = "mastercard";
    private static final String AMERICAN_EXPRESS_CARD_SCHEME_VALUE = "americanExpress";

    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        Map<String, Object> cardSchemesConstants = new HashMap<>();
        for (Fidel.CardScheme cardScheme : Fidel.CardScheme.values()) {
            String cardSchemeKey = null;
            switch (cardScheme) {
                case VISA: cardSchemeKey = VISA_CARD_SCHEME_VALUE; break;
                case MASTERCARD: cardSchemeKey = MASTERCARD_SCHEME_VALUE; break;
                case AMERICAN_EXPRESS: cardSchemeKey = AMERICAN_EXPRESS_CARD_SCHEME_VALUE; break;
            }
            cardSchemesConstants.put(cardSchemeKey, cardScheme.ordinal());
        }
        constants.put(EXPORTED_CARD_SCHEME_KEY, cardSchemesConstants);
        return constants;
    }

    @Override
    public Set<Fidel.CardScheme> cardSchemesWithReadableArray(JSONArray arrayToAdapt) {
        if (arrayToAdapt == null) {
            return null;
        }
        ArrayList<Integer> integerArrayToAdapt = new ArrayList<>();
        ArrayList<Object> arrayToAdaptObjects = new Gson().fromJson(arrayToAdapt.toString(),
                new TypeToken<List<Object>>(){}.getType());
        for (Object objectToAdapt: arrayToAdaptObjects) {
            if (objectToAdapt.getClass() == Integer.class) {
                integerArrayToAdapt.add((Integer)objectToAdapt);
            }
            else if (objectToAdapt.getClass() == Double.class) {
                Double doubleObjectToAdapt = (Double)objectToAdapt;
                integerArrayToAdapt.add(doubleObjectToAdapt.intValue());
            }
        }
        Set<Integer> receivedObjectsSet = new HashSet<>(integerArrayToAdapt);
        Set<Fidel.CardScheme> cardSchemeSet = new HashSet<>();
        for (Fidel.CardScheme scheme : Fidel.CardScheme.values()) {
            if (receivedObjectsSet.contains(scheme.ordinal())) {
                cardSchemeSet.add(scheme);
            }
        }
        return cardSchemeSet;
    }
}
