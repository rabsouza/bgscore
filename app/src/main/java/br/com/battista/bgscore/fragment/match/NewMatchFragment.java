package br.com.battista.bgscore.fragment.match;


import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.adpater.FriendAdapter;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.fragment.BaseFragment;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.Player;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.FriendDto;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.repository.PlayerRepository;
import br.com.battista.bgscore.service.CacheManageService;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.battista.bgscore.util.RatingUtils;

public class NewMatchFragment extends BaseFragment {
    private static final String TAG = NewMatchFragment.class.getSimpleName();

    private RecycleEmptyErrorView recycleViewPlayers;
    private TextView emptyMsgPlayers;
    private TextView errorMsgPlayers;

    private final Map<String, Game> gameMap = Maps.newTreeMap();

    private ImageButton btnSearchGame;
    private AutoCompleteTextView txtSearchNameGame;
    private CardView cardViewGame;

    private ImageView imgInfoGame;
    private TextView txtInfoName;
    private TextView txtInfoTime;
    private TextView txtInfoPlayers;
    private TextView txtInfoAges;
    private RatingBar rtbInfoRating;

    public NewMatchFragment() {
    }

    public static NewMatchFragment newInstance() {
        NewMatchFragment fragment = new NewMatchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create new NewMatchFragment!");

        final View view = inflater.inflate(R.layout.fragment_new_match, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab_next_edit_match);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Game game = new Game().name("Game" + Math.random());
                game.initEntity();
                new GameRepository().save(game);
                new CacheManageService().onActionCache(ActionCacheEnum.LOAD_DATA_GAME);

                final Player player01 = new Player().name("Player01" + Math.random()).winner(false);
                player01.initEntity();
                final PlayerRepository playerRepository = new PlayerRepository();
                playerRepository.save(player01);
                final Player player02 = new Player().name("Player02" + Math.random()).winner(true);
                player02.initEntity();
                playerRepository.save(player02);

                int result = new Random().nextInt(200) + 100;
                final Match match = new Match()
                        .alias("Alias" + Math.random())
                        .duration(Long.valueOf(result))
                        .finish(true).game(game)
                        .players(Lists.newArrayList(player01, player02));
                Log.i(TAG, "***********************************************************");
                Log.i(TAG, "MATCH SAVE:" + match);
                Log.i(TAG, "***********************************************************");
                new MatchRepository().save(match);

                final List<Match> matches = new MatchRepository().findAll();
                Log.i(TAG, "***********************************************************");
                for (Match matchLoad : matches) {
                    Log.i(TAG, "MATCH LOAD:" + matchLoad);
                }
                Log.i(TAG, "***********************************************************");
                new CacheManageService().onActionCache(ActionCacheEnum.LOAD_DATA_MATCHES);
            }
        });

        setupRecycleViewPlayers(view);
        loadDataForm(view);
        return view;
    }

    private void loadDataForm(final View view) {
        Log.i(TAG, "loadDataForm: Load all form fields!");
        final List<Game> games = new GameRepository().findAll();
        for (Game game : games) {
            gameMap.put(game.getName(), game);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, Lists.newArrayList(gameMap.keySet()));

        txtSearchNameGame = view.findViewById(R.id.card_view_game_search_name);
        txtSearchNameGame.setAdapter(adapter);
        txtSearchNameGame.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    processDataSearchGame(view);
                }
                return false;
            }
        });

        cardViewGame = view.findViewById(R.id.card_view_game_info);

        btnSearchGame = view.findViewById(R.id.card_view_game_button_search);
        btnSearchGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewClicked) {
                processDataSearchGame(view);
            }
        });
    }

    private void processDataSearchGame(View view) {
        if (Strings.isNullOrEmpty(txtSearchNameGame.getText().toString())) {
            String msgErrorUsername = getContext().getString(R.string.msg_name_game_required);
            AndroidUtils.changeErrorEditText(txtSearchNameGame, msgErrorUsername, true);
            return;
        }
        AndroidUtils.changeErrorEditText(txtSearchNameGame);
        final String nameGame = txtSearchNameGame.getText().toString().trim();
        if (gameMap.containsKey(nameGame)) {
            Game game = gameMap.get(nameGame);

            imgInfoGame = view.findViewById(R.id.card_view_game_info_image);
            txtInfoName = view.findViewById(R.id.card_view_game_info_name);
            txtInfoPlayers = view.findViewById(R.id.card_view_game_info_players);
            txtInfoTime = view.findViewById(R.id.card_view_game_info_time);
            txtInfoAges = view.findViewById(R.id.card_view_game_info_ages);
            rtbInfoRating = view.findViewById(R.id.card_view_game_info_rating);

            Log.i(TAG, "processDataSearchGame: Fill the data Game!");

            String urlThumbnail = game.getUrlThumbnail();
            if (Strings.isNullOrEmpty(urlThumbnail)) {
                ImageLoadUtils.loadImage(getContext(),
                        R.drawable.boardgame_game,
                        imgInfoGame);
            } else {
                ImageLoadUtils.loadImage(getContext(),
                        urlThumbnail,
                        imgInfoGame);
            }

            txtInfoName.setText(MessageFormat.format("{0} ({1})",
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getName()), "-"),
                    MoreObjects.firstNonNull(Strings.emptyToNull(game.getYearPublished()), "*")));

            if (Strings.isNullOrEmpty(game.getMaxPlayers())) {
                txtInfoPlayers.setText(MessageFormat.format("{0} joga.",
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayers()), "1")));
            } else {
                txtInfoPlayers.setText(MessageFormat.format("{0}/{1} joga.",
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayers()), "1"),
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMaxPlayers()), "*")));
            }

            if (Strings.isNullOrEmpty(game.getMaxPlayTime())) {
                txtInfoTime.setText(MessageFormat.format("{0} mins",
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayTime()), "∞")));
            } else {
                txtInfoTime.setText(MessageFormat.format("{0}-{1} mins",
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMinPlayTime()), "*"),
                        MoreObjects.firstNonNull(Strings.emptyToNull(game.getMaxPlayTime()), "∞")));
            }

            if (Strings.isNullOrEmpty(game.getAge())) {
                txtInfoAges.setText("-");
            } else {
                txtInfoAges.setText(MessageFormat.format("{0}+ anos", game.getAge()));
            }

            if (Strings.isNullOrEmpty(game.getRating())) {
                rtbInfoRating.setRating(0F);
            } else {
                rtbInfoRating.setRating(RatingUtils.convertFrom(game.getRating()));
            }

            cardViewGame.setVisibility(View.VISIBLE);
        } else {
            AndroidUtils.snackbar(view, R.string.msg_game_dont_found);
        }
    }


    private void setupRecycleViewPlayers(View view) {
        final MainApplication instance = MainApplication.instance();
        final User user = instance.getUser();

        recycleViewPlayers = view.findViewById(R.id.card_view_players_recycler_view);
        emptyMsgPlayers = view.findViewById(R.id.card_view_players_empty_view);
        errorMsgPlayers = view.findViewById(R.id.card_view_players_error_view);
        recycleViewPlayers.setEmptyView(emptyMsgPlayers);
        recycleViewPlayers.setErrorView(errorMsgPlayers);

        recycleViewPlayers.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
        recycleViewPlayers.setItemAnimator(new DefaultItemAnimator());
        recycleViewPlayers.setHasFixedSize(false);
        final List<FriendDto> players = Lists.newLinkedList(user.getFriends());
        recycleViewPlayers.setAdapter(new FriendAdapter(getContext(), players, false, true));

    }

}
