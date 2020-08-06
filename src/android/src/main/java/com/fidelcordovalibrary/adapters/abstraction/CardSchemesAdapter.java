package com.fidelcordovalibrary.adapters.abstraction;

import com.fidel.sdk.Fidel;

import org.json.JSONArray;

import java.util.Set;

public interface CardSchemesAdapter extends ConstantsProvider {
    Set<Fidel.CardScheme> cardSchemesWithReadableArray(JSONArray cardSchemes);
}
