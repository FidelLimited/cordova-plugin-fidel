package com.fidelcordovalibrary.adapters.abstraction;

import com.fidel.sdk.Fidel;

import org.json.JSONArray;

import java.util.Set;

public interface CardSchemesAdapter{
    Set<Fidel.CardScheme> cardSchemesWithJSONArray(JSONArray cardSchemes);
}
