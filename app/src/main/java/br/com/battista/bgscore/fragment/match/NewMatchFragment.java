package br.com.battista.bgscore.fragment.match;


import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static br.com.battista.bgscore.constants.BundleConstant.DATA;
import static br.com.battista.bgscore.constants.BundleConstant.NAVIGATION_TO;
import static br.com.battista.bgscore.constants.BundleConstant.NavigationTo.MATCH_FRAGMENT;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.HomeActivity;
import br.com.battista.bgscore.adpater.FriendAdapter;
import br.com.battista.bgscore.constants.BundleConstant;
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

public class NewMatchFragment extends BaseFragment {

    private static final String TAG = NewMatchFragment.class.getSimpleName();
    private final Map<String, Game> gameMap = Maps.newTreeMap();
    private RecycleEmptyErrorView recycleViewPlayers;
    private TextView emptyMsgPlayers;
    private TextView errorMsgPlayers;
    private Game gameSelected;
    private FriendAdapter friendAdapter;
    private Set<FriendDto> friendsSelected = Sets.newLinkedHashSet();
    private Set<FriendDto> playersSaved = Sets.newLinkedHashSet();

    private ImageButton btnSearchGame;
    private AutoCompleteTextView txtSearchNameGame;
    private CardView cardViewGame;

    private EditText txtMatchAlias;
    private Switch swtIPlaying;

    private ImageView imgInfoGame;
    private TextView txtInfoName;
    private TextView txtInfoTime;
    private TextView txtInfoPlayers;
    private TextView txtInfoAges;
    private TextView txtInfoYear;

    private Match match;

    public NewMatchFragment() {
    }

