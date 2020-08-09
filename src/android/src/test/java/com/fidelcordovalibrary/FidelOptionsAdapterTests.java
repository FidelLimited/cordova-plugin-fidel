package com.fidelcordovalibrary;

import android.graphics.Bitmap;

import com.fidel.sdk.Fidel;
import com.fidelcordovalibrary.adapters.FidelOptionsAdapter;
import com.fidelcordovalibrary.fakes.CardSchemeAdapterStub;
import com.fidelcordovalibrary.fakes.CountryAdapterStub;
import com.fidelcordovalibrary.fakes.DataProcessorSpy;
import com.fidelcordovalibrary.fakes.JSONObjectStub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.fidelcordovalibrary.helpers.AssertHelpers.assertMapContainsMap;
import static com.fidelcordovalibrary.helpers.AssertHelpers.assertMapEqualsWithJSONObject;
import static junit.framework.Assert.fail;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


//Custom test runner is necessary for being able to use JSONObject
@RunWith(RobolectricTestRunner.class)
public class FidelOptionsAdapterTests {

    private DataProcessorSpy<JSONObject> imageAdapterSpy = new DataProcessorSpy<>();
    private JSONObject map;
    private CountryAdapterStub countryAdapterStub = new CountryAdapterStub();
    private CardSchemeAdapterStub cardSchemesAdapterStub = new CardSchemeAdapterStub();
    private FidelOptionsAdapter sut = new FidelOptionsAdapter(imageAdapterSpy, countryAdapterStub, cardSchemesAdapterStub);

