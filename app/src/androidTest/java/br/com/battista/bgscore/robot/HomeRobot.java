package br.com.battista.bgscore.robot;

import android.content.Context;
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
import static br.com.battista.bgscore.helper.DrawableMatcher.withDrawable;
import static org.hamcrest.Matchers.allOf;

public class HomeRobot {

    private Context context;

    public HomeRobot(Context context) {
        this.context = context;
    }

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

    public void checkDrawableUserAvatar(int avatar_profile) {
        onView(withId(R.id.card_view_home_img)).check(matches(
                withDrawable(avatar_profile)));
    }

    public void checkTextLastPlay(String lastPlay) {
        onView(withId(R.id.card_view_home_last_play))
                .check(matches(
                        withText(context.getString(R.string.text_home_last_play, lastPlay))));
    }

    public void checkTextUsername(String username) {
        onView(withId(R.id.card_view_home_username))
                .check(matches(
                        withText(context.getString(R.string.text_home_username, username))));
    }

    public void checkScoreValueTotalTime(String value) {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_score_total_time))))
                .check(matches(isDisplayed()));
    }

    public void checkScoreValueMatches(String value) {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_score_matches))))
                .check(matches(isDisplayed()));
    }

    public void checkScoreValueGames(String value) {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_score_games))))
                .check(matches(isDisplayed()));
    }
    public void checkEmptyRanking() {
        onView(withText(R.string.text_home_stats_empty_view))
                .check(matches(isDisplayed()));
    }

}
