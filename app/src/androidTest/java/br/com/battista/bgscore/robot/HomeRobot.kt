package br.com.battista.bgscore.robot

import android.content.Context
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import br.com.battista.bgscore.R
import br.com.battista.bgscore.helper.DrawableMatcher
import org.hamcrest.Matchers.allOf

class HomeRobot(private val context: Context) : BaseRobot() {

    fun checkDrawableUserAvatar(avatar_profile: Int): HomeRobot {
        onView(withId(R.id.card_view_home_img)).check(matches(
                DrawableMatcher.withDrawable(avatar_profile)))
        return this
    }

    fun checkTextLastPlay(lastPlay: String): HomeRobot {
        onView(withId(R.id.card_view_home_last_play))
                .check(matches(
                        withText(context.getString(R.string.text_home_last_play, lastPlay))))
        return this
    }

    fun checkTextUsername(username: String): HomeRobot {
        onView(withId(R.id.card_view_home_username))
                .check(matches(
                        withText(context.getString(R.string.text_home_username, username))))
        return this
    }

    fun checkScoreValueTotalTime(value: String): HomeRobot {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_score_total_time))))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkScoreValueMatches(value: String): HomeRobot {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_score_matches))))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkScoreValueGames(value: String): HomeRobot {
        onView(allOf(withText(value),
                withParent(withId(R.id.card_view_score_games))))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkEmptyRanking(): HomeRobot {
        onView(withText(R.string.text_home_stats_empty_view))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkLegendRanking(): HomeRobot {
        onView(withId(R.id.card_view_stats_help))
                .perform(click())

        onView(withText(R.string.text_legend_info_01))
                .check(matches(isDisplayed()))
        return this
    }

}
