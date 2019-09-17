package br.com.battista.bgscore.robot

import android.content.Context
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import br.com.battista.bgscore.R
import br.com.battista.bgscore.helper.NestedScrollViewScrollToAction

class NewGameRobot(private val context: Context) : BaseRobot() {

    private fun checkStateCheckedView(checked: Boolean, idRes: Int) {
        if (checked) {
            onView(withId(idRes))
                    .check(matches(isChecked()))
        } else {
            onView(withId(idRes))
                    .check(matches(isNotChecked()))
        }
    }

    fun fillTextName(name: String): NewGameRobot {
        onView(withId(R.id.card_view_new_game_name))
                .perform(NestedScrollViewScrollToAction.scrollTo())

        onView(withId(R.id.card_view_new_game_name))
                .perform(replaceText(name), closeSoftKeyboard())
        return this
    }

    fun checkMyGame(checked: Boolean): NewGameRobot {
        onView(withId(R.id.card_view_new_game_my_game))
                .perform(NestedScrollViewScrollToAction.scrollTo())

        checkStateCheckedView(checked, R.id.card_view_new_game_my_game)
        return this
    }

    fun checkFavorite(checked: Boolean): NewGameRobot {
        onView(withId(R.id.card_view_new_game_favorite))
                .perform(NestedScrollViewScrollToAction.scrollTo())

        checkStateCheckedView(checked, R.id.card_view_new_game_favorite)
        return this
    }

    fun checkWantGame(checked: Boolean): NewGameRobot {
        onView(withId(R.id.card_view_new_game_want_game))
                .perform(NestedScrollViewScrollToAction.scrollTo())

        checkStateCheckedView(checked, R.id.card_view_new_game_want_game)
        return this
    }

    fun fillTextYearPublished(yearPublished: String): NewGameRobot {
        onView(withId(R.id.card_view_new_game_year_published))
                .perform(NestedScrollViewScrollToAction.scrollTo())

        onView(withId(R.id.card_view_new_game_year_published))
                .perform(replaceText(yearPublished), closeSoftKeyboard())
        return this
    }

    fun fillTextAge(age: String): NewGameRobot {
        onView(withId(R.id.card_view_new_game_age))
                .perform(NestedScrollViewScrollToAction.scrollTo())

        onView(withId(R.id.card_view_new_game_age))
                .perform(replaceText(age), closeSoftKeyboard())
        return this
    }

    fun fillTextMinPlayers(minPlayers: String): NewGameRobot {
        onView(withId(R.id.card_view_new_game_min_players))
                .perform(NestedScrollViewScrollToAction.scrollTo())

        onView(withId(R.id.card_view_new_game_min_players))
                .perform(replaceText(minPlayers), closeSoftKeyboard())
        return this
    }

    fun fillTextMaxPlayers(maxPlayers: String): NewGameRobot {
        onView(withId(R.id.card_view_new_game_max_players))
                .perform(NestedScrollViewScrollToAction.scrollTo())

        onView(withId(R.id.card_view_new_game_max_players))
                .perform(replaceText(maxPlayers), closeSoftKeyboard())
        return this
    }

    fun fillTextMinPlayTime(minPlayTime: String): NewGameRobot {
        onView(withId(R.id.card_view_new_game_min_play_time))
                .perform(NestedScrollViewScrollToAction.scrollTo())

        onView(withId(R.id.card_view_new_game_min_play_time))
                .perform(replaceText(minPlayTime), closeSoftKeyboard())
        return this
    }

    fun fillTextMaxPlayTime(maxPlayTime: String): NewGameRobot {
        onView(withId(R.id.card_view_new_game_max_play_time))
                .perform(NestedScrollViewScrollToAction.scrollTo())

        onView(withId(R.id.card_view_new_game_max_play_time))
                .perform(replaceText(maxPlayTime), closeSoftKeyboard())
        return this
    }

    fun fillTextGameBadge(gameBadge: String): NewGameRobot {
        onView(withId(R.id.card_view_new_game_badge_game))
                .perform(NestedScrollViewScrollToAction.scrollTo())

        onView(withId(R.id.card_view_new_game_badge_game))
                .perform(replaceText(gameBadge), closeSoftKeyboard())
        return this
    }

    fun saveGame(): GameRobot {
        onView(withId(R.id.fab_next_done_game))
                .perform(NestedScrollViewScrollToAction.scrollTo())

        onView(withId(R.id.fab_next_done_game))
                .perform(click())
        return GameRobot(context)
    }

}
