package br.com.battista.bgscore.robot

import android.content.Context
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import br.com.battista.bgscore.R
import org.hamcrest.Matchers.allOf

class MatchRobot(private val context: Context) : BaseRobot() {

    fun checkEmptyMatches(): MatchRobot {
        onView(allOf(withId(R.id.card_view_matches_score_very_dissatisfied),
                hasDescendant(withText(EMPTY_SCORE_VALUE))))
                .check(matches(isDisplayed()))
        onView(allOf(withId(R.id.card_view_matches_score_dissatisfied),
                hasDescendant(withText(EMPTY_SCORE_VALUE))))
                .check(matches(isDisplayed()))
        onView(allOf(withId(R.id.card_view_matches_score_neutral),
                hasDescendant(withText(EMPTY_SCORE_VALUE))))
                .check(matches(isDisplayed()))
        onView(allOf(withId(R.id.card_view_matches_score_satisfied),
                hasDescendant(withText(EMPTY_SCORE_VALUE))))
                .check(matches(isDisplayed()))
        onView(allOf(withId(R.id.card_view_matches_score_very_satisfied),
                hasDescendant(withText(EMPTY_SCORE_VALUE))))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkLegendMatches(): MatchRobot {
        onView(withId(R.id.card_view_matches_help))
                .perform(click())

        onView(withText(R.string.text_legend_info_01))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkPopupWindowOrderListMatches(): MatchRobot {
        onView(withId(R.id.btn_sort_list))
                .perform(click())

        onView(withText(R.string.text_match_sort_list_info_01))
                .check(matches(isDisplayed()))
        return this
    }

    companion object {

        private val EMPTY_SCORE_VALUE = "00"
    }

}
