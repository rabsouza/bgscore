package br.com.battista.bgscore.robot;

import android.content.Context;

import br.com.battista.bgscore.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MatchRobot extends BaseRobot {

    private Context context;

    public MatchRobot(Context context) {
        this.context = context;
    }

    public MatchRobot checkEmptyMatches() {
        onView(withText(R.string.text_match_matches_empty_view))
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
