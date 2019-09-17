package br.com.battista.bgscore.activity

import android.Manifest
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.Toolbar
import android.widget.TextView
import br.com.battista.bgscore.MainApplication
import br.com.battista.bgscore.R
import br.com.battista.bgscore.helper.HomeActivityHelper
import br.com.battista.bgscore.model.User
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @Rule
    @JvmField
    var runtimePermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE)

    @Rule
    @JvmField
    var testRule = ActivityTestRule(HomeActivity::class.java, true, false)

    private lateinit var user: User

    @Before
    fun setup() {
        testRule.launchActivity(Intent())

        val application = MainApplication.instance()

        user = HomeActivityHelper.createNewUser()
        application.clearPreferences()
        application.setUser(user)
    }

    @Test
    fun checkExistsTheElementsActivity() {
        val activity = testRule.activity

        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        assertNotNull(toolbar)

        val bottomNavigationView = activity.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        assertNotNull(bottomNavigationView)
    }

    @Test
    fun checkExistMenuInBottomNavigationView() {
        val activity = testRule.activity

        val bottomNavigationView = activity.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        assertNotNull(bottomNavigationView)

        assertThat(bottomNavigationView.menu.size(), equalTo(5))

        assertNotNull(bottomNavigationView.menu.findItem(R.id.action_home))
        assertNotNull(bottomNavigationView.menu.findItem(R.id.action_matches))
        assertNotNull(bottomNavigationView.menu.findItem(R.id.action_games))
        assertNotNull(bottomNavigationView.menu.findItem(R.id.action_account))
        assertNotNull(bottomNavigationView.menu.findItem(R.id.action_info))
    }

    @Test
    fun shouldShowHomeWhenClickActionHome() {
        try {
            onView(withText(R.string.btn_ok))
                    .perform(click())
        } catch (e: Exception) {
        }

        onView(withId(R.id.action_home))
                .perform(click())

        onView(allOf(isAssignableFrom(TextView::class.java), withParent(isAssignableFrom(Toolbar::class.java))))
                .check(matches(withText(R.string.title_home)))
    }

    @Test
    fun shouldShowMatchesWhenClickActionMatches() {
        try {
            onView(withText(R.string.btn_ok))
                    .perform(click())
        } catch (e: Exception) {
        }

        onView(withId(R.id.action_matches))
                .perform(click())

        onView(allOf(isAssignableFrom(TextView::class.java), withParent(isAssignableFrom(Toolbar::class.java))))
                .check(matches(withText(R.string.title_matches)))
    }

    @Test
    fun shouldShowGamesWhenClickActionGames() {
        try {
            onView(withText(R.string.btn_ok))
                    .perform(click())
        } catch (e: Exception) {
        }

        onView(withId(R.id.action_games))
                .perform(click())

        onView(allOf(isAssignableFrom(TextView::class.java), withParent(isAssignableFrom(Toolbar::class.java))))
                .check(matches(withText(R.string.title_games)))
    }

    @Test
    fun shouldShowAccountWhenClickActionAccount() {
        try {
            onView(withText(R.string.btn_ok))
                    .perform(click())
        } catch (e: Exception) {
        }

        onView(withId(R.id.action_account))
                .perform(click())

        onView(allOf(isAssignableFrom(TextView::class.java), withParent(isAssignableFrom(Toolbar::class.java))))
                .check(matches(withText(R.string.title_account)))
    }

    @Test
    fun shouldShowInfoWhenClickActionInfo() {
        try {
            onView(withText(R.string.btn_ok))
                    .perform(click())
        } catch (e: Exception) {
        }

        onView(withId(R.id.action_info))
                .perform(click())

        onView(withText(R.string.title_info))
                .check(matches(isDisplayed()))
    }

}
