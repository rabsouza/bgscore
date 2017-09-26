package br.com.battista.bgscore.testcase.step02;

import android.content.Context;
import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.HomeActivity;
import br.com.battista.bgscore.helper.HomeActivityHelper;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.robot.GameRobot;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ManageGameTest {

    private static final String YEAR_PUBLISHED =
            String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    @Rule
    public ActivityTestRule<HomeActivity> testRule =
            new ActivityTestRule<>(HomeActivity.class, Boolean.TRUE, Boolean.FALSE);

    private User user;
    private Context context;
    private GameRobot gameRobot;

    @Before
    public void setup() {
        testRule.launchActivity(new Intent());
        context = testRule.getActivity().getApplicationContext();

        gameRobot = new GameRobot(context);

        MainApplication application = (MainApplication)
                testRule.getActivity().getApplication();
        MainApplication.init(application);

        user = HomeActivityHelper.createNewUser();
        application.clearPreferences();
        application.setUser(user);

        gameRobot.closeWelcomeDialog();
        gameRobot.navigationToGames();
    }

    @Test
    public void shouldValidateStepInitialDataWhenFirstAccess() {
        gameRobot.checkScoreValueMyGames("00")
                .checkScoreValueFavoriteGames("00")
                .checkScoreValueWantGames("00")
                .checkEmptyGames();
    }

    @Test
    public void shouldValidateStepNewGameWhenAddNewGame01Manual() {
        shouldValidateStepInitialDataWhenFirstAccess();

        gameRobot.openNewGame()
                .fillTextName("Game 01")
                .checkMyGame(Boolean.TRUE)
                .checkFavorite(Boolean.FALSE)
                .checkWantGame(Boolean.FALSE)
                .fillTextYearPublished(YEAR_PUBLISHED)
                .fillTextAge("10")
                .fillTextMinPlayers("1")
                .fillTextMinPlayTime("30")
                .fillTextGameBadge(context.getString(R.string.badge_game_family))
                .saveGame();

        gameRobot.checkScoreValueMyGames("01")
                .checkScoreValueFavoriteGames("00")
                .checkScoreValueWantGames("00")
                .checkEmptyGames();
    }

}
