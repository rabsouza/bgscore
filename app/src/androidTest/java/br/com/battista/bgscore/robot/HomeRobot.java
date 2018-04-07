package br.com.battista.bgscore.robot;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.battista.bgscore.helper.DrawableMatcher.withDrawable;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;

import br.com.battista.bgscore.R;

public class HomeRobot extends BaseRobot {

    private Context context;

    public HomeRobot(Context context) {
        this.context = context;
    }

    public HomeRobot checkDrawableUserAvatar(int avatar_profile) {
        onView(withId(R.id.card_view_home_img)).check(matches(
                withDrawable(avatar_profile)));
        return this;
    }

    public HomeRobot checkTextLastPlay(String lastPlay) {
        onView(withId(R.id.card_view_home_last_play))
                .check(matches(
                        withText(context.getString(R.string.text_home_last_play, lastPlay))));
        return this;
    }

    public HomeRobot checkTextUsername(String username) {
        onView(withId(R.id.card_view_home_username))
                .check(matches(
                        withText(context.getString(R.string.text_home_username, username))));
        return this;
    }

    public HomeRobot checkScoreValueTotalTime(String value) {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_score_total_time))))
                .check(matches(isDisplayed()));
        return this;
    }

    public HomeRobot checkScoreValueMatches(String value) {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_score_matches))))
                .check(matches(isDisplayed()));
        return this;
    }

    public HomeRobot checkScoreValueGames(String value) {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_score_games))))
                .check(matches(isDisplayed()));
        return this;
    }

    public HomeRobot checkEmptyRanking() {
        onView(withText(R.string.text_home_stats_empty_view))
                .check(matches(isDisplayed()));
        return this;
    }

    public HomeRobot checkLegendRanking() {
        onView(withId(R.id.card_view_stats_help))
                .perform(click());

        onView(withText(R.string.text_legend_info_01))
                .check(matches(isDisplayed()));
        return this;
    }

}
