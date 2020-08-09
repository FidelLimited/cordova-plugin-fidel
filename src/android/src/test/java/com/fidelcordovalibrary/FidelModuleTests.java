//package com.fidelcordovalibrary;
//
//import android.content.Context;
//
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.fidelcordovalibrary.adapters.abstraction.ConstantsProvider;
//import com.fidelcordovalibrary.fakes.CallbackInputSpy;
//import com.fidelcordovalibrary.fakes.CallbackSpy;
//import com.fidelcordovalibrary.fakes.ConstantsProviderStub;
//import com.fidelcordovalibrary.fakes.DataProcessorSpy;
//import com.fidelcordovalibrary.fakes.ReactContextMock;
//import com.fidelcordovalibrary.fakes.JSONObjectStub;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.fidelcordovalibrary.helpers.AssertHelpers.assertMapContainsMap;
//import static org.junit.Assert.assertEquals;
//
//@RunWith(AndroidJUnit4.class)
//public class FidelModuleTests {
//
//    private FidelModule sut;
//    private DataProcessorSpy optionsAdapterSpy;
//    private DataProcessorSpy setupAdapterSpy;
//    private List<ConstantsProvider> constantsProviderListStub;
//    private CallbackInputSpy callbackInputSpy;
//
//    @Before
//    public final void setUp() {
//        Context context = ApplicationProvider.getApplicationContext();
//        ReactContextMock reactContext = new ReactContextMock(context);
//        optionsAdapterSpy = new DataProcessorSpy();
//        setupAdapterSpy = new DataProcessorSpy();
//        constantsProviderListStub = new ArrayList<>();
//        ConstantsProvider constantsProvider = new ConstantsProviderStub("testModuleConstantKey", 345);
//        constantsProviderListStub.add(constantsProvider);
//        callbackInputSpy = new CallbackInputSpy();
//        sut = new FidelModule(reactContext,
//                setupAdapterSpy,
//                optionsAdapterSpy,
//                constantsProviderListStub,
//                callbackInputSpy);
//    }
//
//    @After
//    public final void tearDown() {
//        sut = null;
//        optionsAdapterSpy = null;
//        constantsProviderListStub = null;
//    }
//
//    @Test
//    public void test_WhenSettingOptions_ForwardThemToOptionsAdapter() {
//        JSONObjectStub fakeMap = new JSONObjectStub();
//        sut.setOptions(fakeMap);
//        assertEquals(optionsAdapterSpy.dataToProcess, fakeMap);
//    }
//
//    @Test
//    public void test_WhenGettingConstants_ReturnConstantsFromFirstConstantsProvider() {
//        assertMapContainsMap(sut.getConstants(), constantsProviderListStub.get(0).getConstants());
//    }
//
//    @Test
//    public void test_WhenAskedToSetup_ForwardSetupDataToSetupAdapter() {
//        JSONObjectStub fakeMap = new JSONObjectStub();
//        sut.setup(fakeMap);
//        assertEquals(setupAdapterSpy.dataToProcess, fakeMap);
//    }
//
//    @Test
//    public void test_WhenAskedToOpenForm_SendCallbackToInput() {
//        CallbackSpy callback = new CallbackSpy();
//        sut.openForm(callback);
//        assertEquals(callbackInputSpy.receivedCallback, callback);
//    }
//}
