package br.com.battista.bgscore.robot;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import br.com.battista.bgscore.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by rafaelbs on 14/09/17.
 */

class BaseRobot {

    public void navigationToHome() {
        onView(withId(R.id.action_home))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.title_home)));
    }

    public void navigationToMatches() {
        onView(withId(R.id.action_matches))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.title_matches)));
    }

    public void navigationToGames() {
        onView(withId(R.id.action_games))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.title_games)));
    }

    public void navigationToAccount() {
        onView(withId(R.id.action_account))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.title_account)));
    }

    public void navigationToInfo() {
        onView(withId(R.id.action_info))
                .perform(click());

        onView(withText(R.string.title_info))
                .check(matches(isDisplayed()));
    }

    public void closeWelcomeDialog() {
        try {
            onView(withText(R.string.btn_ok))
                    .perform(click());
        } catch (Exception e) {
        }
    }

}
