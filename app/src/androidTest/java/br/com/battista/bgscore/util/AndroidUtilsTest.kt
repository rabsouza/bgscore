package br.com.battista.bgscore.util

import android.support.test.filters.SmallTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import br.com.battista.bgscore.activity.MainActivity
import org.hamcrest.Matchers.isEmptyOrNullString
import org.hamcrest.Matchers.not
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class AndroidUtilsTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun shouldReturnVersion() {
        val versionName = AndroidUtils.getVersionName(mActivityTestRule.activity)

        assertThat(versionName, not(isEmptyOrNullString()))
    }

}