package br.com.battista.bgscore.robot;

import android.content.Context;

import java.util.Date;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.util.DateUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.battista.bgscore.helper.DrawableMatcher.withDrawable;

public class ProfileRobot extends BaseRobot {

    private Context context;

    public ProfileRobot(Context context) {
        this.context = context;
    }

    public void checkDrawableUserAvatar(int avatar_profile) {
        onView(withId(R.id.card_view_profile_img)).check(matches(
                withDrawable(avatar_profile)));
    }

    public void checkTextUsername(String username) {
        onView(withId(R.id.card_view_profile_username))
                .check(matches(
                        withText(context.getString(R.string.text_username, username))));
    }

    public void checkTextMail(String mail) {
        onView(withId(R.id.card_view_profile_mail))
                .check(matches(
                        withText(context.getString(R.string.text_mail, mail))));
    }

    public void checkTextDateCreated(Date dateCreated) {
        onView(withId(R.id.card_view_profile_date_created))
                .check(matches(
                        withText(context.getString(R.string.text_date_created,
                                DateUtils.format(dateCreated)))));
    }

    public void checkEmptyFriends() {
        onView(withText(R.string.text_profile_friends_empty_view))
                .check(matches(isDisplayed()));
    }

}
