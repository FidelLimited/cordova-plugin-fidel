package com.fidelcordovalibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.FidelOptionsAdapter;
import com.fidelcordovalibrary.fakes.CardSchemeAdapterStub;
import com.fidelcordovalibrary.fakes.CountryAdapterStub;
import com.fidelcordovalibrary.fakes.JSONObjectStub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


//Custom test runner is necessary for being able to use JSONObject
@RunWith(RobolectricTestRunner.class)
public class FidelOptionsAdapterTests {

    private JSONObject jsonObject;
    private CountryAdapterStub countryAdapterStub = new CountryAdapterStub();
    private CardSchemeAdapterStub cardSchemesAdapterStub = new CardSchemeAdapterStub();
    private FidelOptionsAdapter sut;
    private Context context;

    private static final String TEST_COMPANY_NAME = "Test Company Name Inc.";
    private static final String TEST_PROGRAM_NAME = "Test Program Name";
    private static final String TEST_DELETE_INSTRUCTIONS = "Test Delete instructions.";
    private static final String TEST_PRIVACY_URL = "testprivacy.url";
    private static final String TEST_TERMS_CONDITIONS_URL = "termsconditions.url";


    @Before
    public final void setUp() {
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();
        sut  = new FidelOptionsAdapter(countryAdapterStub, cardSchemesAdapterStub,activity.getApplicationContext());
        context = activity.getApplicationContext();
    }

    @After
    public final void tearDown() {
        sut = null;
        Fidel.bannerImage = null;
        Fidel.autoScan = false;
        Fidel.companyName = null;
        Fidel.programName = null;
        Fidel.deleteInstructions = null;
        Fidel.privacyURL = null;
        Fidel.termsConditionsURL = null;
        Fidel.metaData = null;
        Fidel.allowedCountries = null;
        Fidel.supportedCardSchemes = EnumSet.allOf(Fidel.CardScheme.class);
    }

    //Verification values tests
    @Test
    public void test_ChecksForSupportedCardSchemes() {
        JSONObject jsonObject = JSONObjectStub.JSONObjectWithExistingKey(FidelOptionsAdapter.CARD_SCHEMES_KEY);
        sut.process(jsonObject);
        assertTrue(jsonObject.has(FidelOptionsAdapter.CARD_SCHEMES_KEY));
    }

    //Tests when keys are present, but no data is found for that key
    @Test
    public void test_IfHasAutoScanKeyButNoValue_DontSetItToTheSDK() {
        jsonObject = JSONObjectStub.JSONObjectWithExistingKeyButNoValue(FidelOptionsAdapter.AUTO_SCAN_KEY);
        sut.process(jsonObject);
        assertFalse(Fidel.autoScan);
    }

