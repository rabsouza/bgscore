package br.com.battista.bgscore.robot

import android.content.Context
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.RecyclerView.ViewHolder
import br.com.battista.bgscore.R
import br.com.battista.bgscore.helper.DrawableMatcher
import br.com.battista.bgscore.util.DateUtils
import java.util.*

class ProfileRobot(private val context: Context) : BaseRobot() {

    fun checkDrawableUserAvatar(avatar_profile: Int): ProfileRobot {
        onView(withId(R.id.card_view_profile_img)).check(matches(
                DrawableMatcher.withDrawable(avatar_profile)))
        return this
    }

    fun checkTextUsername(username: String): ProfileRobot {
        onView(withId(R.id.card_view_profile_username))
                .check(matches(
                        withText(context.getString(R.string.text_username, username))))
        return this
    }

    fun changeTextUsername(username: String): ProfileRobot {
        onView(withId(R.id.dialog_view_edit_profile_username))
                .perform(replaceText(username), closeSoftKeyboard())
        return this
    }

    fun checkTextMail(mail: String): ProfileRobot {
        onView(withId(R.id.card_view_profile_mail))
                .check(matches(
                        withText(context.getString(R.string.text_mail, mail))))
        return this
    }

    fun changeTextMail(mail: String): ProfileRobot {
        onView(withId(R.id.dialog_view_edit_profile_mail))
                .perform(replaceText(mail), closeSoftKeyboard())
        return this
    }

    fun checkTextDateCreated(dateCreated: Date): ProfileRobot {
        onView(withId(R.id.card_view_profile_date_created))
                .check(matches(
                        withText(context.getString(R.string.text_date_created,
                                DateUtils.format(dateCreated)))))
        return this
    }

    fun checkEmptyFriends(): ProfileRobot {
        onView(withText(R.string.text_profile_friends_empty_view))
                .check(matches(isDisplayed()))
        return this
    }

    fun checkFriend(friend: String, position: Int): ProfileRobot {
        onView(withId(R.id.card_view_friends_recycler_view))
                .perform(scrollToPosition<ViewHolder>(position))
                .check(matches(hasDescendant(withText(friend))))
        return this
    }

    fun openChangeProfile(): ProfileRobot {
        onView(withId(R.id.button_profile_edit_profile))
                .perform(click())
        return this
    }

    fun resetDataProfile(): ProfileRobot {
        onView(withId(R.id.dialog_view_edit_profile_reset))
                .perform(click())
        return this
    }

    fun saveChangeProfile(): ProfileRobot {
        onView(withId(R.id.dialog_view_edit_profile_btn_save))
                .perform(click())
        return this
    }

    fun cancelChangeProfile(): ProfileRobot {
        onView(withId(R.id.dialog_view_edit_profile_btn_cancel))
                .perform(click())
        return this
    }

    fun openChangeAvatar(): ProfileRobot {
        onView(withId(R.id.card_view_profile_img))
                .perform(click())
        return this
    }

    fun saveChangeAvatar(): ProfileRobot {
        onView(withId(R.id.dialog_view_change_avatar_btn_confirm))
                .perform(click())
        return this
    }

    fun changeAvatar(avatar: String): ProfileRobot {
        doWait(1000)
        onView(withText(avatar))
                .perform(click())
        return this
    }

    fun cancelChangeAvatar(): ProfileRobot {
        onView(withId(R.id.dialog_view_change_avatar_btn_cancel))
                .perform(click())
        return this
    }

    fun addNewFriend(friend: String): ProfileRobot {
        onView(withId(R.id.card_view_friends_username))
                .perform(replaceText(friend), closeSoftKeyboard())

        onView(withId(R.id.card_view_friends_button_add))
                .perform(click())

        checkFriend(friend, 0)
        return this
    }
}
