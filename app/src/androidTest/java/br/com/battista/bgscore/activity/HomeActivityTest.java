package br.com.battista.bgscore.activity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.helper.HomeActivityHelper;
import br.com.battista.bgscore.model.User;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Rule
    public ActivityTestRule<HomeActivity> testRule =
            new ActivityTestRule<>(HomeActivity.class, Boolean.TRUE, Boolean.FALSE);

    private User user;

    @Before
    public void setup() {
        testRule.launchActivity(new Intent());

        MainApplication application = (MainApplication)
                testRule.getActivity().getApplication();
        MainApplication.init(application);

        user = HomeActivityHelper.createNewUser();
        application.clearPreferences();
        application.setUser(user);
    }

    @Test
    public void checkExistsTheElementsActivity() {
        HomeActivity activity = testRule.getActivity();

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        assertNotNull(toolbar);

        BottomNavigationView bottomNavigationView =
                activity.findViewById(R.id.bottom_navigation);
        assertNotNull(bottomNavigationView);
    }

    @Test
    public void checkExistMenuInBottomNavigationView() {
        HomeActivity activity = testRule.getActivity();

        BottomNavigationView bottomNavigationView =
                activity.findViewById(R.id.bottom_navigation);
        assertNotNull(bottomNavigationView);

        assertThat(bottomNavigationView.getMenu().size(), equalTo(5));

        assertNotNull(bottomNavigationView.getMenu().findItem(R.id.action_home));
        assertNotNull(bottomNavigationView.getMenu().findItem(R.id.action_matches));
        assertNotNull(bottomNavigationView.getMenu().findItem(R.id.action_games));
        assertNotNull(bottomNavigationView.getMenu().findItem(R.id.action_account));
        assertNotNull(bottomNavigationView.getMenu().findItem(R.id.action_info));
    }

    @Test
    public void shouldShowHomeWhenClickActionHome() {
        try {
            onView(withText(R.string.btn_ok))
                    .perform(click());
        } catch (Exception e) {
        }

        onView(withId(R.id.action_home))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.title_home)));
    }

    @Test
    public void shouldShowMatchesWhenClickActionMatches() {
        try {
            onView(withText(R.string.btn_ok))
                    .perform(click());
        } catch (Exception e) {
        }

        onView(withId(R.id.action_matches))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.title_matches)));
    }

    @Test
    public void shouldShowGamesWhenClickActionGames() {
        try {
            onView(withText(R.string.btn_ok))
                    .perform(click());
        } catch (Exception e) {
        }

        onView(withId(R.id.action_games))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.title_games)));
    }

    @Test
    public void shouldShowAccountWhenClickActionAccount() {
        try {
            onView(withText(R.string.btn_ok))
                    .perform(click());
        } catch (Exception e) {
        }

        onView(withId(R.id.action_account))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.title_account)));
    }

    @Test
    public void shouldShowInfoWhenClickActionInfo() {
        try {
            onView(withText(R.string.btn_ok))
                    .perform(click());
        } catch (Exception e) {
        }

        onView(withId(R.id.action_info))
                .perform(click());

        onView(withText(R.string.title_info))
                .check(matches(isDisplayed()));
    }

}
