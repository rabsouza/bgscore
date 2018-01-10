package br.com.battista.bgscore.fragment.game;


import static br.com.battista.bgscore.constants.BundleConstant.DATA;
import static br.com.battista.bgscore.constants.BundleConstant.NAVIGATION_TO;
import static br.com.battista.bgscore.constants.BundleConstant.NavigationTo.GAME_FRAGMENT;
import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_SEARCH_GAME_FRAGMENT;
import static br.com.battista.bgscore.model.enuns.BadgeGameEnum.BADGE_GAME_ABSTRACT;
import static br.com.battista.bgscore.model.enuns.BadgeGameEnum.BADGE_GAME_AMERITRASH;
import static br.com.battista.bgscore.model.enuns.BadgeGameEnum.BADGE_GAME_EUROS;
import static br.com.battista.bgscore.model.enuns.BadgeGameEnum.BADGE_GAME_FAMILY;
import static br.com.battista.bgscore.model.enuns.BadgeGameEnum.BADGE_GAME_NONE;
import static br.com.battista.bgscore.model.enuns.BadgeGameEnum.BADGE_GAME_PARTY;
import static br.com.battista.bgscore.model.enuns.BadgeGameEnum.BADGE_GAME_WARGAMER;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.greenrobot.eventbus.EventBus;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.HomeActivity;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.custom.ProgressApp;
import br.com.battista.bgscore.fragment.BaseFragment;
import br.com.battista.bgscore.fragment.dialog.SearchGameDialog;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.model.response.GameResponse;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.service.Inject;
import br.com.battista.bgscore.service.server.GameService;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.battista.bgscore.util.RatingUtils;

public class NewGameFragment extends BaseFragment {
    private static final String TAG = NewGameFragment.class.getSimpleName();

    private EditText txtIdBgg;
    private EditText txtNameGame;
    private EditText txtUrlThumbnailGame;
    private EditText txtUrlImageGame;
    private EditText txtUrlInfoGame;
    private MaterialBetterSpinner spnYearPublishedGame;
    private MaterialBetterSpinner spnBadgeGame;
    private EditText txtMinPlayersGame;
    private EditText txtMaxPlayersGame;
    private EditText txtMinPlayTimeGame;
    private EditText txtMaxPlayTimeGame;
    private EditText txtAgeGame;
    private RatingBar rtbRatingGame;
    private Switch swtMyGame;
    private Switch swtFavorite;
    private Switch swtWantGame;

    private ImageButton btnSearchGame;
    private EditText txtSearchNameGame;

    private Game game;

    public NewGameFragment() {
    }

