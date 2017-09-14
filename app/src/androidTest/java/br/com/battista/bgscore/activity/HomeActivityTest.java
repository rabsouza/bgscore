package br.com.battista.bgscore.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.helper.HomeActivityHelper;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.robot.HomeRobot;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Rule
    public ActivityTestRule<HomeActivity> testRule =
            new ActivityTestRule<>(HomeActivity.class, Boolean.TRUE, Boolean.FALSE);

    private User user;
    private HomeRobot homeRobot;

    @Before
    public void setup() {
        testRule.launchActivity(new Intent());

        MainApplication application = (MainApplication)
                testRule.getActivity().getApplication();
        MainApplication.init(application);

        Context context = testRule.getActivity().getApplicationContext();
        homeRobot = new HomeRobot(context);

        user = HomeActivityHelper.createNewUser();
        application.clearPreferences();
        application.setUser(user);
    }

    @Test
    public void checkExistsTheElementsActivity() {
        HomeActivity activity = testRule.getActivity();

        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        assertNotNull(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.bottom_navigation);
        assertNotNull(bottomNavigationView);
    }

    @Test
    public void checkExistMenuInBottomNavigationView() {
        HomeActivity activity = testRule.getActivity();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.bottom_navigation);
        assertNotNull(bottomNavigationView);

        assertThat(bottomNavigationView.getMenu().size(), equalTo(5));

        assertNotNull(bottomNavigationView.getMenu().findItem(R.id.action_home));
        assertNotNull(bottomNavigationView.getMenu().findItem(R.id.action_matches));
        assertNotNull(bottomNavigationView.getMenu().findItem(R.id.action_games));
        assertNotNull(bottomNavigationView.getMenu().findItem(R.id.action_account));
        assertNotNull(bottomNavigationView.getMenu().findItem(R.id.action_info));
    }

    @Test
    public void shouldShowHomeWhenClickActionHome() {
        homeRobot.closeWelcomeDialog();
        homeRobot.navigationToHome();
    }

    @Test
    public void shouldShowMatchesWhenClickActionMatches() {
        homeRobot.closeWelcomeDialog();
        homeRobot.navigationToMatches();
    }

    @Test
    public void shouldShowGamesWhenClickActionGames() {
        homeRobot.closeWelcomeDialog();
        homeRobot.navigationToGames();
    }

    @Test
    public void shouldShowAccountWhenClickActionAccount() {
        homeRobot.closeWelcomeDialog();
        homeRobot.navigationToAccount();
    }

    @Test
    public void shouldShowInfoWhenClickActionInfo() {
        homeRobot.closeWelcomeDialog();
        homeRobot.navigationToInfo();
    }

}
