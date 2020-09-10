package com.fidelcordovalibrary;

import android.app.Activity;

import com.fidel.sdk.Fidel;
import com.fidel.sdk.LinkResult;
import com.fidel.sdk.LinkResultErrorCode;
import com.fidelcordovalibrary.fakes.CordovaWebViewMock;
import com.fidelcordovalibrary.fakes.DataConverterStub;
import com.fidelcordovalibrary.fakes.IntentMock;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaWebView;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class FidelPluginTests {

    private FidelPlugin sut;
    private DataConverterStub<Object, JSONObject> linkResultConverterStub;
    private Activity activity;
    private IntentMock<LinkResult> intent;
    private CallbackContext callbackContext;

    private static final String RESULT_EXTRA_KEY = Fidel.FIDEL_LINK_CARD_RESULT_CARD;
    private static final int REQUEST_CODE = Fidel.FIDEL_LINK_CARD_REQUEST_CODE;

    @Before
    public final void setUp() {
        linkResultConverterStub = new DataConverterStub<>();
        sut = new FidelPlugin();
        final String callbackId = "10";
        CordovaWebView cordovaWebView =  CordovaWebViewMock.getWebView();
        callbackContext = new CallbackContext(callbackId, cordovaWebView);
        activity = Robolectric.buildActivity(Activity.class).setup().get();
        CordovaInterfaceImpl cordovaInterfaceImpl = new CordovaInterfaceImpl(activity);
        sut.initialize(cordovaInterfaceImpl, cordovaWebView);
        sut.setCallbackContext(callbackContext);
        intent = new IntentMock<>(activity, Activity.class);
        intent.parcelableExtraToReturn = new LinkResult("TEST CARD ID");
        linkResultConverterStub.convertedDataToReturn = new JSONObject();
    }

    @After
    public final void tearDown() {
        sut = null;
        linkResultConverterStub = null;
        activity = null;
        intent = null;
    }

    @Test
    public void test_WhenReceivingLinkResult_SendItConvertedInCallback() {
        sut.onActivityResult(REQUEST_CODE, 0, intent);

        assertEquals(RESULT_EXTRA_KEY, intent.parcelableExtraNameAskedFor);
    }


    @Test
    public void test_WhenReceivingLinkResultWithError_InvokeCallback() {
        onActivityResultWithError();
        linkResultConverterStub.convertedDataToReturn = new JSONObject();
        assertTrue(callbackContext.isFinished());
    }

    @Test
    public void test_WhenRequestCodeIsNotFidelRequestCode_DontInvokeTheCallback() {
        sut.onActivityResult(0, 0, intent);
        assertFalse(callbackContext.isFinished());
    }

    @Test
    public void test_WhenRequestCodeIsNotFidelRequestCode_DontAskForFidelExtras() {
        sut.onActivityResult(0, 0, intent);
        assertNull(intent.parcelableExtraNameAskedFor);
    }

    private void onActivityResultWithError() {
        LinkResultErrorCode errorCode = LinkResultErrorCode.USER_CANCELED;
        intent.parcelableExtraToReturn = new LinkResult(errorCode, "Test error message");;
        sut.onActivityResult(REQUEST_CODE, 0, intent);
    }
}
