package br.com.battista.bgscore.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.Toolbar
import br.com.battista.bgscore.MainApplication
import br.com.battista.bgscore.R
import br.com.battista.bgscore.helper.HomeActivityHelper
import br.com.battista.bgscore.model.User
import br.com.battista.bgscore.robot.HomeRobot
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
    private lateinit var context: Context
    private lateinit var homeRobot: HomeRobot

    @Before
    fun setup() {
        testRule.launchActivity(Intent())
        context = testRule.activity.applicationContext

        homeRobot = HomeRobot(context)

        val application = MainApplication.instance()

        user = HomeActivityHelper.createNewUser()
        application.clearPreferences()
        application.user = user

        homeRobot.closeWelcomeDialog()
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
        assertNotNull(bottomNavigationView.menu.findItem(R.id.action_more))
    }

    @Test
    fun shouldShowHomeWhenClickActionHome() {
        homeRobot.navigationToHome()
                .checkBottomBarHomeChecked()
    }

    @Test
    fun shouldShowMatchesWhenClickActionMatches() {
        homeRobot.navigationToMatches()
                .checkBottomBarMatchesChecked()
    }

    @Test
    fun shouldShowGamesWhenClickActionGames() {
        homeRobot.navigationToGames()
                .checkBottomBarGamesChecked()
    }

    @Test
    fun shouldShowAccountWhenClickActionAccount() {
        homeRobot.navigationToAccount()
                .checkBottomBarAccountChecked()
    }

}
