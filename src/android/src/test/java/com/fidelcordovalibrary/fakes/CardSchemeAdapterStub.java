package com.fidelcordovalibrary.fakes;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.abstraction.CardSchemesAdapter;

import org.json.JSONArray;

import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

public final class CardSchemeAdapterStub implements CardSchemesAdapter {

    public JSONArray cardSchemesReceived;
    public Set<Fidel.CardScheme> fakeAdaptedCardSchemesToReturn;

    @Override
    public Set<Fidel.CardScheme> cardSchemesWithReadableArray(JSONArray cardSchemes) {
        cardSchemesReceived = cardSchemes;
        return fakeAdaptedCardSchemesToReturn;
    }

    @Nonnull
    @Override
    public Map<String, Object> getConstants() {
        return new ConstantsProviderStub("testKeyCardSchemeAdapter", 234).getConstants();
    }
}
