package com.fidelcordovalibrary.fakes;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.abstraction.CardSchemesAdapter;

import org.json.JSONArray;

import java.util.Set;

public final class CardSchemeAdapterStub implements CardSchemesAdapter {

    public JSONArray cardSchemesReceived;
    public Set<Fidel.CardScheme> fakeAdaptedCardSchemesToReturn;

    @Override
    public Set<Fidel.CardScheme> cardSchemesWithJSONArray(JSONArray cardSchemes) {
        cardSchemesReceived = cardSchemes;
        return fakeAdaptedCardSchemesToReturn;
    }

}
