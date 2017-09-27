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
import br.com.battista.bgscore.robot.MatchRobot;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MatchesFragmentTest {

    @Rule
    public ActivityTestRule<HomeActivity> testRule =
            new ActivityTestRule<>(HomeActivity.class, Boolean.TRUE, Boolean.FALSE);

    private User user;
    private Context context;
    private MatchRobot matchRobot;

    @Before
    public void setup() {
        testRule.launchActivity(new Intent());
        context = testRule.getActivity().getApplicationContext();

        matchRobot = new MatchRobot(context);

        MainApplication application = (MainApplication)
                testRule.getActivity().getApplication();
        MainApplication.init(application, Boolean.FALSE);

        user = HomeActivityHelper.createNewUser();
        application.clearPreferences();
        application.setUser(user);

        matchRobot.closeWelcomeDialog();
        matchRobot.navigationToMatches();
    }

    @Test
    public void shouldShowEmptyMatchesWhenFirstAccess() {
        matchRobot.checkEmptyMatches();
    }

    @Test
    public void shouldShowLegendRankingWhenClickIconLegend() {
        matchRobot.checkLegendMatches();
    }

    @Test
    public void shouldShowOrderPopupWindowWhenClickOrderList() {
        matchRobot.checkPopupWindowOrderListMatches();
    }

}
