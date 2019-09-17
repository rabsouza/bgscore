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
import br.com.battista.bgscore.robot.GameRobot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class GameFragmentTest {

    @Rule
    @JvmField
    var runtimePermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE)

    @Rule
    @JvmField
    var testRule = ActivityTestRule(HomeActivity::class.java, true, false)

    private lateinit var user: User
    private lateinit var context: Context
    private lateinit var gameRobot: GameRobot

    @Before
    fun setup() {
        testRule.launchActivity(Intent())
        context = testRule.activity.applicationContext

        gameRobot = GameRobot(context)

        val application = testRule.activity.application as MainApplication
        MainApplication.init(application, false)

        user = HomeActivityHelper.createNewUser()
        application.clearPreferences()
        application.user = user

        gameRobot.closeWelcomeDialog()
        gameRobot.navigationToGames()
    }

    @Test
    fun shouldShowDataStaticsGamesWhenFirstAccess() {
        gameRobot.checkScoreValueMyGames("00")
                .checkScoreValueFavoriteGames("00")
                .checkScoreValueWantGames("00")
    }

    @Test
    fun shouldShowEmptyGameWhenFirstAccess() {
        gameRobot.checkEmptyGames()
    }

    @Test
    fun shouldShowLegendGameWhenClickIconLegend() {
        gameRobot.checkLegendGames()
    }

    @Test
    fun shouldShowOrderPopupWindowWhenClickOrderList() {
        gameRobot.checkPopupWindowOrderListGames()
    }

}
