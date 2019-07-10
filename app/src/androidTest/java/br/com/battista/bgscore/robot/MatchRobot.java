package br.com.battista.bgscore.robot;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;

import br.com.battista.bgscore.R;

public class MatchRobot extends BaseRobot {

    private static final String EMPTY_SCORE_VALUE = "00";
    private Context context;

    public MatchRobot(Context context) {
        this.context = context;
    }

    public MatchRobot checkEmptyMatches() {
        onView(allOf(withId(R.id.card_view_matches_score_very_dissatisfied),
                hasDescendant(withText(EMPTY_SCORE_VALUE))))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.card_view_matches_score_dissatisfied),
                hasDescendant(withText(EMPTY_SCORE_VALUE))))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.card_view_matches_score_neutral),
                hasDescendant(withText(EMPTY_SCORE_VALUE))))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.card_view_matches_score_satisfied),
                hasDescendant(withText(EMPTY_SCORE_VALUE))))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.card_view_matches_score_very_satisfied),
                hasDescendant(withText(EMPTY_SCORE_VALUE))))
                .check(matches(isDisplayed()));
        return this;
    }

    public MatchRobot checkLegendMatches() {
        onView(withId(R.id.card_view_matches_help))
                .perform(click());

        onView(withText(R.string.text_legend_info_01))
                .check(matches(isDisplayed()));
        return this;
    }

    public MatchRobot checkPopupWindowOrderListMatches() {
        onView(withId(R.id.btn_sort_list))
                .perform(click());

        onView(withText(R.string.text_match_sort_list_info_01))
                .check(matches(isDisplayed()));
        return this;
    }

}
