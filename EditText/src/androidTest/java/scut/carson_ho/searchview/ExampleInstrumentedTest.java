package scut.carson_ho.searchview;

import android.content.Context;

import androidx.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

//import android.support.test.InstrumentationRegistry;
//import android.support.test.runner.AndroidJUnit4;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("scut.carson_ho.searchview.test", appContext.getPackageName());
    }
}
