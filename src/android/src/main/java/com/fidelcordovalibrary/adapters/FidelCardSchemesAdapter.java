package com.fidelcordovalibrary.adapters;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.abstraction.CardSchemesAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class FidelCardSchemesAdapter implements CardSchemesAdapter {

    @Override
    public Set<Fidel.CardScheme> cardSchemesWithJSONArray(JSONArray arrayToAdapt) {
        if (arrayToAdapt == null) {
            return null;
        }

        ArrayList<Integer> integerArrayToAdapt = new ArrayList<>();
        ArrayList<Object> arrayToAdaptObjects = new ArrayList<Object>();
        for (int i= 0; i < arrayToAdapt.length(); i++) {
            try {
                arrayToAdaptObjects.add(arrayToAdapt.get(i));
            }
            catch (JSONException e) {
                return null;
            }
        }
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
