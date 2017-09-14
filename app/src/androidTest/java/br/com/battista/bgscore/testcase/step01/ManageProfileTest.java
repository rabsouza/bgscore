package br.com.battista.bgscore.testcase.step01;

import android.content.Context;
import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.HomeActivity;
import br.com.battista.bgscore.helper.HomeActivityHelper;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.robot.HomeRobot;
import br.com.battista.bgscore.robot.ProfileRobot;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ManageProfileTest {

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
        MainApplication.init(application);

        user = HomeActivityHelper.createNewUser();
        application.clearPreferences();
        application.setUser(user);

        profileRobot.closeWelcomeDialog();
        profileRobot.navigationToAccount();
    }

    @Test
    public void shouldValidateStepInitialDataWhenFirstAccess() {
        profileRobot.checkTextUsername(user.getUsername())
                .checkTextMail(user.getMail())
                .checkTextDateCreated(user.getCreatedAt())
                .checkDrawableUserAvatar(R.drawable.avatar_profile)
                .checkEmptyFriends();
    }

    @Test
    public void shouldValidateStepUserDataChangeWhenChangeUsernameMail() {
        shouldValidateStepInitialDataWhenFirstAccess();

        String username = "teste02";
        String mail = "teste02@teste.com";
        profileRobot.openChangeProfile()
                .changeTextUsername(username)
                .changeTextMail(mail)
                .saveChangeProfile();

        profileRobot.checkTextUsername(username)
                .checkTextMail(mail)
                .checkTextDateCreated(user.getCreatedAt())
                .checkDrawableUserAvatar(R.drawable.avatar_profile)
                .checkEmptyFriends();
    }

    @Test
    public void shouldValidateStepDiscardUserDataChangeWhenCancelChangeUsernameMail() {
        shouldValidateStepInitialDataWhenFirstAccess();

        String username = "teste02";
        String mail = "teste02@teste.com";
        profileRobot.openChangeProfile()
                .changeTextUsername(username)
                .changeTextMail(mail)
                .cancelChangeProfile();

        shouldValidateStepInitialDataWhenFirstAccess();
    }

    @Test
    public void shouldValidateStepResetUserWhenResetUser() {
        shouldValidateStepInitialDataWhenFirstAccess();

        profileRobot.openChangeProfile()
                .resetDataProfile()
                .saveChangeProfile();

        profileRobot.checkTextUsername(context.getString(R.string.text_default_username))
                .checkTextMail("-")
                .checkTextDateCreated(new Date())
                .checkDrawableUserAvatar(R.drawable.avatar_profile)
                .checkEmptyFriends();
    }

    @Test
    public void shouldValidateStepDiscardResetUserWhenCancelResetUser() {
        shouldValidateStepInitialDataWhenFirstAccess();

        profileRobot.openChangeProfile()
                .resetDataProfile()
                .cancelChangeProfile();

        shouldValidateStepInitialDataWhenFirstAccess();
    }

    @Test
    public void shouldValidateStepChangeAvatarWhenChangeAvatar() {
        shouldValidateStepInitialDataWhenFirstAccess();

        profileRobot.openChangeAvatar()
                .changeAvatar(context.getString(R.string.avatar_c3p0))
                .saveChangeAvatar();

        profileRobot.checkTextUsername(user.getUsername())
                .checkTextMail(user.getMail())
                .checkTextDateCreated(user.getCreatedAt())
                .checkDrawableUserAvatar(R.drawable.avatar_c3p0)
                .checkEmptyFriends();
    }

    @Test
    public void shouldValidateStepDiscardChangeAvatarWhenCancelChangeAvatar() {
        shouldValidateStepInitialDataWhenFirstAccess();

        profileRobot.openChangeAvatar()
                .changeAvatar(context.getString(R.string.avatar_c3p0))
                .cancelChangeAvatar();

        shouldValidateStepInitialDataWhenFirstAccess();
    }

    @Test
    public void shouldValidateStepNewFriendsWhenAddNewFriend() {
        shouldValidateStepInitialDataWhenFirstAccess();

        String friend = "friend01";
        profileRobot.addNewFriend(friend);

        profileRobot.checkTextUsername(user.getUsername())
                .checkTextMail(user.getMail())
                .checkTextDateCreated(user.getCreatedAt())
                .checkDrawableUserAvatar(R.drawable.avatar_profile)
                .checkFriend(friend, 0);
    }

    @Test
    public void shouldValidateStepNewFriendsWhenMoreFriends() {
        shouldValidateStepInitialDataWhenFirstAccess();

        for (int i = 0; i < 10; i++) {
            String friend = "friend0" + i;
            profileRobot.addNewFriend(friend)
                    .checkFriend(friend, 0);
        }
        profileRobot.checkTextUsername(user.getUsername())
                .checkTextMail(user.getMail())
                .checkTextDateCreated(user.getCreatedAt())
                .checkDrawableUserAvatar(R.drawable.avatar_profile);
    }

    @Test
    public void shouldValidateStepFullDataWhenFillAllData() {
        shouldValidateStepInitialDataWhenFirstAccess();

        String username = "teste02";
        String mail = "teste02@teste.com";
        profileRobot.openChangeProfile()
                .changeTextUsername(username)
                .changeTextMail(mail)
                .saveChangeProfile();

        profileRobot.openChangeAvatar()
                .changeAvatar(context.getString(R.string.avatar_c3p0))
                .saveChangeAvatar();

        String friend = "friend00";
        profileRobot.addNewFriend(friend)
                .checkFriend(friend, 0);

        friend = "friend01";
        profileRobot.addNewFriend(friend)
                .checkFriend(friend, 0);

        friend = "friend02";
        profileRobot.addNewFriend(friend)
                .checkFriend(friend, 0);

        profileRobot.checkTextUsername(username)
                .checkTextMail(mail)
                .checkTextDateCreated(user.getCreatedAt())
                .checkDrawableUserAvatar(R.drawable.avatar_c3p0);
    }

    @Test
    public void shouldValidateStepHomeDataWhenFillAllData() {
        shouldValidateStepInitialDataWhenFirstAccess();

        String username = "teste02";
        String mail = "teste02@teste.com";
        profileRobot.openChangeProfile()
                .changeTextUsername(username)
                .changeTextMail(mail)
                .saveChangeProfile();

        profileRobot.openChangeAvatar()
                .changeAvatar(context.getString(R.string.avatar_c3p0))
                .saveChangeAvatar();

        String friend = "friend00";
        profileRobot.addNewFriend(friend)
                .checkFriend(friend, 0);

        friend = "friend01";
        profileRobot.addNewFriend(friend)
                .checkFriend(friend, 0);

        friend = "friend02";
        profileRobot.addNewFriend(friend)
                .checkFriend(friend, 0);

        profileRobot.checkTextUsername(username)
                .checkTextMail(mail)
                .checkTextDateCreated(user.getCreatedAt())
                .checkDrawableUserAvatar(R.drawable.avatar_c3p0);

        HomeRobot homeRobot = new HomeRobot(context);
        homeRobot.navigationToHome();
        homeRobot.checkTextUsername(username)
        .checkDrawableUserAvatar(R.drawable.avatar_c3p0);
    }

}
