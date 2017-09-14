package br.com.battista.bgscore.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.activity.HomeActivity;
import br.com.battista.bgscore.helper.HomeActivityHelper;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.robot.GameRobot;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GameFragmentTest {

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
    public void shouldShowDataStaticsGamesWhenFirstAccess() {
        gameRobot.checkScoreValueMyGames("00")
                .checkScoreValueFavoriteGames("00")
                .checkScoreValueWantGames("00");
    }

    @Test
    public void shouldShowEmptyGameWhenFirstAccess() {
        gameRobot.checkEmptyGames();
    }

    @Test
    public void shouldShowLegendGameWhenClickIconLegend() {
        gameRobot.checkLegendGames();
    }

    @Test
    public void shouldShowOrderPopupWindowWhenClickOrderList() {
        gameRobot.checkPopupWindowOrderListGames();
    }

}
