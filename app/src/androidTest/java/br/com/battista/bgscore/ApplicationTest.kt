package br.com.battista.bgscore

import android.support.test.InstrumentationRegistry
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.containsString
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class ApplicationTest {

    @Test
    @Throws(Exception::class)
    fun shouldValidAppPackageName() {
        val appContext = InstrumentationRegistry.getTargetContext()

        assertThat(appContext.packageName,
                containsString("br.com.battista.bgscore"))
    }
}
