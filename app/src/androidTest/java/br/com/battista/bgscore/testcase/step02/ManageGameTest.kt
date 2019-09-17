package br.com.battista.bgscore.testcase.step02

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
import br.com.battista.bgscore.robot.GameRobot
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@LargeTest
@RunWith(AndroidJUnit4::class)
class ManageGameTest {

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

        gameRobot = GameRobot(context!!)

        val application = testRule.activity.application as MainApplication
        MainApplication.init(application, false)
        application.cleanAllDataDB()

        user = HomeActivityHelper.createNewUser()
        application.clearPreferences()
        application.user = user

        gameRobot.closeWelcomeDialog()
        gameRobot.navigationToGames()
    }

    @Test
    fun shouldValidateStepInitialDataWhenFirstAccess() {
        gameRobot.checkScoreValueMyGames("00")
                .checkScoreValueFavoriteGames("00")
                .checkScoreValueWantGames("00")
                .checkEmptyGames()
    }

    @Ignore("TODO: vou ignorar até resolver o problema do scroll nessa tela")
    @Test
    fun shouldValidateStepNewGameWhenAddNewGame01Manual() {
        shouldValidateStepInitialDataWhenFirstAccess()

        gameRobot.openNewGame()
                .fillTextName("Game 01")
                .checkMyGame(true)
                .checkFavorite(false)
                .checkWantGame(false)
                .fillTextYearPublished(YEAR_PUBLISHED)
                .fillTextAge("10")
                .fillTextMinPlayers("1")
                .fillTextMinPlayTime("30")
                .fillTextGameBadge(context.getString(R.string.badge_game_family))
                .saveGame()

        gameRobot.checkScoreValueMyGames("01")
                .checkScoreValueFavoriteGames("00")
                .checkScoreValueWantGames("00")
    }

    companion object {

        private val YEAR_PUBLISHED = Calendar.getInstance().get(Calendar.YEAR).toString()
    }

}
