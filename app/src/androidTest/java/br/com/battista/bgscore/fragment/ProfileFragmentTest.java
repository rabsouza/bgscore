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
import br.com.battista.bgscore.robot.ProfileRobot;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTest {

    @Rule
    public ActivityTestRule<HomeActivity> testRule =
            new ActivityTestRule<>(HomeActivity.class, Boolean.TRUE, Boolean.FALSE);

    private User user;
    private Context context;
    private ProfileRobot profileRobot;

    @Before
    public void setup() {
        testRule.launchActivity(new Intent());
        context = testRule.getActivity().getApplicationContext();

        profileRobot = new ProfileRobot(context);

        MainApplication application = (MainApplication)
                testRule.getActivity().getApplication();
        MainApplication.init(application, Boolean.FALSE);

        user = HomeActivityHelper.createNewUser();
        application.clearPreferences();
        application.setUser(user);

        profileRobot.closeWelcomeDialog();
        profileRobot.navigationToAccount();
    }

    @Test
    public void shouldShowDataUserWhenFirstAccess() {
        profileRobot.checkTextUsername(user.getUsername())
                .checkTextMail(user.getMail())
                .checkTextDateCreated(user.getCreatedAt())
                .checkDrawableUserAvatar(R.drawable.avatar_profile);
    }

    @Test
    public void shouldShowEmptyFriendsWhenFirstAccess() {
        profileRobot.checkEmptyFriends();
    }

}