    public static NewMatchFragment newInstance(Match match) {
        NewMatchFragment fragment = new NewMatchFragment();
        Bundle args = new Bundle();
        if (match != null) {
            args.putSerializable(DATA, match);
        }
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
            public void onClick(View viewClicked) {
                fillDataAndSave();
            }
        });

        setupRecycleViewPlayers(view);
        setupDataForm(view);

        processDataFragment(view, getArguments());
        loadDataPlayers();
        return view;
    }

    private void processDataFragment(View viewFragment, Bundle bundle) {
        Log.d(TAG, "processDataFragment: Process bundle data Fragment!");
        if (bundle.containsKey(BundleConstant.DATA)) {
            match = (Match) bundle.getSerializable(BundleConstant.DATA);
            match.reloadId();

            txtMatchAlias.setText(match.getAlias());
            gameSelected = match.getGame();
            txtSearchNameGame.setText(gameSelected.getName());
            fillCardGame();

            final User user = MainApplication.instance().getUser();
            swtIPlaying.setChecked(match.isIPlaying());
            if (match.isIPlaying()) {
                playersSaved.add(user.getMyFriendDTO());
            }
            for (Player player : match.getPlayers()) {
                if (!player.getName().equals(user.getUsername())) {
                    playersSaved.add(new FriendDto().username(player.getName()));
                }
            }
        } else {
            match = new Match();
            match.initEntity();
        }
    }

    private void fillDataAndSave() {
        Log.i(TAG, "fillDataAndSave: Validate form data and fill data to save!");
        if (Strings.isNullOrEmpty(txtMatchAlias.getText().toString())) {
            String msgErrorUsername = getContext().getString(R.string.msg_alias_match_required);
            AndroidUtils.changeErrorEditText(txtMatchAlias, msgErrorUsername, true);
            return;
        }
        AndroidUtils.changeErrorEditText(txtMatchAlias);
        match.setAlias(txtMatchAlias.getText().toString().trim());

        if (gameSelected == null) {
            String msgErrorUsername = getContext().getString(R.string.msg_game_required);
            AndroidUtils.changeErrorEditText(txtSearchNameGame, msgErrorUsername, true);
            return;
        }
        AndroidUtils.changeErrorEditText(txtSearchNameGame);
        match.setGame(gameSelected);

        for (Player player : match.getPlayers()) {
            new PlayerRepository().delete(player);
        }
        match.clearPlayers();
        friendsSelected.clear();

        friendsSelected.addAll(friendAdapter.getFriendsSelected());
        final MainApplication instance = MainApplication.instance();
        final User user = instance.getUser();
        if (swtIPlaying.isChecked()) {
            friendsSelected.add(user.getMyFriendDTO());
        }
        match.iPlaying(swtIPlaying.isChecked());

        for (FriendDto friendDto : friendsSelected) {
            Player player = new Player();
            player.initEntity();
            player.name(friendDto.getUsername());
            match.addPlayer(player);
        }

        Log.i(TAG, "fillDataAndSave: Save the data in BD.");
        new MatchRepository().save(match);

        user.lastPlay(match.getCreatedAt());
        instance.setUser(user);

        Log.i(TAG, "fillDataAndSave: Reload cache data.");
        new CacheManageService().onActionCache(ActionCacheEnum.LOAD_DATA_MATCHES);

        finishFormAndProcessData();
    }

    private void finishFormAndProcessData() {
        Bundle args = new Bundle();
        args.putInt(NAVIGATION_TO, MATCH_FRAGMENT);
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.putExtras(args);

        getContext().startActivity(intent);
    }

    private void setupDataForm(final View view) {
        Log.i(TAG, "setupDataForm: Load all form fields!");
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

        txtMatchAlias = view.findViewById(R.id.card_view_match_info_alias);
        swtIPlaying = view.findViewById(R.id.card_view_players_i_playing_switch);

        imgInfoGame = view.findViewById(R.id.card_view_game_info_image);
        txtInfoName = view.findViewById(R.id.card_view_game_info_name);
        txtInfoPlayers = view.findViewById(R.id.card_view_game_info_players);
        txtInfoTime = view.findViewById(R.id.card_view_game_info_time);
        txtInfoAges = view.findViewById(R.id.card_view_game_info_ages);
        txtInfoYear = view.findViewById(R.id.card_view_game_info_year);
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
            gameSelected = gameMap.get(nameGame);
            fillCardGame();
        } else {
            AndroidUtils.snackbar(view, R.string.msg_game_dont_found);
        }
    }

    private void fillCardGame() {
        Log.i(TAG, "processDataSearchGame: Fill the data Game!");

        String urlThumbnail = gameSelected.getUrlThumbnail();
        if (Strings.isNullOrEmpty(urlThumbnail)) {
            ImageLoadUtils.loadImage(getContext(),
                    R.drawable.boardgame_game,
                    imgInfoGame);
        } else {
            ImageLoadUtils.loadImage(getContext(),
                    urlThumbnail,
                    imgInfoGame);
        }

        txtInfoName.setText(
                MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getName()), "-"));

        txtInfoYear.setText(
                MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getYearPublished()), "****"));

        if (Strings.isNullOrEmpty(gameSelected.getMaxPlayers())) {
            txtInfoPlayers.setText(MessageFormat.format("{0}",
                    MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getMinPlayers()), "1")));
        } else {
            txtInfoPlayers.setText(MessageFormat.format("{0}-{1}",
                    MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getMinPlayers()), "1"),
                    MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getMaxPlayers()), "*")));
        }

        if (Strings.isNullOrEmpty(gameSelected.getMaxPlayTime())) {
            txtInfoTime.setText(MessageFormat.format("{0}´",
                    MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getMinPlayTime()), "∞")));
        } else {
            txtInfoTime.setText(MessageFormat.format("{0}-{1}´",
                    MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getMinPlayTime()), "*"),
                    MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getMaxPlayTime()), "∞")));
        }

        if (Strings.isNullOrEmpty(gameSelected.getAge())) {
            txtInfoAges.setText("-");
        } else {
            txtInfoAges.setText(MessageFormat.format("{0}+", gameSelected.getAge()));
        }

        cardViewGame.setVisibility(View.VISIBLE);
    }


    private void setupRecycleViewPlayers(View view) {
        recycleViewPlayers = view.findViewById(R.id.card_view_players_recycler_view);
        emptyMsgPlayers = view.findViewById(R.id.card_view_players_empty_view);
        errorMsgPlayers = view.findViewById(R.id.card_view_players_error_view);
        recycleViewPlayers.setEmptyView(emptyMsgPlayers);
        recycleViewPlayers.setErrorView(errorMsgPlayers);

        recycleViewPlayers.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
        recycleViewPlayers.setItemAnimator(new DefaultItemAnimator());
        recycleViewPlayers.setHasFixedSize(false);

    }

    private void loadDataPlayers() {
        final MainApplication instance = MainApplication.instance();
        final User user = instance.getUser();
        final List<FriendDto> players = Lists.newLinkedList(user.getFriends());
        Collections.sort(players, new Ordering<FriendDto>() {
            @Override
            public int compare(FriendDto left, FriendDto right) {
                return left.compareTo(right);
            }
        });

        for (FriendDto player : players) {
            player.setSelected(Boolean.FALSE);
            for (FriendDto playerSaved : playersSaved) {
                if (player.getUsername().equals(playerSaved.getUsername())) {
                    player.setSelected(Boolean.TRUE);
                    break;
                }
            }
        }
        friendAdapter = new FriendAdapter(getContext(), players, false, true);
        recycleViewPlayers.setAdapter(friendAdapter);
    }

}