    private static final String TEST_COMPANY_NAME = "Test Company Name Inc.";
    private static final String TEST_PROGRAM_NAME = "Test Program Name";
    private static final String TEST_DELETE_INSTRUCTIONS = "Test Delete instructions.";
    private static final String TEST_PRIVACY_URL = "testprivacy.url";
    private static final String TEST_TERMS_CONDITIONS_URL = "termsconditions.url";
    private static final Fidel.Country TEST_COUNTRY = Fidel.Country.SWEDEN;
    private static final Integer TEST_COUNTRY_NUMBER = 12;

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
        Fidel.country = null;
        Fidel.supportedCardSchemes = EnumSet.allOf(Fidel.CardScheme.class);
    }

    //Verification values tests
    @Test
    public void test_ChecksAllKeys() {
        assertThat(FidelOptionsAdapter.OPTION_KEYS, hasItem(FidelOptionsAdapter.BANNER_IMAGE_KEY));
        assertThat(FidelOptionsAdapter.OPTION_KEYS, hasItem(FidelOptionsAdapter.AUTO_SCAN_KEY));
        assertThat(FidelOptionsAdapter.OPTION_KEYS, hasItem(FidelOptionsAdapter.COMPANY_NAME_KEY));
        assertThat(FidelOptionsAdapter.OPTION_KEYS, hasItem(FidelOptionsAdapter.PROGRAM_NAME_KEY));
        assertThat(FidelOptionsAdapter.OPTION_KEYS, hasItem(FidelOptionsAdapter.DELETE_INSTRUCTIONS_KEY));
        assertThat(FidelOptionsAdapter.OPTION_KEYS, hasItem(FidelOptionsAdapter.PRIVACY_URL_KEY));
        assertThat(FidelOptionsAdapter.OPTION_KEYS, hasItem(FidelOptionsAdapter.TERMS_CONDITIONS_URL_KEY));
        assertThat(FidelOptionsAdapter.OPTION_KEYS, hasItem(FidelOptionsAdapter.META_DATA_KEY));
        assertThat(FidelOptionsAdapter.OPTION_KEYS, hasItem(FidelOptionsAdapter.COUNTRY_KEY));
        assertThat(FidelOptionsAdapter.OPTION_KEYS, hasItem(FidelOptionsAdapter.CARD_SCHEMES_KEY));
        for (String key: FidelOptionsAdapter.OPTION_KEYS) {
            //for the card schemes value we only check if it exists
            if (!key.equals(FidelOptionsAdapter.CARD_SCHEMES_KEY)) {
                assertToCheckForKey(key);
            }
        }
    }

    @Test
    public void test_ChecksForSupportedCardSchemes() {
        JSONObject map = JSONObjectStub.mapWithExistingKey(FidelOptionsAdapter.CARD_SCHEMES_KEY);
        sut.process(map);
        assertTrue(map.has(FidelOptionsAdapter.CARD_SCHEMES_KEY));
    }

    //Tests when keys are present, but no data is found for that key
    @Test
    public void test_IfHasBannerImageKeyButNoImage_DontSendDataToImageAdapter() {
        map = JSONObjectStub.mapWithExistingKeyButNoValue(FidelOptionsAdapter.BANNER_IMAGE_KEY);
        sut.process(map);
        assertFalse(imageAdapterSpy.hasAskedToProcessData);
    }

    @Test
    public void test_IfHasAutoScanKeyButNoValue_DontSetItToTheSDK() {
        map = JSONObjectStub.mapWithExistingKeyButNoValue(FidelOptionsAdapter.AUTO_SCAN_KEY);
        sut.process(map);
        assertFalse(Fidel.autoScan);
    }

    @Test
    public void test_IfHasCompanyNameKeyButNoValue_DontSetItToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.COMPANY_NAME_KEY;
        map = JSONObjectStub.mapWithExistingKeyButNoValue(keyToTestFor);
        processWithString(keyToTestFor, "");
        assertNotEqualsString(keyToTestFor, Fidel.companyName);
    }

    @Test
    public void test_IfHasProgramNameKeyButNoValue_DontSetItToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.PROGRAM_NAME_KEY;
        map = JSONObjectStub.mapWithExistingKeyButNoValue(keyToTestFor);
        processWithString(keyToTestFor, "");
        assertNotEqualsString(keyToTestFor, Fidel.programName);
    }

    @Test
    public void test_IfHasDeleteInstructionsKeyButNoValue_DontSetThemToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.DELETE_INSTRUCTIONS_KEY;
        map = JSONObjectStub.mapWithExistingKeyButNoValue(keyToTestFor);
        processWithString(keyToTestFor, "");
        assertNotEqualsString(keyToTestFor, Fidel.deleteInstructions);
    }

    @Test
    public void test_IfHasPrivacyURLKeyButNoValue_DontSetItToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.PRIVACY_URL_KEY;
        map = JSONObjectStub.mapWithExistingKeyButNoValue(keyToTestFor);
        processWithString(keyToTestFor, "");
        assertNotEqualsString(keyToTestFor, Fidel.privacyURL);
    }

    @Test
    public void test_IfHasTermsConditionsURLKeyButNoValue_DontSetItToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.TERMS_CONDITIONS_URL_KEY;
        map = JSONObjectStub.mapWithExistingKeyButNoValue(keyToTestFor);
        processWithString(keyToTestFor, "");
        assertNotEqualsString(keyToTestFor, Fidel.termsConditionsURL);
    }
    //TODO: Fix failing test
    @Test
    public void test_IfHasMetaDataKeyButNoValue_DontSetItToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.META_DATA_KEY;
        map = JSONObjectStub.mapWithExistingKeyButNoValue(keyToTestFor);
        processWithMap(keyToTestFor, TEST_META_DATA(FidelOptionsAdapter.META_DATA_KEY));
        assertNull(Fidel.metaData);
    }

    @Test
    public void test_IfHasCountryKeyButNoValue_DontSetItToTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.COUNTRY_KEY;
        map = JSONObjectStub.mapWithExistingKeyButNoValue(keyToTestFor);
        assertNull(Fidel.country);
    }

    //Tests when keys are missing
    @Test
    public void test_IfDoesntHaveBannerImageKey_DontSendDataToImageAdapter() {
        map = JSONObjectStub.mapWithNoKey();
        sut.process(map);
        assertFalse(imageAdapterSpy.hasAskedToProcessData);
    }

    @Test
    public void test_IfDoesntHaveAutoScanKey_DontSetItToTheSDK() {
        map = JSONObjectStub.mapWithNoKey();
        sut.process(map);
        assertFalse(Fidel.autoScan);
    }

    @Test
    public void test_IfDoesntHaveCompanyNameKey_DontSetItToTheSDK() {
        map = JSONObjectStub.mapWithNoKey();
        String key = FidelOptionsAdapter.COMPANY_NAME_KEY;
        processWithString(TEST_COMPANY_NAME, key);
        assertNotEqualsString(key, Fidel.companyName);
    }

    @Test
    public void test_IfDoesntHaveProgramNameKey_DontSetItToTheSDK() {
        map = JSONObjectStub.mapWithNoKey();
        String key = FidelOptionsAdapter.PROGRAM_NAME_KEY;
        processWithString(TEST_PROGRAM_NAME, key);
        assertNotEqualsString(key, Fidel.programName);
    }

    @Test
    public void test_IfDoesntHaveDeleteInstructionsKey_DontSetThemToTheSDK() {
        map = JSONObjectStub.mapWithNoKey();
        String key = FidelOptionsAdapter.DELETE_INSTRUCTIONS_KEY;
        processWithString(TEST_DELETE_INSTRUCTIONS, key);
        assertNotEqualsString(key, Fidel.deleteInstructions);
    }

    @Test
    public void test_IfDoesntHavePrivacyURLKey_DontSetItToTheSDK() {
        map = JSONObjectStub.mapWithNoKey();
        String key = FidelOptionsAdapter.PRIVACY_URL_KEY;
        processWithString(TEST_PRIVACY_URL, key);
        assertNotEqualsString(key, Fidel.privacyURL);
    }

    @Test
    public void test_IfDoesntHaveTermsConditionsURLKey_DontSetItToTheSDK() {
        map = JSONObjectStub.mapWithNoKey();
        String key = FidelOptionsAdapter.TERMS_CONDITIONS_URL_KEY;
        processWithString(TEST_TERMS_CONDITIONS_URL, key);
        assertNotEqualsString(key, Fidel.termsConditionsURL);
    }
    //TODO: Fix failing test
    @Test
    public void test_IfDoesntHaveMetaDataKey_DontSetItToTheSDK() {
        String key = FidelOptionsAdapter.META_DATA_KEY;
        map = JSONObjectStub.mapWithNoKey();
        processWithMap(key, TEST_META_DATA(FidelOptionsAdapter.META_DATA_KEY));
        assertNull(Fidel.metaData);
    }

    @Test
    public void test_IfDoesntHaveCountryKey_DontSetItToTheSDK() {
        final String RANDOM_KEY = "random";
        map = JSONObjectStub.mapWithNoKey();
        processWithCountryInt(RANDOM_KEY);
        assertNull(Fidel.country);
    }
    //TODO: Fix failing test
    @Test
    public void test_IfDoesntHaveCardSchemeKey_DontSetItToTheSDK() {
        map = JSONObjectStub.mapWithNoKey();
        processWithCardSchemes(Fidel.CardScheme.VISA);
        assertEquals(EnumSet.allOf(Fidel.CardScheme.class), Fidel.supportedCardSchemes);
        //assertEquals(Fidel.CardScheme.VISA, Fidel.supportedCardSchemes);
    }

    //Setting correct values tests
    //TODO: Fix failing test
    @Test
    public void test_WhenImageProcessorSendsBitmap_SendItToImageProcessor() {
        String keyToTestFor = FidelOptionsAdapter.BANNER_IMAGE_KEY;
        map = JSONObjectStub.mapWithExistingKey(keyToTestFor);
        processWithMap(keyToTestFor, map);
        try {
            assertEquals(map.get(keyToTestFor),
                    imageAdapterSpy.dataToProcess);
        }
        catch (JSONException e) {
            fail("Test failed with error " + e.getLocalizedMessage());
        }
    }

    @Test
    public void test_WhenImageProcessorSendsBitmap_SetItForSDKBannerImage() {
        Bitmap newBitmap = Bitmap.createBitmap(100, 200, Bitmap.Config.ALPHA_8);
        sut.output(newBitmap);
        assertEquals(Fidel.bannerImage, newBitmap);
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
        map = JSONObjectStub.mapWithExistingKey(keyToTestFor);
        processWithString(keyToTestFor, TEST_COMPANY_NAME);
        assertEqualsString(keyToTestFor, Fidel.companyName);
    }

    @Test
    public void test_WhenProgramNameValueIsSet_SetItForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.PROGRAM_NAME_KEY;
        map = JSONObjectStub.mapWithExistingKey(keyToTestFor);
        processWithString(keyToTestFor, TEST_PROGRAM_NAME);
        assertEqualsString(keyToTestFor, Fidel.programName);
    }

    @Test
    public void test_WhenDeleteInstructionsValueIsSet_SetItForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.DELETE_INSTRUCTIONS_KEY;
        map = JSONObjectStub.mapWithExistingKey(keyToTestFor);
        processWithString(keyToTestFor, TEST_DELETE_INSTRUCTIONS);
        assertEqualsString(keyToTestFor, Fidel.deleteInstructions);
    }

    @Test
    public void test_WhenPrivacyURLValueIsSet_SetItForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.PRIVACY_URL_KEY;
        map = JSONObjectStub.mapWithExistingKey(keyToTestFor);
        processWithString(keyToTestFor, TEST_PRIVACY_URL);
        assertEqualsString(keyToTestFor, Fidel.privacyURL);
    }

    @Test
    public void test_WhenTermsConditionsURLValueIsSet_SetItForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.TERMS_CONDITIONS_URL_KEY;
        map = JSONObjectStub.mapWithExistingKey(keyToTestFor);
        processWithString(keyToTestFor, TEST_TERMS_CONDITIONS_URL);
        assertEqualsString(keyToTestFor, Fidel.termsConditionsURL);
    }
    //TODO: Fix failing test
    @Test
    public void test_WhenMetaDataValueIsSet_ConvertItToJSONForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.META_DATA_KEY;
        map = JSONObjectStub.mapWithExistingKey(keyToTestFor);
        processWithMap(keyToTestFor, TEST_META_DATA(FidelOptionsAdapter.META_DATA_KEY));
        System.out.println("In test, TEST_HASH_MAP / Fidel.metaData is " + TEST_HASH_MAP() + " / " + Fidel.metaData);
        assertMapEqualsWithJSONObject(TEST_HASH_MAP(), Fidel.metaData);
    }

    @Test
    public void test_WhenCountryIsSet_ConvertItWithCountryAdapterForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.COUNTRY_KEY;
        map = JSONObjectStub.mapWithExistingKey(keyToTestFor);
        processWithCountryInt(FidelOptionsAdapter.COUNTRY_KEY);
        assertEquals(countryAdapterStub.countryToReturn, Fidel.country);
        try {
            assertEquals(map.get(FidelOptionsAdapter.COUNTRY_KEY), countryAdapterStub.countryIntegerReceived);

        }
        catch (JSONException e) {
            fail("Test failed with error " + e.getLocalizedMessage());
        }
    }

    @Test
    public void test_WhenCardSchemesAreSet_ConvertThemWithCountryAdapterForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.CARD_SCHEMES_KEY;
        map = JSONObjectStub.mapWithExistingKey(keyToTestFor);
        processWithCardSchemes(Fidel.CardScheme.VISA, Fidel.CardScheme.MASTERCARD);
        try {
            assertEquals(map.getJSONArray(FidelOptionsAdapter.CARD_SCHEMES_KEY), cardSchemesAdapterStub.cardSchemesReceived);
        }
        catch (JSONException e) {
            fail("Test failed with error " + e.getLocalizedMessage());
        }

    }

    @Test
    public void test_WhenCardSchemesAreSet_SetThemForTheSDK() {
        String keyToTestFor = FidelOptionsAdapter.CARD_SCHEMES_KEY;
        map = JSONObjectStub.mapWithExistingKey(keyToTestFor);
        Fidel.CardScheme[] expectedSchemes = {Fidel.CardScheme.VISA, Fidel.CardScheme.MASTERCARD};
        Set<Fidel.CardScheme> expectedSchemesSet = EnumSet.copyOf(Arrays.asList(expectedSchemes));
        processWithCardSchemes(expectedSchemes);
        assertEquals(expectedSchemesSet, Fidel.supportedCardSchemes);
    }

    //Exposed constants tests
    @Test
    public void test_WhenAskedForConstants_IncludeConstantsFromCountriesAdapter() {
        Map<String, Object> actualConstants = sut.getConstants();
        Map<String, Object> expectedConstants = countryAdapterStub.getConstants();
        assertMapContainsMap(actualConstants, expectedConstants);
    }

    @Test
    public void test_WhenAskedForConstants_IncludeConstantsFromCardSchemesAdapter() {
        Map<String, Object> actualConstants = sut.getConstants();
        Map<String, Object> expectedConstants = cardSchemesAdapterStub.getConstants();
        assertMapContainsMap(actualConstants, expectedConstants);
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
        //JSONObjectStub map = new JSONObjectStub();
        //map.hashMapToReturn = TEST_HASH_MAP();
        try {
            map.put(key, TEST_HASH_MAP());
            return map;
        }
        catch(JSONException e) {
            fail("Test failed with error " + e.getLocalizedMessage());
            return map;
        }
    }

    private void processWithBoolean(Boolean bool) {
        map = JSONObjectStub.mapWithExistingKey(FidelOptionsAdapter.AUTO_SCAN_KEY);
        try {
            map.put(FidelOptionsAdapter.AUTO_SCAN_KEY, bool);
            sut.process(map);
        }
        catch (JSONException e) {
            fail("Test failed with message " + e.getLocalizedMessage());
        }

    }

