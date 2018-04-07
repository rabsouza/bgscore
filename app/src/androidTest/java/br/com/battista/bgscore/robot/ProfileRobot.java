package br.com.battista.bgscore.robot;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.battista.bgscore.helper.DrawableMatcher.withDrawable;

import android.content.Context;

import java.util.Date;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.util.DateUtils;

public class ProfileRobot extends BaseRobot {

    private Context context;

    public ProfileRobot(Context context) {
        this.context = context;
    }

    public ProfileRobot checkDrawableUserAvatar(int avatar_profile) {
        onView(withId(R.id.card_view_profile_img)).check(matches(
                withDrawable(avatar_profile)));
        return this;
    }

    public ProfileRobot checkTextUsername(String username) {
        onView(withId(R.id.card_view_profile_username))
                .check(matches(
                        withText(context.getString(R.string.text_username, username))));
        return this;
    }

    public ProfileRobot changeTextUsername(String username) {
        onView(withId(R.id.dialog_view_edit_profile_username))
                .perform(replaceText(username), closeSoftKeyboard());
        return this;
    }

    public ProfileRobot checkTextMail(String mail) {
        onView(withId(R.id.card_view_profile_mail))
                .check(matches(
                        withText(context.getString(R.string.text_mail, mail))));
        return this;
    }

    public ProfileRobot changeTextMail(String mail) {
        onView(withId(R.id.dialog_view_edit_profile_mail))
                .perform(replaceText(mail), closeSoftKeyboard());
        return this;
    }

    public ProfileRobot checkTextDateCreated(Date dateCreated) {
        onView(withId(R.id.card_view_profile_date_created))
                .check(matches(
                        withText(context.getString(R.string.text_date_created,
                                DateUtils.format(dateCreated)))));
        return this;
    }

    public ProfileRobot checkEmptyFriends() {
        onView(withText(R.string.text_profile_friends_empty_view))
                .check(matches(isDisplayed()));
        return this;
    }

    public ProfileRobot checkFriend(String friend, int position) {
        onView(withId(R.id.card_view_friends_recycler_view))
                .perform(scrollToPosition(position))
                .check(matches(hasDescendant(withText(friend))));
        return this;
    }

    public ProfileRobot openChangeProfile() {
        onView(withId(R.id.button_profile_edit_profile))
                .perform(click());
        return this;
    }

    public ProfileRobot resetDataProfile() {
        onView(withId(R.id.dialog_view_edit_profile_reset))
                .perform(click());
        return this;
    }

    public ProfileRobot saveChangeProfile() {
        onView(withId(R.id.dialog_view_edit_profile_btn_save))
                .perform(click());
        return this;
    }

    public ProfileRobot cancelChangeProfile() {
        onView(withId(R.id.dialog_view_edit_profile_btn_cancel))
                .perform(click());
        return this;
    }

    public ProfileRobot openChangeAvatar() {
        onView(withId(R.id.card_view_profile_img))
                .perform(click());
        return this;
    }

    public ProfileRobot saveChangeAvatar() {
        onView(withId(R.id.dialog_view_change_avatar_btn_confirm))
                .perform(click());
        return this;
    }

    public ProfileRobot changeAvatar(String avatar) {
        doWait(1000);
        onView(withText(avatar))
                .perform(click());
        return this;
    }

    public ProfileRobot cancelChangeAvatar() {
        onView(withId(R.id.dialog_view_change_avatar_btn_cancel))
                .perform(click());
        return this;
    }

    public ProfileRobot addNewFriend(String friend) {
        onView(withId(R.id.card_view_friends_username))
                .perform(replaceText(friend), closeSoftKeyboard());

        onView(withId(R.id.card_view_friends_button_add))
                .perform(click());

        checkFriend(friend, 0);
        return this;
    }
}
