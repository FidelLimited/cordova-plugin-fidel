package com.fidelcordovalibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.WebChromeClient;


import com.fidel.sdk.Fidel;
import com.fidel.sdk.LinkResult;
import com.fidel.sdk.LinkResultErrorCode;
import com.fidelcordovalibrary.fakes.CallbackSpy;
import com.fidelcordovalibrary.fakes.DataConverterStub;
import com.fidelcordovalibrary.fakes.DataProcessorSpy;
import com.fidelcordovalibrary.fakes.IntentMock;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewEngine;
import org.apache.cordova.ICordovaCookieManager;
import org.apache.cordova.PluginEntry;
import org.apache.cordova.PluginManager;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class FidelPluginTests {

    private FidelPlugin sut;
    private CallbackSpy callbackSpy;
    private DataConverterStub<Object, JSONObject> linkResultConverterStub;
    private Activity activity;
    private IntentMock<LinkResult> intent;
    private DataProcessorSpy<JSONObject> errorHandlerSpy;
    private CallbackContext callbackContext;

    private static final String RESULT_EXTRA_KEY = Fidel.FIDEL_LINK_CARD_RESULT_CARD;
    private static final int REQUEST_CODE = Fidel.FIDEL_LINK_CARD_REQUEST_CODE;

    @Before
    public final void setUp() {
        linkResultConverterStub = new DataConverterStub<>();
        errorHandlerSpy = new DataProcessorSpy<>();
        sut = new FidelPlugin();
        callbackSpy = new CallbackSpy();
        final String callbackId = "10";
        final CordovaWebView cordovaWebView = new CordovaWebView() {
            @Override
            public void init(CordovaInterface cordova, List<PluginEntry> pluginEntries, CordovaPreferences preferences) {

            }

            @Override
            public boolean isInitialized() {
                return false;
            }

            @Override
            public View getView() {
                return null;
            }

            @Override
            public void loadUrlIntoView(String url, boolean recreatePlugins) {

            }

            @Override
            public void stopLoading() {

            }

            @Override
            public boolean canGoBack() {
                return false;
            }

            @Override
            public void clearCache() {

            }

            @Override
            public void clearCache(boolean b) {

            }

            @Override
            public void clearHistory() {

            }

            @Override
            public boolean backHistory() {
                return false;
            }

            @Override
            public void handlePause(boolean keepRunning) {

            }

            @Override
            public void onNewIntent(Intent intent) {

            }

            @Override
            public void handleResume(boolean keepRunning) {

            }

            @Override
            public void handleStart() {

            }

            @Override
            public void handleStop() {

            }

            @Override
            public void handleDestroy() {

            }

            @Override
            public void sendJavascript(String statememt) {

            }

            @Override
            public void showWebPage(String url, boolean openExternal, boolean clearHistory, Map<String, Object> params) {

            }

            @Override
            public boolean isCustomViewShowing() {
                return false;
            }

            @Override
            public void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {

            }

            @Override
            public void hideCustomView() {

            }

            @Override
            public CordovaResourceApi getResourceApi() {
                return null;
            }

            @Override
            public void setButtonPlumbedToJs(int keyCode, boolean override) {

            }

            @Override
            public boolean isButtonPlumbedToJs(int keyCode) {
                return false;
            }

            @Override
            public void sendPluginResult(PluginResult cr, String callbackId) {

            }

            @Override
            public PluginManager getPluginManager() {
                return null;
            }

            @Override
            public CordovaWebViewEngine getEngine() {
                return null;
            }

            @Override
            public CordovaPreferences getPreferences() {
                return null;
            }

            @Override
            public ICordovaCookieManager getCookieManager() {
                return null;
            }

            @Override
            public String getUrl() {
                return null;
            }

            @Override
            public Context getContext() {
                return null;
            }

            @Override
            public void loadUrl(String url) {

            }

            @Override
            public Object postMessage(String id, Object data) {
                return null;
            }
        };
        callbackContext = new CallbackContext(callbackId, cordovaWebView);


        activity = Robolectric.buildActivity(Activity.class).setup().get();
        sut.initialize(callbackContext, activity.getApplicationContext());
        intent = new IntentMock<>(activity, Activity.class);
        intent.parcelableExtraToReturn = new LinkResult("TEST CARD ID");
        linkResultConverterStub.convertedDataToReturn = new JSONObject();
    }

    @After
    public final void tearDown() {
        sut = null;
        callbackSpy = null;
        linkResultConverterStub = null;
        activity = null;
        intent = null;
        errorHandlerSpy = null;
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
