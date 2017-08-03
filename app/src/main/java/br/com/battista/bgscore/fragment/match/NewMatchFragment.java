package br.com.battista.bgscore.fragment.match;


import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.HomeActivity;
import br.com.battista.bgscore.adpater.FriendAdapter;
import br.com.battista.bgscore.adpater.PlayerAdapter;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
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
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.DateUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

import static br.com.battista.bgscore.constants.BundleConstant.DATA;
import static br.com.battista.bgscore.constants.BundleConstant.NAVIGATION_TO;
import static br.com.battista.bgscore.constants.BundleConstant.NavigationTo.MATCH_FRAGMENT;
import static br.com.battista.bgscore.constants.ViewConstant.SPACE_DRAWABLE;

public class NewMatchFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = NewMatchFragment.class.getSimpleName();
    private final Map<String, Game> gameMap = Maps.newTreeMap();

    private RecycleEmptyErrorView recycleViewPlayers;
    private ImageButton btnAddPlayer;
    private EditText txtUsernamePlayer;
    final List<Player> players = Lists.newLinkedList();

    private RecycleEmptyErrorView recycleViewPlayersFriends;
    private TextView emptyMsgPlayersFriends;
    private TextView errorMsgPlayersFriends;
    private Game gameSelected;
    private FriendAdapter friendAdapter;
    private Set<FriendDto> friendsSelected = Sets.newLinkedHashSet();
    private Set<FriendDto> playersSaved = Sets.newLinkedHashSet();

    private ImageButton btnSearchGame;
    private AutoCompleteTextView txtSearchNameGame;
    private CardView cardViewGame;

    private EditText txtMatchAlias;
    private EditText txtCreateAt;
    private ImageButton btnCreateAt;
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
            txtCreateAt.setText(DateUtils.format(match.getCreatedAt()));
            gameSelected = match.getGame();
            txtSearchNameGame.setText(gameSelected.getName());
            fillCardGame();

            final User user = MainApplication.instance().getUser();
            swtIPlaying.setChecked(match.isIPlaying());
            if (match.isIPlaying()) {
                playersSaved.add(user.getMyFriendDTO());
            }

            players.addAll(match.getPlayers());
            List<Player> noPlayers = Lists.newArrayList();
            for (Player player : players) {
                if (!player.getName().equals(user.getUsername())) {
                    playersSaved.add(new FriendDto().username(player.getName()));
                    for (FriendDto friendDto : user.getFriends()) {
                        if (friendDto.getUsername().equals(player.getName())) {
                            noPlayers.add(player);
                            break;
                        }
                    }
                } else {
                    noPlayers.add(player);
                }
            }

            players.removeAll(noPlayers);
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

        if (Strings.isNullOrEmpty(txtCreateAt.getText().toString())) {
            match.setCreatedAt(new Date());
        } else {
            match.setCreatedAt(DateUtils.parse(txtCreateAt.getText().toString().trim()));
        }

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

        for (Player player : players) {
            match.addPlayer(player);
        }

        Log.i(TAG, "fillDataAndSave: Save the data in BD.");
        new MatchRepository().save(match);

        user.lastPlayed(match.getCreatedAt());
        instance.setUser(user);

        Log.i(TAG, "fillDataAndSave: Reload cache data.");
        EventBus.getDefault().post(ActionCacheEnum.LOAD_DATA_MATCHES);

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
        txtCreateAt = view.findViewById(R.id.card_view_match_info_created_at);
        txtCreateAt.addTextChangedListener(new MaskEditTextChangedListener("##/##/####", txtCreateAt));

        btnCreateAt = view.findViewById(R.id.card_view_match_info_btn_created_at);
        final NewMatchFragment currentFragment = this;
        btnCreateAt.setOnClickListener(new View.OnClickListener() {
            Calendar now = Calendar.getInstance();

            @Override
            public void onClick(View view) {
                if (!Strings.isNullOrEmpty(txtCreateAt.getText().toString())) {
                    now.setTime(DateUtils.parse(txtCreateAt.getText().toString().trim()));
                }

                new DatePickerDialog(getContext(),
                        currentFragment,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        swtIPlaying = view.findViewById(R.id.card_view_players_i_playing_switch);

        imgInfoGame = view.findViewById(R.id.card_view_game_info_image);
        txtInfoName = view.findViewById(R.id.card_view_game_info_name);
        txtInfoPlayers = view.findViewById(R.id.card_view_game_info_players);
        txtInfoTime = view.findViewById(R.id.card_view_game_info_time);
        txtInfoAges = view.findViewById(R.id.card_view_game_info_ages);
        txtInfoYear = view.findViewById(R.id.card_view_game_info_year);

        txtUsernamePlayer = view.findViewById(R.id.card_view_players_username);
        txtUsernamePlayer.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    processDataPlayers();
                }
                return false;
            }
        });

        btnAddPlayer = view.findViewById(R.id.card_view_players_button_add);
        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processDataPlayers();
            }
        });
    }

    private void processDataPlayers() {
        final Player player = addNewPlayer();
        if (player != null) {
            players.add(0, player);

            txtUsernamePlayer.setText(null);
            recycleViewPlayers.getAdapter().notifyDataSetChanged();

            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_ADD_PLAYER);
        }
    }

    private Player addNewPlayer() {
        Log.i(TAG, "addNewFriend: Add new player!");

        if (Strings.isNullOrEmpty(txtUsernamePlayer.getText().toString())) {
            String msgErrorUsername = getContext().getString(R.string.msg_username_player_required);
            AndroidUtils.changeErrorEditText(txtUsernamePlayer, msgErrorUsername, true);
            return null;
        }
        AndroidUtils.changeErrorEditText(txtUsernamePlayer);
        final String username = txtUsernamePlayer.getText().toString().trim();
        txtUsernamePlayer.setText(null);

        Log.d(TAG, MessageFormat.format("Create new player with username: {0}.", username));
        final Player player = new Player().name(username);
        player.initEntity();
        return player;
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

        txtInfoYear.setText(SPACE_DRAWABLE +
                MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getYearPublished()), "****"));

        if (Strings.isNullOrEmpty(gameSelected.getMaxPlayers())) {
            txtInfoPlayers.setText(MessageFormat.format(" {0}",
                    MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getMinPlayers()), "1")));
        } else {
            txtInfoPlayers.setText(MessageFormat.format(" {0}-{1}",
                    MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getMinPlayers()), "1"),
                    MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getMaxPlayers()), "*")));
        }

        if (Strings.isNullOrEmpty(gameSelected.getMaxPlayTime())) {
            txtInfoTime.setText(MessageFormat.format(" {0}´",
                    MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getMinPlayTime()), "∞")));
        } else {
            txtInfoTime.setText(MessageFormat.format(" {0}-{1}´",
                    MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getMinPlayTime()), "*"),
                    MoreObjects.firstNonNull(Strings.emptyToNull(gameSelected.getMaxPlayTime()), "∞")));
        }

        if (Strings.isNullOrEmpty(gameSelected.getAge())) {
            txtInfoAges.setText("-");
        } else {
            txtInfoAges.setText(MessageFormat.format(" {0}+", gameSelected.getAge()));
        }

        cardViewGame.setVisibility(View.VISIBLE);
    }


    private void setupRecycleViewPlayers(View view) {
        recycleViewPlayers = view.findViewById(R.id.card_view_players_recycler_view);
        recycleViewPlayers.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycleViewPlayers.setItemAnimator(new DefaultItemAnimator());
        recycleViewPlayers.setHasFixedSize(false);

        recycleViewPlayersFriends = view.findViewById(R.id.card_view_players_friends_recycler_view);
        emptyMsgPlayersFriends = view.findViewById(R.id.card_view_players_friends_empty_view);
        errorMsgPlayersFriends = view.findViewById(R.id.card_view_players_friends_error_view);
        recycleViewPlayersFriends.setEmptyView(emptyMsgPlayersFriends);
        recycleViewPlayersFriends.setErrorView(errorMsgPlayersFriends);

        recycleViewPlayersFriends.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycleViewPlayersFriends.setItemAnimator(new DefaultItemAnimator());
        recycleViewPlayersFriends.setHasFixedSize(true);

    }

    private void loadDataPlayers() {
        final MainApplication instance = MainApplication.instance();
        final User user = instance.getUser();
        final List<FriendDto> friendDtos = Lists.newLinkedList(user.getFriends());
        Collections.sort(friendDtos, new Ordering<FriendDto>() {
            @Override
            public int compare(FriendDto left, FriendDto right) {
                return left.compareTo(right);
            }
        });

        for (FriendDto friendDto : friendDtos) {
            friendDto.setSelected(Boolean.FALSE);
            for (FriendDto playerSaved : playersSaved) {
                if (friendDto.getUsername().equals(playerSaved.getUsername())) {
                    friendDto.setSelected(Boolean.TRUE);
                    break;
                }
            }
        }
        friendAdapter = new FriendAdapter(getContext(), friendDtos, false, true);
        recycleViewPlayersFriends.setAdapter(friendAdapter);

        Collections.sort(players, new Ordering<Player>() {
            @Override
            public int compare(Player left, Player right) {
                return left.compareTo(right);
            }
        });
        recycleViewPlayers.setAdapter(new PlayerAdapter(getContext(), players));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        DecimalFormat decimalFormatScore = new DecimalFormat("#00");
        StringBuilder newDate = new StringBuilder();
        newDate.append(decimalFormatScore.format(day))
                .append("/")
                .append(decimalFormatScore.format(month))
                .append("/")
                .append(year);

        txtCreateAt.setText(newDate.toString());
    }
}
