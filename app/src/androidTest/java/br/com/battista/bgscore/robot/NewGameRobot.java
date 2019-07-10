package br.com.battista.bgscore.robot;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static br.com.battista.bgscore.helper.NestedScrollViewScrollToAction.scrollTo;

import android.content.Context;

import br.com.battista.bgscore.R;

public class NewGameRobot extends BaseRobot {

    private Context context;

    public NewGameRobot(Context context) {
        this.context = context;
    }

    private void checkStateCheckedView(boolean checked, int idRes) {
        if (checked) {
            onView(withId(idRes))
                    .check(matches(isChecked()));
        } else {
            onView(withId(idRes))
                    .check(matches(isNotChecked()));
        }
    }

    public NewGameRobot fillTextName(String name) {
        onView(withId(R.id.card_view_new_game_name))
                .perform(scrollTo());
        
        onView(withId(R.id.card_view_new_game_name))
                .perform(replaceText(name), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot checkMyGame(boolean checked) {
        onView(withId(R.id.card_view_new_game_my_game))
                .perform(scrollTo());
        
        checkStateCheckedView(checked, R.id.card_view_new_game_my_game);
        return this;
    }

    public NewGameRobot checkFavorite(boolean checked) {
        onView(withId(R.id.card_view_new_game_favorite))
                .perform(scrollTo());
        
        checkStateCheckedView(checked, R.id.card_view_new_game_favorite);
        return this;
    }

    public NewGameRobot checkWantGame(boolean checked) {
        onView(withId(R.id.card_view_new_game_want_game))
                .perform(scrollTo());
        
        checkStateCheckedView(checked, R.id.card_view_new_game_want_game);
        return this;
    }

    public NewGameRobot fillTextYearPublished(String yearPublished) {
        onView(withId(R.id.card_view_new_game_age))
                .perform(scrollTo());

        onView(withId(R.id.card_view_new_game_year_published))
                .perform(replaceText(yearPublished), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot fillTextAge(String age) {
        onView(withId(R.id.card_view_new_game_age))
                .perform(scrollTo());

        onView(withId(R.id.card_view_new_game_age))
                .perform(replaceText(age), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot fillTextMinPlayers(String minPlayers) {
        onView(withId(R.id.card_view_new_game_min_players))
                .perform(scrollTo());
        
        onView(withId(R.id.card_view_new_game_min_players))
                .perform(replaceText(minPlayers), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot fillTextMaxPlayers(String maxPlayers) {
        onView(withId(R.id.card_view_new_game_max_players))
                .perform(scrollTo());

        onView(withId(R.id.card_view_new_game_max_players))
                .perform(replaceText(maxPlayers), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot fillTextMinPlayTime(String minPlayTime) {
        onView(withId(R.id.card_view_new_game_min_play_time))
                .perform(scrollTo());

        onView(withId(R.id.card_view_new_game_min_play_time))
                .perform(replaceText(minPlayTime), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot fillTextMaxPlayTime(String maxPlayTime) {
        onView(withId(R.id.card_view_new_game_max_play_time))
                .perform(scrollTo());

        onView(withId(R.id.card_view_new_game_max_play_time))
                .perform(replaceText(maxPlayTime), closeSoftKeyboard());
        return this;
    }

    public NewGameRobot fillTextGameBadge(String gameBadge) {
        onView(withId(R.id.card_view_new_game_badge_game))
                .perform(scrollTo());
        
        onView(withId(R.id.card_view_new_game_badge_game))
                .perform(replaceText(gameBadge), closeSoftKeyboard());
        return this;
    }

    public GameRobot saveGame() {
        onView(withId(R.id.fab_next_done_game))
                .perform(scrollTo());
        
        onView(withId(R.id.fab_next_done_game))
                .perform(click());
        return new GameRobot(context);
    }

}
