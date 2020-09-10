package com.fidelcordovalibrary;

import android.os.Parcelable;

import com.fidel.sdk.LinkResult;
import com.fidel.sdk.LinkResultErrorCode;
import com.fidelcordovalibrary.adapters.JSONObjectDataConverter;
import com.fidelcordovalibrary.adapters.abstraction.ObjectFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

//Custom test runner is necessary for being able to use JSONObject
@RunWith(RobolectricTestRunner.class)
public class JSONObjectDataConverterTests {

    private static final String TEST_CARD_ID = "Test Card ID";
    private static final String TEST_ERROR_MESSAGE = "Test Error Message";

    private JSONObjectDataConverter sut;

    @Before
    public final void setUp() {
        sut = new JSONObjectDataConverter(new ObjectFactory<JSONObject>() {
            @Override
            public JSONObject create() {
                return new JSONObject();
            }
        });
    }

    @After
    public final void tearDown() {
        sut = null;
    }

    @Test
    public void test_WhenAskedToConvertNullObject_ReturnNull() {
        assertNull(sut.getConvertedDataFor(null));
    }
    //TODO: Fix failing test
    @Test
    public void test_WhenConvertingLinkResult_IncludeAllObjectFields() throws IllegalAccessException {
        LinkResult linkResult = new LinkResult(TEST_CARD_ID);
        setFieldsFor(linkResult);

        JSONObject receivedJsonObject = sut.getConvertedDataFor(linkResult);

        for (Field field: linkResult.getClass().getDeclaredFields()) {
            try {
                if (field.getType() == String.class) {
                    String receivedString = receivedJsonObject.getString(field.getName());
                    assertEquals(receivedString, field.get(linkResult));
                }
                else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                    boolean receivedVal = receivedJsonObject.getBoolean(field.getName());
                    assertEquals(receivedVal, field.get(linkResult));
                }
                else if (field.getType() == int.class) {
                    int receivedVal = receivedJsonObject.getInt(field.getName());
                    assertEquals(receivedVal, field.get(linkResult));
                }
                else if (field.getType() == JSONObject.class) {
                    JSONObject mapJson = receivedJsonObject.getJSONObject(field.getName());
                    JSONObject jsonField = (JSONObject)field.get(linkResult);
                    //TODO: Find a way to remove whitespace comparison from the assert
                    //assertEquals(mapJson, jsonField);
                    Iterator<String> jsonKeyIterator = mapJson.keys();
                    while (jsonKeyIterator.hasNext()) {
                        String key = jsonKeyIterator.next();
                        Object refereceValue = mapJson.get(key);
                        Object compareValue = jsonField.get(key);
                        if (refereceValue.getClass() == JSONObject.class) {
                            Iterator<String> nestedKeyIterator = ((JSONObject) refereceValue).keys();
                            while (nestedKeyIterator.hasNext()) {
                                String nestedKey = nestedKeyIterator.next();
                                Object nestedReferenceValue = ((JSONObject) refereceValue).get(nestedKey);
                                Object nestedCompareValue = ((JSONObject) compareValue).get(nestedKey);
                                assertEquals(nestedReferenceValue, nestedCompareValue);
                            }
                        }
                        else {
                            assertEquals(refereceValue, compareValue);
                        }
                    }

                }
                else if (field.getType() != Parcelable.Creator.class) {
                    fail("Some of the link result properties are not converted");
                }
            }
            catch (JSONException e) {
                fail("Test failed with message " + e.getLocalizedMessage());
            }
        }
    }

    @Test
    public void test_WhenConvertingLinkResultWithError_IncludeAllErrorFields() throws IllegalAccessException {
        LinkResultErrorCode errorCode = LinkResultErrorCode.USER_CANCELED;
        String errorMessage = TEST_ERROR_MESSAGE;
        LinkResult linkResult = new LinkResult(errorCode, errorMessage);
        Object objectToConvert = linkResult.getError();

        JSONObject receivedMap = sut.getConvertedDataFor(objectToConvert);

        for (Field field: objectToConvert.getClass().getDeclaredFields()) {
            try {
                if (field.getType() == String.class) {
                    String receivedString = receivedMap.getString(field.getName());
                    assertEquals(receivedString, field.get(objectToConvert));
                }
                else if (field.getType() == LinkResultErrorCode.class) {
                    String receivedErrorCodeString = receivedMap.getString(field.getName());
                    LinkResultErrorCode expectedErrorCode = (LinkResultErrorCode) field.get(objectToConvert);
                    String expectedErrorCodeString = expectedErrorCode.toString().toLowerCase();
                    assertEquals(receivedErrorCodeString, expectedErrorCodeString);
                }
                else {
                    fail("Some of the link result error properties are not converted");
                }
            }
            catch (JSONException e) {
                fail("Test failed with message " + e.getLocalizedMessage());
            }

        }
    }

    //Helpers
    private void setFieldsFor(Object object) {
        for (Field field: object.getClass().getDeclaredFields()) {
            try {
                if (field.getType() == String.class) {
                    field.set(object, field.getName()+"Value");
                }
                else if (field.getType() == boolean.class) {
                    field.setBoolean(object, getRandomBoolean());
                }
                else if (field.getType() == int.class) {
                    field.setInt(object, getRandomInt());
                }
                else if (field.getType() == JSONObject.class) {
                    JSONObject json = new JSONObject();
                    json.put("keyString", "StringValue");
                    json.put("keyInt", getRandomInt());
                    json.put("keyBool", getRandomBoolean());
                    JSONObject internalJSON = new JSONObject();
                    internalJSON.put("internalJSONKey1", getRandomInt());
                    internalJSON.put("internalJSONKey2", new JSONObject());
                    json.put("keyJSON", internalJSON);
                    field.set(object, json);
                }
            } catch (IllegalAccessException | JSONException e) {
                fail();
            }
        }
    }

    private static boolean getRandomBoolean() {
        return Math.random() < 0.5;
    }
    private static int getRandomInt() {
        return (int)(Math.random() * 1000);
    }
}
