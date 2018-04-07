package br.com.battista.bgscore.robot;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.battista.bgscore.helper.CustomViewMatcher.withCollapsibleToolbarTitle;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;

import br.com.battista.bgscore.R;

public class GameRobot extends BaseRobot {

    private Context context;

    public GameRobot(Context context) {
        this.context = context;
    }

    public GameRobot checkScoreValueMyGames(String value) {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_games_score_my_game))))
                .check(matches(isDisplayed()));
        return this;
    }

    public GameRobot checkScoreValueFavoriteGames(String value) {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_games_score_favorite))))
                .check(matches(isDisplayed()));
        return this;
    }

    public GameRobot checkScoreValueWantGames(String value) {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_games_score_want_game))))
                .check(matches(isDisplayed()));
        return this;
    }

    public GameRobot checkEmptyGames() {
        onView(withText(R.string.text_game_games_empty_view))
                .check(matches(isDisplayed()));
        return this;
    }

    public GameRobot checkLegendGames() {
        onView(withId(R.id.card_view_games_help))
                .perform(click());

        onView(withText(R.string.text_legend_info_01))
                .check(matches(isDisplayed()));
        return this;
    }

    public GameRobot checkPopupWindowOrderListGames() {
        onView(withId(R.id.btn_sort_list))
                .perform(click());

        onView(withText(R.string.text_game_sort_list_info_01))
                .check(matches(isDisplayed()));
        return this;
    }

    public NewGameRobot openNewGame() {
        onView(withId(R.id.fab_new_game))
                .perform(click());

        onView(isAssignableFrom(CollapsingToolbarLayout.class))
                .check(matches(withCollapsibleToolbarTitle(is(
                        context.getString(R.string.title_game)))));

        return new NewGameRobot(context);
    }

}
