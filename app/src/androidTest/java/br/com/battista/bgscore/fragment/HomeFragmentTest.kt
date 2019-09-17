package br.com.battista.bgscore.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import br.com.battista.bgscore.MainApplication
import br.com.battista.bgscore.R
import br.com.battista.bgscore.activity.HomeActivity
import br.com.battista.bgscore.helper.HomeActivityHelper
import br.com.battista.bgscore.model.User
import br.com.battista.bgscore.robot.HomeRobot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

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

        val application = testRule.activity.application as MainApplication
        MainApplication.init(application, false)

        user = HomeActivityHelper.createNewUser()
        application.clearPreferences()
        application.user = user

        homeRobot.closeWelcomeDialog()
        homeRobot.navigationToHome()
    }

    @Test
    fun shouldShowDataUserWhenFirstAccess() {
        homeRobot.checkTextUsername(user.username)
                .checkTextLastPlay("-")
                .checkDrawableUserAvatar(R.drawable.avatar_profile)
    }

    @Test
    fun shouldShowDataStaticsWhenFirstAccess() {
        homeRobot.checkScoreValueGames("00")
                .checkScoreValueMatches("00")
                .checkScoreValueTotalTime("00:00")
    }

    @Test
    fun shouldShowEmptyRankingWhenFirstAccess() {
        homeRobot.checkEmptyRanking()
    }

    @Test
    fun shouldShowLegendRankingWhenClickIconLegend() {
        homeRobot.checkLegendRanking()
    }

}
