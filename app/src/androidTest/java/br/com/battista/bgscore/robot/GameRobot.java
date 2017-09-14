package br.com.battista.bgscore.robot;

import android.content.Context;

import br.com.battista.bgscore.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class GameRobot extends BaseRobot {

    private Context context;

    public GameRobot(Context context) {
        this.context = context;
    }

    public void checkScoreValueMyGames(String value) {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_games_score_my_game))))
                .check(matches(isDisplayed()));
    }

    public void checkScoreValueFavoriteGames(String value) {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_games_score_favorite))))
                .check(matches(isDisplayed()));
    }

    public void checkScoreValueWantGames(String value) {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_games_score_want_game))))
                .check(matches(isDisplayed()));
    }

    public void checkEmptyGames() {
        onView(withText(R.string.text_game_games_empty_view))
                .check(matches(isDisplayed()));
    }

    public void checkLegendGames() {
        onView(withId(R.id.card_view_games_help))
                .perform(click());

        onView(withText(R.string.text_legend_info_01))
                .check(matches(isDisplayed()));
    }

    public void checkPopupWindowOrderListGames() {
        onView(withId(R.id.btn_sort_list))
                .perform(click());

        onView(withText(R.string.text_game_sort_list_info_01))
                .check(matches(isDisplayed()));
    }

}
