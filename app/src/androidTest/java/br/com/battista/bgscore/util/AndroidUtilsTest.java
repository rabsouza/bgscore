package br.com.battista.bgscore.util;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.battista.bgscore.activity.MainActivity;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class AndroidUtilsTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldReturnVersion() {
        String versionName = AndroidUtils.getVersionName(mActivityTestRule.getActivity());

        assertThat(versionName, not(isEmptyOrNullString()));
    }

}