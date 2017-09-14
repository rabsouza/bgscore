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


public abstract class BaseRobot {

    public BaseRobot navigationToHome() {
        onView(withId(R.id.action_home))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.title_home)));
        return this;
    }

    public BaseRobot navigationToMatches() {
        onView(withId(R.id.action_matches))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.title_matches)));
        return this;
    }

    public BaseRobot navigationToGames() {
        onView(withId(R.id.action_games))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.title_games)));
        return this;
    }

    public BaseRobot navigationToAccount() {
        onView(withId(R.id.action_account))
                .perform(click());

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.title_account)));
        return this;
    }

    public BaseRobot navigationToInfo() {
        onView(withId(R.id.action_info))
                .perform(click());

        onView(withText(R.string.title_info))
                .check(matches(isDisplayed()));
        return this;
    }

    public BaseRobot closeWelcomeDialog() {
        try {
            onView(withText(R.string.btn_ok))
                    .perform(click());
        } catch (Exception e) {
        }
        return this;
    }

}
