package br.com.battista.bgscore.fragment;

import android.content.Context;
import android.content.Intent;
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
import br.com.battista.bgscore.activity.HomeActivity;
import br.com.battista.bgscore.helper.HomeActivityHelper;
import br.com.battista.bgscore.model.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.battista.bgscore.helper.DrawableMatcher.withDrawable;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    @Rule
    public ActivityTestRule<HomeActivity> testRule =
            new ActivityTestRule<>(HomeActivity.class, Boolean.TRUE, Boolean.FALSE);

    private User user;
    private Context context;

    @Before
    public void setup() {
        testRule.launchActivity(new Intent());
        context = testRule.getActivity().getApplicationContext();

        MainApplication application = (MainApplication)
                testRule.getActivity().getApplication();
        MainApplication.init(application);

        user = HomeActivityHelper.createNewUser();
        application.clearPreferences();
        application.setUser(user);

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
    public void shouldShowDataUserWhenFirstAccess() {
        onView(withId(R.id.card_view_home_username))
                .check(matches(
                        withText(context.getString(R.string.text_home_username, user.getUsername()))));

        onView(withId(R.id.card_view_home_last_play))
                .check(matches(
                        withText(context.getString(R.string.text_home_last_play, "-"))));

        onView(withId(R.id.card_view_home_img)).check(matches(
                withDrawable(R.drawable.avatar_profile)));
    }

    @Test
    public void shouldShowDataStaticsWhenFirstAccess() {
        onView(allOf(withText("00"),
                withParent(withId(R.id.card_view_score_games))))
                .check(matches(isDisplayed()));

        onView(allOf(withText("00"),
                withParent(withId(R.id.card_view_score_matches))))
                .check(matches(isDisplayed()));

        onView(allOf(withText("00:00"),
                withParent(withId(R.id.card_view_score_total_time))))
                .check(matches(isDisplayed()));
    }

}
