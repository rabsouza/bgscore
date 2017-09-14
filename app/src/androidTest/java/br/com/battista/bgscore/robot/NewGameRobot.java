package br.com.battista.bgscore.robot;

import android.content.Context;

import br.com.battista.bgscore.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static br.com.battista.bgscore.helper.CustomScrollActions.nestedScrollTo;

public class NewGameRobot extends BaseRobot {

    private Context context;

    public NewGameRobot(Context context) {
        this.context = context;
    }

    public NewGameRobot fillTextName(String name) {
        onView(withId(R.id.card_view_new_game_name))
                .perform(nestedScrollTo(), replaceText(name), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot fillTextYearPublished(String yearPublished) {
        onView(withId(R.id.card_view_new_game_year_published))
                .perform(nestedScrollTo(), replaceText(yearPublished), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot fillTextAge(String age) {
        onView(withId(R.id.card_view_new_game_age))
                .perform(nestedScrollTo(), replaceText(age), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot fillTextMinPlayers(String minPlayers) {
        onView(withId(R.id.card_view_new_game_min_players))
                .perform(nestedScrollTo(), replaceText(minPlayers), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot fillTextMaxPlayers(String maxPlayers) {
        onView(withId(R.id.card_view_new_game_max_players))
                .perform(nestedScrollTo(), replaceText(maxPlayers), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot fillTextMinPlayTime(String minPlayTime) {
        onView(withId(R.id.card_view_new_game_min_play_time))
                .perform(nestedScrollTo(), replaceText(minPlayTime), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot fillTextMaxPlayTime(String maxPlayTime) {
        onView(withId(R.id.card_view_new_game_max_play_time))
                .perform(nestedScrollTo(), replaceText(maxPlayTime), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot fillTextGameBadge(String gameBadge) {
        onView(withId(R.id.card_view_new_game_badge_game))
                .perform(nestedScrollTo(), replaceText(gameBadge), closeSoftKeyboard());
        return this;
    }

    public GameRobot saveGame() {
        onView(withId(R.id.fab_next_done_game))
                .perform(nestedScrollTo(), click());
        return new GameRobot(context);
    }

}
