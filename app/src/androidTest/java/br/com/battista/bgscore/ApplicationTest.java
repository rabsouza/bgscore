package br.com.battista.bgscore;

import static org.junit.Assert.assertThat;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class ApplicationTest {

    @Test
    public void shouldValidAppPackageName() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertThat(appContext.getPackageName(),
                Matchers.containsString("br.com.battista.bgscore"));
    }
}
