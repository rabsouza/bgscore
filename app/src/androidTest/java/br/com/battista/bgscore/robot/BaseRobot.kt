package br.com.battista.bgscore.robot

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.Toolbar
import android.widget.TextView
import br.com.battista.bgscore.R
import br.com.battista.bgscore.helper.CustomViewMatcher.withBottomBarItemCheckedStatus
import org.hamcrest.Matchers.allOf


abstract class BaseRobot {

    protected fun doWait(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (var4: InterruptedException) {
            throw RuntimeException("Could not sleep.", var4)
        }

    }

    fun navigationToHome() = apply {
        onView(withId(R.id.action_home))
                .perform(click())

        onView(allOf(isAssignableFrom(TextView::class.java), withParent(isAssignableFrom(Toolbar::class.java))))
                .check(matches(withText(R.string.title_home)))
    }

    fun navigationToMatches() = apply {
        onView(withId(R.id.action_matches))
                .perform(click())

        onView(allOf(isAssignableFrom(TextView::class.java), withParent(isAssignableFrom(Toolbar::class.java))))
                .check(matches(withText(R.string.title_matches)))
    }

    fun navigationToGames() = apply {
        onView(withId(R.id.action_games))
                .perform(click())

        onView(allOf(isAssignableFrom(TextView::class.java), withParent(isAssignableFrom(Toolbar::class.java))))
                .check(matches(withText(R.string.title_games)))
    }

    fun navigationToAccount() = apply {
        onView(withId(R.id.action_account))
                .perform(click())

        onView(allOf(isAssignableFrom(TextView::class.java), withParent(isAssignableFrom(Toolbar::class.java))))
                .check(matches(withText(R.string.title_account)))
    }

    fun navigationToInfo() = apply {
        onView(withId(R.id.action_info))
                .perform(click())

        onView(withText(R.string.title_info))
                .check(matches(isDisplayed()))
    }

    fun checkBottomBarHomeChecked() = apply {
        onView(withId(R.id.action_home))
                .check(matches(withBottomBarItemCheckedStatus(true)))
    }

    fun checkBottomBarMatchesChecked() = apply {
        onView(withId(R.id.action_matches))
                .check(matches(withBottomBarItemCheckedStatus(true)))
    }

    fun checkBottomBarGamesChecked() = apply {
        onView(withId(R.id.action_games))
                .check(matches(withBottomBarItemCheckedStatus(true)))
    }

    fun checkBottomBarAccountChecked() = apply {
        onView(withId(R.id.action_account))
                .check(matches(withBottomBarItemCheckedStatus(true)))
    }

    fun closeWelcomeDialog() = apply {
        try {
            onView(withText(R.string.btn_ok))
                    .perform(click())
        } catch (e: Exception) {
        }
    }

    companion object {
        val DO_WAIT_MILLIS = 500
    }

}
