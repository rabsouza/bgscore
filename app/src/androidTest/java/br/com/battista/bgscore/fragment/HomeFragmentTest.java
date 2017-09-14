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
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.HomeActivity;
import br.com.battista.bgscore.helper.HomeActivityHelper;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.robot.HomeRobot;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    @Rule
    public ActivityTestRule<HomeActivity> testRule =
            new ActivityTestRule<>(HomeActivity.class, Boolean.TRUE, Boolean.FALSE);

    private User user;
    private Context context;
    private HomeRobot homeRobot;

    @Before
    public void setup() {
        testRule.launchActivity(new Intent());
        context = testRule.getActivity().getApplicationContext();

        homeRobot = new HomeRobot(context);

        MainApplication application = (MainApplication)
                testRule.getActivity().getApplication();
        MainApplication.init(application);

        user = HomeActivityHelper.createNewUser();
        application.clearPreferences();
        application.setUser(user);

        homeRobot.closeWelcomeDialog();
        homeRobot.navigationToHome();
    }

    @Test
    public void shouldShowDataUserWhenFirstAccess() {
        homeRobot.checkTextUsername(user.getUsername())
                .checkTextLastPlay("-")
                .checkDrawableUserAvatar(R.drawable.avatar_profile);
    }

    @Test
    public void shouldShowDataStaticsWhenFirstAccess() {
        homeRobot.checkScoreValueGames("00")
                .checkScoreValueMatches("00")
                .checkScoreValueTotalTime("00:00");
    }

    @Test
    public void shouldShowEmptyRankingWhenFirstAccess() {
        homeRobot.checkEmptyRanking();
    }

    @Test
    public void shouldShowLegendRankingWhenClickIconLegend() {
        homeRobot.checkLegendRanking();
    }

}
