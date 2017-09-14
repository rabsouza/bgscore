package br.com.battista.bgscore.helper;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

public class CustomScrollActions {

    public static void nestedScrollTo() {
        Instrumentation instr = InstrumentationRegistry.getInstrumentation();
        UiDevice.getInstance(instr);
        UiScrollable appViews = new UiScrollable(
                new UiSelector().scrollable(true));

        try {
            appViews.scrollForward();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

}
