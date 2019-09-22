package br.com.battista.bgscore.testcase.step01

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
import br.com.battista.bgscore.robot.ProfileRobot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@LargeTest
@RunWith(AndroidJUnit4::class)
class ManageProfileTest {

    @Rule
    @JvmField
    var runtimePermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE)

    @Rule
    @JvmField
    var testRule = ActivityTestRule(HomeActivity::class.java, true, false)

    private lateinit var user: User
    private lateinit var context: Context
    private lateinit var profileRobot: ProfileRobot

    @Before
    fun setup() {
        testRule.launchActivity(Intent())
        context = testRule.activity.applicationContext

        profileRobot = ProfileRobot(context)

        val application = testRule.activity.application as MainApplication
        MainApplication.init(application, false)

        user = HomeActivityHelper.createNewUser()
        application.clearPreferences()
        application.user = user

        profileRobot.closeWelcomeDialog()
        profileRobot.navigationToAccount()
    }

    @Test
    fun shouldValidateStepInitialDataWhenFirstAccess() {
        profileRobot.checkTextUsername(user.username)
                .checkTextMail(user.mail)
                .checkTextDateCreated(user.createdAt)
                .checkDrawableUserAvatar(R.drawable.avatar_profile)
                .checkEmptyFriends()
    }

    @Test
    fun shouldValidateStepUserDataChangeWhenChangeUsernameMail() {
        shouldValidateStepInitialDataWhenFirstAccess()

        val username = "teste02"
        val mail = "teste02@teste.com"
        profileRobot.openChangeProfile()
                .changeTextUsername(username)
                .changeTextMail(mail)
                .saveChangeProfile()

        profileRobot.checkTextUsername(username)
                .checkTextMail(mail)
                .checkTextDateCreated(user.createdAt)
                .checkDrawableUserAvatar(R.drawable.avatar_profile)
                .checkEmptyFriends()
    }

    @Test
    fun shouldValidateStepDiscardUserDataChangeWhenCancelChangeUsernameMail() {
        shouldValidateStepInitialDataWhenFirstAccess()

        val username = "teste02"
        val mail = "teste02@teste.com"
        profileRobot.openChangeProfile()
                .changeTextUsername(username)
                .changeTextMail(mail)
                .cancelChangeProfile()

        shouldValidateStepInitialDataWhenFirstAccess()
    }

    @Test
    fun shouldValidateStepResetUserWhenResetUser() {
        shouldValidateStepInitialDataWhenFirstAccess()

        profileRobot.openChangeProfile()
                .resetDataProfile()
                .saveChangeProfile()

        profileRobot.checkTextUsername(context.getString(R.string.text_default_username))
                .checkTextMail("-")
                .checkTextDateCreated(Date())
                .checkDrawableUserAvatar(R.drawable.avatar_profile)
                .checkEmptyFriends()
    }

    @Test
    fun shouldValidateStepDiscardResetUserWhenCancelResetUser() {
        shouldValidateStepInitialDataWhenFirstAccess()

        profileRobot.openChangeProfile()
                .resetDataProfile()
                .cancelChangeProfile()

        shouldValidateStepInitialDataWhenFirstAccess()
    }

    @Test
    fun shouldValidateStepChangeAvatarWhenChangeAvatar() {
        shouldValidateStepInitialDataWhenFirstAccess()

        profileRobot.openChangeAvatar()
                .changeAvatar(context.getString(R.string.avatar_c3p0))
                .saveChangeAvatar()

        profileRobot.checkTextUsername(user.username)
                .checkTextMail(user.mail)
                .checkTextDateCreated(user.createdAt)
                .checkDrawableUserAvatar(R.drawable.avatar_c3p0)
                .checkEmptyFriends()
    }

    @Test
    fun shouldValidateStepDiscardChangeAvatarWhenCancelChangeAvatar() {
        shouldValidateStepInitialDataWhenFirstAccess()

        profileRobot.openChangeAvatar()
                .changeAvatar(context.getString(R.string.avatar_c3p0))
                .cancelChangeAvatar()

        shouldValidateStepInitialDataWhenFirstAccess()
    }

    @Test
    fun shouldValidateStepNewFriendsWhenAddNewFriend() {
        shouldValidateStepInitialDataWhenFirstAccess()

        val friend = "friend01"
        profileRobot.addNewFriend(friend)

        profileRobot.checkTextUsername(user.username)
                .checkTextMail(user.mail)
                .checkTextDateCreated(user.createdAt)
                .checkDrawableUserAvatar(R.drawable.avatar_profile)
                .checkFriend(friend, 0)
    }

    @Test
    fun shouldValidateStepNewFriendsWhenMoreFriends() {
        shouldValidateStepInitialDataWhenFirstAccess()

        for (i in 0..9) {
            val friend = "friend0$i"
            profileRobot.addNewFriend(friend)
                    .checkFriend(friend, 0)
        }
        profileRobot.checkTextUsername(user.username)
                .checkTextMail(user.mail)
                .checkTextDateCreated(user.createdAt)
                .checkDrawableUserAvatar(R.drawable.avatar_profile)
    }

    @Test
    fun shouldValidateStepFullDataWhenFillAllData() {
        shouldValidateStepInitialDataWhenFirstAccess()

        val username = "teste02"
        val mail = "teste02@teste.com"
        profileRobot.openChangeProfile()
                .changeTextUsername(username)
                .changeTextMail(mail)
                .saveChangeProfile()

        profileRobot.openChangeAvatar()
                .changeAvatar(context.getString(R.string.avatar_c3p0))
                .saveChangeAvatar()

        var friend = "friend00"
        profileRobot.addNewFriend(friend)
                .checkFriend(friend, 0)

        friend = "friend01"
        profileRobot.addNewFriend(friend)
                .checkFriend(friend, 0)

        friend = "friend02"
        profileRobot.addNewFriend(friend)
                .checkFriend(friend, 0)

        profileRobot.checkTextUsername(username)
                .checkTextMail(mail)
                .checkTextDateCreated(user.createdAt)
                .checkDrawableUserAvatar(R.drawable.avatar_c3p0)
    }

    @Test
    fun shouldValidateStepHomeDataWhenFillAllData() {
        shouldValidateStepInitialDataWhenFirstAccess()

        val username = "teste02"
        val mail = "teste02@teste.com"
        profileRobot.openChangeProfile()
                .changeTextUsername(username)
                .changeTextMail(mail)
                .saveChangeProfile()

        profileRobot.openChangeAvatar()
                .changeAvatar(context.getString(R.string.avatar_c3p0))
                .saveChangeAvatar()

        var friend = "friend00"
        profileRobot.addNewFriend(friend)
                .checkFriend(friend, 0)

        friend = "friend01"
        profileRobot.addNewFriend(friend)
                .checkFriend(friend, 0)

        friend = "friend02"
        profileRobot.addNewFriend(friend)
                .checkFriend(friend, 0)

        profileRobot.checkTextUsername(username)
                .checkTextMail(mail)
                .checkTextDateCreated(user.createdAt)
                .checkDrawableUserAvatar(R.drawable.avatar_c3p0)

        val homeRobot = HomeRobot(context)
        homeRobot.navigationToHome()
        homeRobot.checkTextUsername(username)
                .checkDrawableUserAvatar(R.drawable.avatar_c3p0)
    }

}
