package com.fidelcordovalibrary;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.FidelSetupAdapter;
import com.fidelcordovalibrary.fakes.JSONObjectStub;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class FidelSetupAdapterTests {

    private static final String TEST_API_KEY = "pk_123123123";
    private static final String TEST_PROGRAM_ID = "234234";

    private FidelSetupAdapter sut;

    @Before
    public final void setUp() {
        sut = new FidelSetupAdapter();
    }

    @After
    public final void tearDown() {
        sut = null;
        Fidel.apiKey = null;
        Fidel.programId = null;
    }

    @Test
    public void test_WhenDataHasNoApiKey_DontSetItToSDK() {
        JSONObject jsonObjectStub = JSONObjectStub.JSONObjectWithNoKey();
        sut.process(jsonObjectStub);
        assertNull(Fidel.apiKey);
    }

    @Test
    public void test_WhenDataHasNoProgramIDKey_DontSetItToSDK() {
        JSONObject jsonObjectStub = JSONObjectStub.JSONObjectWithNoKey();
        sut.process(jsonObjectStub);
        assertNull(Fidel.programId);
    }

    @Test
    public void test_WhenApiKeyIsSet_SetItToSDK() {
        String expectedValue = TEST_API_KEY;
        processWithString(FidelSetupAdapter.API_KEY, expectedValue);
        assertEquals(expectedValue, Fidel.apiKey);
    }

    @Test
    public void test_WhenProgramIDIsSet_SetItToSDK() {
        String expectedValue = TEST_PROGRAM_ID;
        processWithString(FidelSetupAdapter.PROGRAM_ID_KEY, expectedValue);
        assertEquals(expectedValue, Fidel.programId);
    }

    private void processWithString(String key, String value) {
        JSONObject jsonObjectStub = new JSONObject();
        try {
            jsonObjectStub.put(key, value);
            sut.process(jsonObjectStub);
        }
        catch (JSONException e) {
            e.printStackTrace();
            fail("Test failed with error " + e.getLocalizedMessage());
        }
    }
}
