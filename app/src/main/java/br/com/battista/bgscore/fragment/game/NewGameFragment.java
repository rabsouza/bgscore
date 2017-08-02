package br.com.battista.bgscore.fragment.game;


import static br.com.battista.bgscore.constants.BundleConstant.DATA;
import static br.com.battista.bgscore.constants.BundleConstant.NAVIGATION_TO;
import static br.com.battista.bgscore.constants.BundleConstant.NavigationTo.GAME_FRAGMENT;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.HomeActivity;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.fragment.BaseFragment;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.service.CacheManageService;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.RatingUtils;

public class NewGameFragment extends BaseFragment {
    private static final String TAG = NewGameFragment.class.getSimpleName();

    private EditText txtNameGame;
    private EditText txtUrlThumbnailGame;
    private EditText txtUrlImageGame;
    private EditText txtUrlInfoGame;
    private MaterialBetterSpinner txtYearPublishedGame;
    private EditText txtMinPlayersGame;
    private EditText txtMaxPlayersGame;
    private EditText txtMinPlayTimeGame;
    private EditText txtMaxPlayTimeGame;
    private EditText txtAgeGame;
    private RatingBar rtbRatingGame;

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

    private void processDataFragment(View viewFragment, Bundle bundle) {
        Log.d(TAG, "processDataFragment: Process bundle data Fragment!");
        if (bundle.containsKey(BundleConstant.DATA)) {
            game = (Game) bundle.getSerializable(BundleConstant.DATA);
            game.reloadId();

            txtNameGame.setText(game.getName());
            txtUrlThumbnailGame.setText(game.getUrlThumbnail());
            txtUrlImageGame.setText(game.getUrlImage());
            txtUrlInfoGame.setText(game.getUrlInfo());
            txtYearPublishedGame.setText(game.getYearPublished());
            txtMinPlayersGame.setText(game.getMinPlayers());
            txtMaxPlayersGame.setText(game.getMaxPlayers());
            txtMinPlayTimeGame.setText(game.getMinPlayTime());
            txtMaxPlayTimeGame.setText(game.getMaxPlayTime());
            txtAgeGame.setText(game.getAge());
            if (!Strings.isNullOrEmpty(game.getRating())) {
                rtbRatingGame.setRating(RatingUtils.convertFrom(game.getRating()));
            }
        } else {
            game = new Game();
            game.initEntity();
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
        game.urlThumbnail(txtUrlThumbnailGame.getText().toString().trim());
        game.urlImage(txtUrlImageGame.getText().toString().trim());
        game.urlInfo(txtUrlInfoGame.getText().toString().trim());
        game.yearPublished(txtYearPublishedGame.getText().toString().trim());
        game.minPlayers(txtMinPlayersGame.getText().toString().trim());
        game.maxPlayers(txtMaxPlayersGame.getText().toString().trim());
        game.minPlayTime(txtMinPlayTimeGame.getText().toString().trim());
        game.maxPlayTime(txtMaxPlayTimeGame.getText().toString().trim());
        game.age(txtAgeGame.getText().toString().trim());
        game.rating(RatingUtils.convertTo(rtbRatingGame.getRating()));

        Log.i(TAG, "fillDataAndSave: Save the data in BD.");
        new GameRepository().save(game);
        Log.i(TAG, "fillDataAndSave: Reload cache data.");
        new CacheManageService().onActionCache(ActionCacheEnum.LOAD_DATA_GAME);

        finishFormAndProcessData();
    }

    private void setupDataForm(View view) {
        Log.i(TAG, "setupDataForm: Load all form fields!");

        txtNameGame = view.findViewById(R.id.card_view_new_game_name);
        txtUrlThumbnailGame = view.findViewById(R.id.card_view_new_game_url_thumbnail);
        txtUrlImageGame = view.findViewById(R.id.card_view_new_game_url_image);
        txtUrlInfoGame = view.findViewById(R.id.card_view_new_game_url_info);

        final Calendar now = Calendar.getInstance();
        ArrayList<Integer> namesHeroes = Lists.newArrayList(now.get(Calendar.YEAR));
        for (int offset = 1; offset < 50; offset++) {
            now.add(Calendar.YEAR, -1);
            namesHeroes.add(now.get(Calendar.YEAR));
        }
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, namesHeroes);

        txtYearPublishedGame = view.findViewById(R.id.card_view_new_game_year_published);
        txtYearPublishedGame.setAdapter(arrayAdapter);
        txtMinPlayersGame = view.findViewById(R.id.card_view_new_game_min_players);
        txtMaxPlayersGame = view.findViewById(R.id.card_view_new_game_max_players);
        txtMinPlayTimeGame = view.findViewById(R.id.card_view_new_game_min_play_time);
        txtMaxPlayTimeGame = view.findViewById(R.id.card_view_new_game_max_play_time);
        txtAgeGame = view.findViewById(R.id.card_view_new_game_age);
        rtbRatingGame = view.findViewById(R.id.card_view_new_game_rating);

    }

}
