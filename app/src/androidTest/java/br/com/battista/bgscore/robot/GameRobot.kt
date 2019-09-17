package br.com.battista.bgscore.robot

import android.content.Context
import android.support.design.widget.CollapsingToolbarLayout
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import br.com.battista.bgscore.R
import br.com.battista.bgscore.helper.CustomViewMatcher.withCollapsibleToolbarTitle
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf

class GameRobot(private val context: Context) : BaseRobot() {

    fun checkScoreValueMyGames(value: String): GameRobot {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_games_score_my_game))))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkScoreValueFavoriteGames(value: String): GameRobot {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_games_score_favorite))))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkScoreValueWantGames(value: String): GameRobot {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_games_score_want_game))))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkEmptyGames(): GameRobot {
        onView(allOf(withId(R.id.card_view_games_score_my_game),
                hasDescendant(withText(EMPTY_SCORE_VALUE))))
                .check(matches(isDisplayed()))
        onView(allOf(withId(R.id.card_view_games_score_favorite),
                hasDescendant(withText(EMPTY_SCORE_VALUE))))
                .check(matches(isDisplayed()))
        onView(allOf(withId(R.id.card_view_games_score_want_game),
                hasDescendant(withText(EMPTY_SCORE_VALUE))))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkLegendGames(): GameRobot {
        onView(withId(R.id.card_view_games_help))
                .perform(click())

        onView(withText(R.string.text_legend_info_01))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkPopupWindowOrderListGames(): GameRobot {
        onView(withId(R.id.btn_sort_list))
                .perform(click())

        onView(withText(R.string.text_game_sort_list_info_01))
                .check(matches(isDisplayed()))
        return this
    }

    fun openNewGame(): NewGameRobot {
        onView(withId(R.id.fab_new_game))
                .perform(click())

        onView(isAssignableFrom(CollapsingToolbarLayout::class.java))
                .check(matches(withCollapsibleToolbarTitle(`is`<String>(
                        context.getString(R.string.title_game)))))

        return NewGameRobot(context)
    }

    companion object {

        private val EMPTY_SCORE_VALUE = "00"
    }

}