    public static NewGameFragment newInstance(Game game) {
        NewGameFragment fragment = new NewGameFragment();
        Bundle args = new Bundle();
        if (game != null) {
            args.putSerializable(DATA, game);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create new NewGameFragment!");

        final View view = inflater.inflate(R.layout.fragment_new_game, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab_next_done_game);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillDataAndSave();
            }
        });

        setupDataForm(view);

        processDataFragment(view, getArguments());
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DIALOG_SEARCH_GAME_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    final long gameId = data.getLongExtra(BundleConstant.SEARCH_GAME_ID, 0l);
                    Log.i(TAG, MessageFormat.format("onActivityResult: Add new game: {0}",
                            gameId));

                    new ProgressApp(this.getActivity(), R.string.msg_action_saving, false) {
                        Game gameServer;

                        @Override
                        protected void onPostExecute(Boolean result) {
                            if (gameServer == null) {
                                AndroidUtils.snackbar(getView(), R.string.msg_game_error_add_game);
                            } else {
                                game = gameServer;
                                fillGameData();
                            }
                            dismissProgress();
                        }

                        @Override
                        protected Boolean doInBackground(Void... params) {
                            final GameService gameService = Inject.provideGameService();
                            gameServer = gameService.loadGame(gameId);
                            return true;
                        }
                    }.execute();

                    AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                            CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_ADD_GAME_ONLINE);
                }
                break;
        }
    }

    private void processDataFragment(View viewFragment, Bundle bundle) {
        Log.d(TAG, "processDataFragment: Process bundle data Fragment!");
        if (bundle != null && bundle.containsKey(BundleConstant.DATA)) {
            game = (Game) bundle.getSerializable(BundleConstant.DATA);
            game.reloadId();

            fillGameData();
        } else {
            game = new Game();
            game.initEntity();
        }
    }

    private void fillGameData() {
        if (game.getIdBGG() != null) {
            txtIdBgg.setText(String.valueOf(game.getIdBGG()));
            txtIdBgg.setEnabled(false);
        }
        txtNameGame.setText(game.getName());
        txtSearchNameGame.setText(game.getName());
        txtUrlThumbnailGame.setText(game.getUrlThumbnail());
        txtUrlImageGame.setText(game.getUrlImage());
        txtUrlInfoGame.setText(game.getUrlInfo());
        spnYearPublishedGame.setText(game.getYearPublished());
        spnBadgeGame.setText(getContext().getString(game.getBadgeGame().getIdResString()));
        txtMinPlayersGame.setText(game.getMinPlayers());
        txtMaxPlayersGame.setText(game.getMaxPlayers());
        txtMinPlayTimeGame.setText(game.getMinPlayTime());
        txtMaxPlayTimeGame.setText(game.getMaxPlayTime());
        txtAgeGame.setText(game.getAge());
        if (game.getRating() != null) {
            rtbRatingGame.setRating(game.getRating());
        } else {
            rtbRatingGame.setRating(0F);
        }
        swtMyGame.setChecked(game.isMyGame());
        swtFavorite.setChecked(game.isFavorite());
        swtWantGame.setChecked(game.isWantGame());

        if (!Strings.isNullOrEmpty(game.getUrlImage())) {
            final ImageView imgToolbar = getActivity().findViewById(R.id.detail_image_toolbar);
            ImageLoadUtils.loadImage(getContext(), game.getUrlImage(), imgToolbar);
        } else if (!Strings.isNullOrEmpty(game.getUrlThumbnail())) {
            final ImageView imgToolbar = getActivity().findViewById(R.id.detail_image_toolbar);
            ImageLoadUtils.loadImage(getContext(), game.getUrlThumbnail(), imgToolbar);
        }
    }

    private void finishFormAndProcessData() {
        Bundle args = new Bundle();
        args.putInt(NAVIGATION_TO, GAME_FRAGMENT);
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.putExtras(args);

        getContext().startActivity(intent);
    }

    private void fillDataAndSave() {
        if (Strings.isNullOrEmpty(txtNameGame.getText().toString())) {
            String msgErrorUsername = getContext().getString(R.string.msg_name_game_required);
            AndroidUtils.changeErrorEditText(txtNameGame, msgErrorUsername, true);
            return;
        }
        AndroidUtils.changeErrorEditText(txtNameGame);
        game.name(txtNameGame.getText().toString().trim());
        if (game.getIdBGG() == null) {
            try {
                game.setIdBGG(Long.valueOf(txtIdBgg.getText().toString().trim()));
            } catch (Exception e) {
                Log.e(TAG, "fillDataAndSave: ID bgg invalid!", e);
            }
        }
        game.urlThumbnail(txtUrlThumbnailGame.getText().toString().trim());
        game.urlImage(txtUrlImageGame.getText().toString().trim());
        game.urlInfo(txtUrlInfoGame.getText().toString().trim());
        game.yearPublished(spnYearPublishedGame.getText().toString().trim());

        String badgeGame = spnBadgeGame.getText().toString().trim();
        if (getContext().getString(BADGE_GAME_ABSTRACT.getIdResString()).equals(badgeGame)) {
            game.badgeGame(BADGE_GAME_ABSTRACT);
        } else if (getContext().getString(BADGE_GAME_AMERITRASH.getIdResString()).equals(badgeGame)) {
            game.badgeGame(BADGE_GAME_AMERITRASH);
        } else if (getContext().getString(BADGE_GAME_EUROS.getIdResString()).equals(badgeGame)) {
            game.badgeGame(BADGE_GAME_EUROS);
        } else if (getContext().getString(BADGE_GAME_FAMILY.getIdResString()).equals(badgeGame)) {
            game.badgeGame(BADGE_GAME_FAMILY);
        } else if (getContext().getString(BADGE_GAME_PARTY.getIdResString()).equals(badgeGame)) {
            game.badgeGame(BADGE_GAME_PARTY);
        } else if (getContext().getString(BADGE_GAME_WARGAMER.getIdResString()).equals(badgeGame)) {
            game.badgeGame(BADGE_GAME_WARGAMER);
        } else {
            game.badgeGame(BADGE_GAME_NONE);
        }

        game.minPlayers(txtMinPlayersGame.getText().toString().trim());
        game.maxPlayers(txtMaxPlayersGame.getText().toString().trim());
        game.minPlayTime(txtMinPlayTimeGame.getText().toString().trim());
        game.maxPlayTime(txtMaxPlayTimeGame.getText().toString().trim());
        game.age(txtAgeGame.getText().toString().trim());
        game.rating(RatingUtils.convertTo(rtbRatingGame.getRating()));
        game.myGame(swtMyGame.isChecked());
        game.favorite(swtFavorite.isChecked());
        game.wantGame(swtWantGame.isChecked());

        Log.i(TAG, "fillDataAndSave: Save the data in BD.");
        new GameRepository().save(game);
        Log.i(TAG, "fillDataAndSave: Reload cache data.");
        EventBus.getDefault().post(ActionCacheEnum.LOAD_DATA_GAME);

        finishFormAndProcessData();
    }

    private void setupDataForm(final View view) {
        Log.i(TAG, "setupDataForm: Load all form fields!");

        txtNameGame = view.findViewById(R.id.card_view_new_game_name);
        txtIdBgg = view.findViewById(R.id.card_view_new_game_id_bgg);
        txtUrlThumbnailGame = view.findViewById(R.id.card_view_new_game_url_thumbnail);
        txtUrlImageGame = view.findViewById(R.id.card_view_new_game_url_image);
        txtUrlInfoGame = view.findViewById(R.id.card_view_new_game_url_info);

        final Calendar now = Calendar.getInstance();
        List<Integer> years = Lists.newArrayList(now.get(Calendar.YEAR));
        for (int offset = 1; offset < 70; offset++) {
            now.add(Calendar.YEAR, -1);
            years.add(now.get(Calendar.YEAR));
        }
        ArrayAdapter<Integer> arrayAdapterYears = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, years);

        spnYearPublishedGame = view.findViewById(R.id.card_view_new_game_year_published);
        spnYearPublishedGame.setAdapter(arrayAdapterYears);

        List<String> badgesGame = Lists.newArrayList();
        badgesGame.add(getContext().getString(BADGE_GAME_ABSTRACT.getIdResString()));
        badgesGame.add(getContext().getString(BADGE_GAME_AMERITRASH.getIdResString()));
        badgesGame.add(getContext().getString(BADGE_GAME_EUROS.getIdResString()));
        badgesGame.add(getContext().getString(BADGE_GAME_FAMILY.getIdResString()));
        badgesGame.add(getContext().getString(BADGE_GAME_NONE.getIdResString()));
        badgesGame.add(getContext().getString(BADGE_GAME_PARTY.getIdResString()));
        badgesGame.add(getContext().getString(BADGE_GAME_WARGAMER.getIdResString()));
        ArrayAdapter<String> arrayAdapterBadges = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, badgesGame);

        spnBadgeGame = view.findViewById(R.id.card_view_new_game_badge_game);
        spnBadgeGame.setAdapter(arrayAdapterBadges);

        txtMinPlayersGame = view.findViewById(R.id.card_view_new_game_min_players);
        txtMaxPlayersGame = view.findViewById(R.id.card_view_new_game_max_players);
        txtMinPlayTimeGame = view.findViewById(R.id.card_view_new_game_min_play_time);
        txtMaxPlayTimeGame = view.findViewById(R.id.card_view_new_game_max_play_time);
        txtAgeGame = view.findViewById(R.id.card_view_new_game_age);
        rtbRatingGame = view.findViewById(R.id.card_view_new_game_rating);
        swtMyGame = view.findViewById(R.id.card_view_new_game_my_game);
        swtFavorite = view.findViewById(R.id.card_view_new_game_favorite);
        swtWantGame = view.findViewById(R.id.card_view_new_game_want_game);

        btnSearchGame = view.findViewById(R.id.card_view_game_button_search);
        btnSearchGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewClicked) {
                processDataSearchGame(view);
            }
        });

        txtSearchNameGame = view.findViewById(R.id.card_view_game_search_name);
        txtSearchNameGame.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    processDataSearchGame(view);
                }
                return false;
            }
        });

    }

    private void processDataSearchGame(final View viewContainer) {
        if (Strings.isNullOrEmpty(txtSearchNameGame.getText().toString())) {
            String msgErrorUsername = getContext().getString(R.string.msg_name_game_required);
            AndroidUtils.changeErrorEditText(txtSearchNameGame, msgErrorUsername, true);
            return;
        }
        AndroidUtils.changeErrorEditText(txtSearchNameGame);
        final String nameGame = txtSearchNameGame.getText().toString().trim();

        Log.i(TAG, "processDataSearchGame: Search game in server!");

        final Fragment currentFragment = this;
        new ProgressApp(this.getActivity(), R.string.msg_action_searching, false) {
            ArrayList<GameResponse> gameResponses;

            @Override
            protected void onPostExecute(Boolean result) {
                if (gameResponses.isEmpty()) {
                    AndroidUtils.snackbar(viewContainer, R.string.msg_game_dont_found_search);
                } else {
                    SearchGameDialog.newInstance(gameResponses).showDialog(currentFragment);
                }
                dismissProgress();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                final GameService gameService = Inject.provideGameService();
                gameResponses = new ArrayList<>(gameService.searchGame(nameGame));
                return true;
            }
        }.execute();

    }

}
