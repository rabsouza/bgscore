package br.com.battista.bgscore.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import br.com.battista.bgscore.MainApplication
import br.com.battista.bgscore.activity.HomeActivity
import br.com.battista.bgscore.helper.HomeActivityHelper
import br.com.battista.bgscore.model.User
import br.com.battista.bgscore.robot.MatchRobot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MatchesFragmentTest {

    @Rule
    @JvmField
    var runtimePermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE)

    @Rule
    @JvmField
    var testRule = ActivityTestRule(HomeActivity::class.java, true, false)

    private lateinit var user: User
    private lateinit var context: Context
    private lateinit var matchRobot: MatchRobot

    @Before
    fun setup() {
        testRule.launchActivity(Intent())
        context = testRule.activity.applicationContext

        matchRobot = MatchRobot(context)

        val application = testRule.activity.application as MainApplication
        MainApplication.init(application, false)

        user = HomeActivityHelper.createNewUser()
        application.clearPreferences()
        application.user = user

        matchRobot.closeWelcomeDialog()
        matchRobot.navigationToMatches()
    }

    @Test
    fun shouldShowEmptyMatchesWhenFirstAccess() {
        matchRobot.checkEmptyMatches()
    }

    @Test
    fun shouldShowLegendRankingWhenClickIconLegend() {
        matchRobot.checkLegendMatches()
    }

    @Test
    fun shouldShowOrderPopupWindowWhenClickOrderList() {
        matchRobot.checkPopupWindowOrderListMatches()
    }

}