//    private void processWithString(String string, String key) {
//        map.stringForKeyToReturn.put(key, string);
//        sut.process(map);
//    }

    private void processWithString(String key, String value) {
        try {
            map.put(key, value);
            sut.process(map);
        }
        catch (JSONException e) {
            e.printStackTrace();
            fail("Test failed with error " + e.getLocalizedMessage());
        }
    }

    private void processWithMap(String key, JSONObject mapToReturn) {
        try {
            map.put(key, mapToReturn);
            sut.process(map);
        }
        catch (JSONException e) {
            fail("Test failed with error " + e.getLocalizedMessage());
        }
    }

    private void processWithCountryInt(String key) {
        countryAdapterStub.countryToReturn = TEST_COUNTRY;
        try {
            map.put(key, TEST_COUNTRY_NUMBER);
            sut.process(map);
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
            map.put(FidelOptionsAdapter.CARD_SCHEMES_KEY, ja);
            //map.readableArrayToReturn = JavaOnlyArray.of((Object[]) cardSchemes);
            sut.process(map);
        }
        catch (JSONException e) {
            fail("Test failed with message " + e.getLocalizedMessage());
        }
    }

    private void assertEqualsString(String key, String valueToCheckWith) {
        System.out.println("In assertEqualsString, map is " + map);
        sut.process(map);
        try {
            //map.put(key, valueToCheckWith);
            assertEquals(map.get(key), valueToCheckWith);
        }
        catch (JSONException e) {
            fail("Test failed with error " + e.getLocalizedMessage());
        }

    }

    private void assertNotEqualsString(String key, String valueToCheckWith) {
        sut.process(map);
        try {
            if (map.has(key)) {
                assertNotEquals(map.get(key), valueToCheckWith);
            }
        }
        catch (JSONException e) {
            fail("Test failed with error " + e.getLocalizedMessage());
        }
    }
//
    private void assertToCheckForKey(String keyToCheckFor) {
        JSONObject map = JSONObjectStub.mapWithExistingKey(keyToCheckFor);
        sut.process(map);
        assertTrue(map.has(keyToCheckFor));
//        assertThat(map.keyNamesCheckedFor, hasItem(keyToCheckFor));
//        assertThat(map.keyNamesVerifiedNullFor, hasItem(keyToCheckFor));
//        assertThat(map.keyNamesAskedFor, hasItem(keyToCheckFor));
    }
}