    @Test
    public void test_IfHasCompanyNameKeyButNoValue_DontSetItToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.COMPANY_NAME_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithExistingKeyButNoValue(keyToTestFor);
        processWithString(keyToTestFor, "");
        assertNotEqualsString(keyToTestFor, Fidel.companyName);
    }

    @Test
    public void test_IfHasProgramNameKeyButNoValue_DontSetItToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.PROGRAM_NAME_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithExistingKeyButNoValue(keyToTestFor);
        processWithString(keyToTestFor, "");
        assertNotEqualsString(keyToTestFor, Fidel.programName);
    }

    @Test
    public void test_IfHasDeleteInstructionsKeyButNoValue_DontSetThemToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.DELETE_INSTRUCTIONS_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithExistingKeyButNoValue(keyToTestFor);
        processWithString(keyToTestFor, "");
        assertNotEqualsString(keyToTestFor, Fidel.deleteInstructions);
    }

    @Test
    public void test_IfHasPrivacyURLKeyButNoValue_DontSetItToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.PRIVACY_URL_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithExistingKeyButNoValue(keyToTestFor);
        processWithString(keyToTestFor, "");
        assertNotEqualsString(keyToTestFor, Fidel.privacyURL);
    }

    @Test
    public void test_IfHasTermsConditionsURLKeyButNoValue_DontSetItToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.TERMS_CONDITIONS_URL_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithExistingKeyButNoValue(keyToTestFor);
        processWithString(keyToTestFor, "");
        assertNotEqualsString(keyToTestFor, Fidel.termsConditionsURL);
    }

    @Test
    public void test_IfHasMetaDataKeyButNoValue_DontSetItToTheSDK() {
        JSONObject mainJson = new JSONObject();
        JSONObject nestedJson = new JSONObject();
        try {
            nestedJson.put("key1","");
            nestedJson.put("key2","my-new-key");
            mainJson.put("metaData", nestedJson);
        }
        catch (JSONException e) {
            fail("Test failed with erro " + e.getLocalizedMessage());
        }
        sut.process(mainJson);
        assertNull(Fidel.metaData);
    }

    @Test
    public void test_IfHasCountryKeyButNoValue_DontSetItToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.ALLOWED_COUNTRIES_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithExistingKeyButNoValue(keyToTestFor);
        assertNull(Fidel.allowedCountries);
    }

    //Tests when keys are missing
    @Test
    public void test_IfDoesntHaveAutoScanKey_DontSetItToTheSDK() {
        jsonObject = JSONObjectStub.JSONObjectWithNoKey();
        sut.process(jsonObject);
        assertFalse(Fidel.autoScan);
    }

    @Test
    public void test_IfDoesntHaveCompanyNameKey_DontSetItToTheSDK() {
        jsonObject = JSONObjectStub.JSONObjectWithNoKey();
        String key = FidelOptionsAdapter.COMPANY_NAME_KEY;
        processWithString(TEST_COMPANY_NAME, key);
        assertNotEqualsString(key, Fidel.companyName);
    }

    @Test
    public void test_IfDoesntHaveProgramNameKey_DontSetItToTheSDK() {
        jsonObject = JSONObjectStub.JSONObjectWithNoKey();
        String key = FidelOptionsAdapter.PROGRAM_NAME_KEY;
        processWithString(TEST_PROGRAM_NAME, key);
        assertNotEqualsString(key, Fidel.programName);
    }

    @Test
    public void test_IfDoesntHaveDeleteInstructionsKey_DontSetThemToTheSDK() {
        jsonObject = JSONObjectStub.JSONObjectWithNoKey();
        String key = FidelOptionsAdapter.DELETE_INSTRUCTIONS_KEY;
        processWithString(TEST_DELETE_INSTRUCTIONS, key);
        assertNotEqualsString(key, Fidel.deleteInstructions);
    }

    @Test
    public void test_IfDoesntHavePrivacyURLKey_DontSetItToTheSDK() {
        jsonObject = JSONObjectStub.JSONObjectWithNoKey();
        String key = FidelOptionsAdapter.PRIVACY_URL_KEY;
        processWithString(TEST_PRIVACY_URL, key);
        assertNotEqualsString(key, Fidel.privacyURL);
    }

    @Test
    public void test_IfDoesntHaveTermsConditionsURLKey_DontSetItToTheSDK() {
        jsonObject = JSONObjectStub.JSONObjectWithNoKey();
        String key = FidelOptionsAdapter.TERMS_CONDITIONS_URL_KEY;
        processWithString(TEST_TERMS_CONDITIONS_URL, key);
        assertNotEqualsString(key, Fidel.termsConditionsURL);
    }

    @Test
    public void test_IfDoesntHaveMetaDataKey_DontSetItToTheSDK() {
        String key = FidelOptionsAdapter.META_DATA_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithNoKey();
        processWithJSONObject(key, TEST_META_DATA(FidelOptionsAdapter.META_DATA_KEY));
        assertNull(Fidel.metaData);
    }

    @Test
    public void test_IfDoesntHaveCountryKey_DontSetItToTheSDK() {
        jsonObject = JSONObjectStub.JSONObjectWithKeyAndValue("random","somevalue");
        sut.process(jsonObject);
        assertNull(Fidel.allowedCountries);
    }
    @Test
    public void test_IfDoesntHaveCardSchemeKey_DontSetItToTheSDK() {
        jsonObject = JSONObjectStub.JSONObjectWithNoKey();
        sut.process(jsonObject);
        assertEquals(EnumSet.allOf(Fidel.CardScheme.class), Fidel.supportedCardSchemes);
    }

    //Setting correct values tests
    @Test
    public void test_WhenBannerImageNameIsSet_SetSDKBannerImage() {
        Bitmap newBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.fl_banner);
        jsonObject = JSONObjectStub.JSONObjectWithKeyAndValue(FidelOptionsAdapter.BANNER_IMAGE_KEY, "fl_banner");
        sut.process(jsonObject);
        boolean isSameBitmap = newBitmap.sameAs(Fidel.bannerImage);
        assertTrue(isSameBitmap);
    }

    @Test
    public void test_WhenBannerImageNameIsEmpty_DoNotSetSDKBannerImage() {
        jsonObject = JSONObjectStub.JSONObjectWithKeyAndValue(FidelOptionsAdapter.BANNER_IMAGE_KEY, "");
        sut.process(jsonObject);
        assertNull(Fidel.bannerImage);
    }

    @Test
    public void test_WhenBannerImageNameIsNull_DoNotSetSDKBannerImage() {
        jsonObject = JSONObjectStub.JSONObjectWithKeyAndValue(FidelOptionsAdapter.BANNER_IMAGE_KEY, null);
        sut.process(jsonObject);
        assertNull(Fidel.bannerImage);
    }

    @Test
    public void test_WhenBannerImageNameIsMissing_DoNotSetSDKBannerImage() {
        jsonObject = JSONObjectStub.JSONObjectWithNoKey();
        sut.process(jsonObject);
        assertNull(Fidel.bannerImage);
    }

    @Test
    public void test_WhenAutoScanValueIsTrue_SetItTrueForTheSDK() {
        processWithBoolean(true);
        assertTrue(Fidel.autoScan);
    }

    @Test
    public void test_WhenAutoScanValueIsFalse_SetItFalseForTheSDK() {
        processWithBoolean(false);
        assertFalse(Fidel.autoScan);
    }

    @Test
    public void test_WhenCompanyNameValueIsSet_SetItForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.COMPANY_NAME_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithExistingKey(keyToTestFor);
        processWithString(keyToTestFor, TEST_COMPANY_NAME);
        assertEqualsString(keyToTestFor, Fidel.companyName);
    }

    @Test
    public void test_WhenProgramNameValueIsSet_SetItForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.PROGRAM_NAME_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithExistingKey(keyToTestFor);
        processWithString(keyToTestFor, TEST_PROGRAM_NAME);
        assertEqualsString(keyToTestFor, Fidel.programName);
    }

    @Test
    public void test_WhenDeleteInstructionsValueIsSet_SetItForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.DELETE_INSTRUCTIONS_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithExistingKey(keyToTestFor);
        processWithString(keyToTestFor, TEST_DELETE_INSTRUCTIONS);
        assertEqualsString(keyToTestFor, Fidel.deleteInstructions);
    }

    @Test
    public void test_WhenPrivacyURLValueIsSet_SetItForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.PRIVACY_URL_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithExistingKey(keyToTestFor);
        processWithString(keyToTestFor, TEST_PRIVACY_URL);
        assertEqualsString(keyToTestFor, Fidel.privacyURL);
    }

    @Test
    public void test_WhenTermsConditionsURLValueIsSet_SetItForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.TERMS_CONDITIONS_URL_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithExistingKey(keyToTestFor);
        processWithString(keyToTestFor, TEST_TERMS_CONDITIONS_URL);
        assertEqualsString(keyToTestFor, Fidel.termsConditionsURL);
    }

    @Test
    public void test_WhenCardSchemesAreSet_ConvertThemWithCardSchemeAdapterForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.CARD_SCHEMES_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithExistingKey(keyToTestFor);
        processWithCardSchemes(Fidel.CardScheme.VISA, Fidel.CardScheme.MASTERCARD);
        try {
            assertEquals(jsonObject.getJSONArray(FidelOptionsAdapter.CARD_SCHEMES_KEY), cardSchemesAdapterStub.cardSchemesReceived);
        }
        catch (JSONException e) {
            fail("Test failed with error " + e.getLocalizedMessage());
        }

    }

    @Test
    public void test_WhenCardSchemesAreSet_SetThemForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.CARD_SCHEMES_KEY;
        jsonObject = JSONObjectStub.JSONObjectWithExistingKey(keyToTestFor);
        Fidel.CardScheme[] expectedSchemes = {Fidel.CardScheme.VISA, Fidel.CardScheme.MASTERCARD};
        Set<Fidel.CardScheme> expectedSchemesSet = EnumSet.copyOf(Arrays.asList(expectedSchemes));
        processWithCardSchemes(expectedSchemes);
        assertEquals(expectedSchemesSet, Fidel.supportedCardSchemes);
    }

    //Helper functions
    private static HashMap<String, Object> TEST_HASH_MAP() {
        HashMap<String, Object> hashmapToReturn = new HashMap<>();
        hashmapToReturn.put("stringKey", "StringVal");
        hashmapToReturn.put("intKey", 3);
        hashmapToReturn.put("doubleKey", 4.5);
        return hashmapToReturn;
    }

    private JSONObject TEST_META_DATA(String key) {
        try {
            jsonObject.put(key, TEST_HASH_MAP());
            return jsonObject;
        }
        catch(Exception e) {
            fail("Test failed with error " + e.getLocalizedMessage());
            return jsonObject;
        }
    }

    private void processWithBoolean(Boolean bool) {
        jsonObject = JSONObjectStub.JSONObjectWithExistingKey(FidelOptionsAdapter.AUTO_SCAN_KEY);
        try {
            jsonObject.put(FidelOptionsAdapter.AUTO_SCAN_KEY, bool);
            sut.process(jsonObject);
        }
        catch (JSONException e) {
            fail("Test failed with message " + e.getLocalizedMessage());
        }

    }

    private void processWithString(String key, String value) {
        try {
            jsonObject.put(key, value);
            sut.process(jsonObject);
        }
        catch (JSONException e) {
            e.printStackTrace();
            fail("Test failed with error " + e.getLocalizedMessage());
        }
    }

    private void processWithJSONObject(String key, JSONObject objectToReturn) {
        try {
            jsonObject.put(key, objectToReturn);
            sut.process(jsonObject);
        }
        catch (JSONException e) {
            fail("Test failed with error " + e.getLocalizedMessage());
        }
    }

    private void processWithCardSchemes(Fidel.CardScheme... cardSchemes) {
        cardSchemesAdapterStub.fakeAdaptedCardSchemesToReturn = EnumSet.copyOf(Arrays.asList(cardSchemes));
        JSONArray ja = new JSONArray();
        ja.put(cardSchemes);
        try {
            jsonObject.put(FidelOptionsAdapter.CARD_SCHEMES_KEY, ja);
            sut.process(jsonObject);
        }
        catch (JSONException e) {
            fail("Test failed with message " + e.getLocalizedMessage());
        }
    }

    private void assertEqualsString(String key, String valueToCheckWith) {
        sut.process(jsonObject);
        try {
            assertEquals(jsonObject.get(key), valueToCheckWith);
        }
        catch (JSONException e) {
            fail("Test failed with error " + e.getLocalizedMessage());
        }

    }

    private void assertNotEqualsString(String key, String valueToCheckWith) {
        sut.process(jsonObject);
        try {
            if (jsonObject.has(key)) {
                assertNotEquals(jsonObject.get(key), valueToCheckWith);
            }
        }
        catch (JSONException e) {
            fail("Test failed with error " + e.getLocalizedMessage());
        }
    }
}
