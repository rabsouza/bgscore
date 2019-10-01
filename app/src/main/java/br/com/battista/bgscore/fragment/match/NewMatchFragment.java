package br.com.battista.bgscore.fragment.match;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
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
import br.com.battista.bgscore.custom.ProgressApp;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.fragment.BaseFragment;
import br.com.battista.bgscore.fragment.dialog.SearchGameDialog;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.Player;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.FriendDto;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.model.enuns.TypePlayerEnum;
import br.com.battista.bgscore.model.response.GameResponse;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.repository.PlayerRepository;
import br.com.battista.bgscore.service.Inject;
import br.com.battista.bgscore.service.server.GameService;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.DateUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.battista.bgscore.util.LogUtils;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

import static br.com.battista.bgscore.constants.BundleConstant.DATA;
import static br.com.battista.bgscore.constants.BundleConstant.NAVIGATION_TO;
import static br.com.battista.bgscore.constants.BundleConstant.NavigationTo.MATCH_FRAGMENT;
import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_SEARCH_GAME_FRAGMENT;

public class NewMatchFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = NewMatchFragment.class.getSimpleName();
    final List<Player> players = Lists.newLinkedList();
    private final Map<String, Game> gameMap = Maps.newTreeMap();
    private RecycleEmptyErrorView recycleViewPlayers;
    private ImageButton btnAddPlayer;
    private EditText txtUsernamePlayer;
    private RecycleEmptyErrorView recycleViewPlayersFriends;
    private TextView emptyMsgPlayersFriends;
    private TextView errorMsgPlayersFriends;
    private Game gameSelected;
    private FriendAdapter friendAdapter;
    private Set<FriendDto> friendsSelected = Sets.newLinkedHashSet();
    private Set<FriendDto> playersSaved = Sets.newLinkedHashSet();

    private ImageButton btnSearchGame;
    private AutoCompleteTextView txtSearchNameGame;
    private MaterialCardView cardViewGame;
    private Switch swtSearchOnline;

    private EditText txtMatchAlias;
    private EditText txtCreateAt;
    private ImageButton btnCreateAt;
    private Switch swtIPlaying;
    private Switch swtScheduleMatch;
    private ViewGroup grpSchedule;
    private EditText txtScheduleTime;
    private ImageButton btnScheduleTime;

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
        LogUtils.d(TAG, "onCreateView: Create new NewMatchFragment!");

        final View view = inflater.inflate(R.layout.fragment_new_match, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab_next_edit_match);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewClicked) {
                fillDataAndSave();
            }
        });

        setupRecycleViewPlayersAndFriends(view);
        setupDataForm(view);

        processDataFragment(view, getArguments());
        loadDataPlayers();
        return view;
    }

    private void processDataFragment(View viewFragment, Bundle bundle) {
        LogUtils.d(TAG, "processDataFragment: Process bundle data Fragment!");
        if (bundle != null && bundle.containsKey(BundleConstant.DATA)) {
            match = (Match) bundle.getSerializable(BundleConstant.DATA);
            match.reloadId();

            txtMatchAlias.setText(match.getAlias());
            txtCreateAt.setText(DateUtils.format(match.getCreatedAt()));

            if (match.getSchedule() == null) {
                txtScheduleTime.setText(DateUtils.formatHours(new Date()));
            } else {
                txtScheduleTime.setText(DateUtils.formatHours(match.getSchedule()));
            }

            gameSelected = match.getGame();
            txtSearchNameGame.setText(gameSelected.getName());
            fillCardGame();

            final User user = MainApplication.instance().getUser();
            swtIPlaying.setChecked(match.isIPlaying());

            swtScheduleMatch.setChecked(match.isScheduleMatch());
            if (match.isIPlaying()) {
                playersSaved.add(new FriendDto().username(user.getUsername()));
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
        LogUtils.i(TAG, "fillDataAndSave: Validate form data and fill data to save!");
        if (Strings.isNullOrEmpty(txtMatchAlias.getText().toString())) {
            String msgErrorUsername = getContext().getString(R.string.msg_alias_match_required);
            AndroidUtils.changeErrorEditText(txtMatchAlias, msgErrorUsername, true);
            return;
        }
        AndroidUtils.changeErrorEditText(txtMatchAlias);
        match.alias(txtMatchAlias.getText().toString().trim());

        if (Strings.isNullOrEmpty(txtCreateAt.getText().toString())) {
            match.createdAt(new Date());
        } else {
            Calendar calendarCreatedAt = Calendar.getInstance();
            Date createdAt = DateUtils.parse(txtCreateAt.getText().toString().trim());
            if (createdAt != null) {
                calendarCreatedAt.setTime(createdAt);
            }

            Calendar now = Calendar.getInstance();
            calendarCreatedAt.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
            calendarCreatedAt.set(Calendar.MINUTE, now.get(Calendar.MINUTE));

            match.createdAt(calendarCreatedAt.getTime());
        }

        if (swtScheduleMatch.isChecked()) {
            Calendar calendarCreatedAt = Calendar.getInstance();
            calendarCreatedAt.setTime(match.getCreatedAt());

            Calendar calendarSchedule = Calendar.getInstance();
            String strDateSchedule = String.format("%s %s", txtCreateAt.getText().toString().trim(), txtScheduleTime.getText().toString().trim());
            Date schedule = DateUtils.parseWithHours(strDateSchedule);
            calendarSchedule.setTime(schedule);
            calendarSchedule.set(Calendar.YEAR, calendarCreatedAt.get(Calendar.YEAR));
            calendarSchedule.set(Calendar.MONTH, calendarCreatedAt.get(Calendar.MONTH));
            calendarSchedule.set(Calendar.DAY_OF_MONTH, calendarCreatedAt.get(Calendar.DAY_OF_MONTH));

            match.schedule(calendarSchedule.getTime());
        } else {
            match.schedule(null);
        }

        if (gameSelected == null) {
            String msgErrorUsername = getContext().getString(R.string.msg_game_required);
            AndroidUtils.changeErrorEditText(txtSearchNameGame, msgErrorUsername, true);
            return;
        }
        AndroidUtils.changeErrorEditText(txtSearchNameGame);
        match.game(gameSelected);

        for (Player player : match.getPlayers()) {
            new PlayerRepository().delete(player);
        }
        match.clearPlayers();
        friendsSelected.clear();

        friendsSelected.addAll(friendAdapter.getFriendsSelected());
        final MainApplication instance = MainApplication.instance();
        final User user = instance.getUser();
        if (swtIPlaying.isChecked()) {
            match.addPlayer(user.getMyPlayer());
        }
        match.iPlaying(swtIPlaying.isChecked());
        match.scheduleMatch(swtScheduleMatch.isChecked());

        for (FriendDto friendDto : friendsSelected) {
            Player player = new Player();
            player.initEntity();
            player.setTypePlayer(TypePlayerEnum.FRIEND);
            player.name(friendDto.getUsername());
            match.addPlayer(player);
        }

        for (Player player : players) {
            match.addPlayer(player);
        }

        LogUtils.i(TAG, "fillDataAndSave: Save the data in BD.");
        new MatchRepository().save(match);

        user.lastPlayed(match.getCreatedAt());
        instance.setUser(user);

        LogUtils.i(TAG, "fillDataAndSave: Reload cache data.");
        AndroidUtils.postAction(ActionCacheEnum.LOAD_DATA_MATCHES);

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
        LogUtils.i(TAG, "setupDataForm: Load all form fields!");
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

        swtSearchOnline = view.findViewById(R.id.card_view_players_search_online_switch);
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
                    Date dateNow = DateUtils.parse(txtCreateAt.getText().toString().trim());
                    if (dateNow != null) {
                        now.setTime(dateNow);
                    } else {
                        now.setTime(new Date());
                    }

                }

                new DatePickerDialog(getContext(),
                        currentFragment,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        swtIPlaying = view.findViewById(R.id.card_view_players_i_playing_switch);
        grpSchedule = view.findViewById(R.id.card_view_players_group_schedule_match);
        swtScheduleMatch = view.findViewById(R.id.card_view_players_schedule_match);
        swtScheduleMatch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    grpSchedule.setVisibility(View.VISIBLE);
                } else {
                    grpSchedule.setVisibility(View.GONE);
                }
            }
        });

        txtScheduleTime = view.findViewById(R.id.card_view_players_group_schedule_match_time);
        txtScheduleTime.addTextChangedListener(new MaskEditTextChangedListener("##:##", txtScheduleTime));

        btnScheduleTime = view.findViewById(R.id.card_view_players_group_schedule_match_btn_time);
        btnScheduleTime.setOnClickListener(new View.OnClickListener() {
            Calendar now = Calendar.getInstance();

            @Override
            public void onClick(View view) {
                if (!Strings.isNullOrEmpty(txtScheduleTime.getText().toString())) {
                    final Long time = DateUtils.parseTime(txtScheduleTime.getText().toString().trim());
                    int minute = time.intValue() % 60;
                    int hour = time.intValue() / 60;
                    now.set(Calendar.HOUR_OF_DAY, hour);
                    now.set(Calendar.MINUTE, minute);
                } else {
                    now.set(Calendar.HOUR_OF_DAY, 0);
                    now.set(Calendar.MINUTE, 0);
                }

                new TimePickerDialog(getContext(),
                        currentFragment,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true).show();
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DIALOG_SEARCH_GAME_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    final long gameId = data.getLongExtra(BundleConstant.SEARCH_GAME_ID, 0l);
                    LogUtils.i(TAG, MessageFormat.format("onActivityResult: Add new game: {0}",
                            gameId));

                    new ProgressApp(this.getActivity(), R.string.msg_action_saving, false) {
                        Game game;

                        @Override
                        protected void onPostExecute(Boolean result) {
                            if (game == null) {
                                AndroidUtils.snackbar(getView(), R.string.msg_game_error_add_game);
                            } else {
                                game.setMyGame(Boolean.FALSE);
                                new GameRepository().save(game);

                                gameSelected = game;
                                fillCardGame();
                            }
                            dismissProgress();
                        }

                        @Override
                        protected Boolean doInBackground(Void... params) {
                            final GameService gameService = Inject.provideGameService();
                            game = gameService.loadGame(gameId);
                            return true;
                        }
                    }.execute();

                    AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                            CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_ADD_GAME_ONLINE);
                }
                break;
        }
    }

    private Player addNewPlayer() {
        LogUtils.i(TAG, "addNewFriend: Add new player!");

        if (Strings.isNullOrEmpty(txtUsernamePlayer.getText().toString())) {
            String msgErrorUsername = getContext().getString(R.string.msg_username_player_required);
            AndroidUtils.changeErrorEditText(txtUsernamePlayer, msgErrorUsername, true);
            return null;
        }
        AndroidUtils.changeErrorEditText(txtUsernamePlayer);
        final String username = txtUsernamePlayer.getText().toString().trim();
        txtUsernamePlayer.setText(null);

        LogUtils.d(TAG, MessageFormat.format("Create new player with username: {0}.", username));
        final Player player = new Player().name(username).typePlayer(TypePlayerEnum.PLAYER);
        player.initEntity();
        return player;
    }

    private void processDataSearchGame(final View viewContainer) {
        if (Strings.isNullOrEmpty(txtSearchNameGame.getText().toString())) {
            String msgErrorUsername = getContext().getString(R.string.msg_name_game_required);
            AndroidUtils.changeErrorEditText(txtSearchNameGame, msgErrorUsername, true);
            return;
        }
        AndroidUtils.changeErrorEditText(txtSearchNameGame);
        final String nameGame = txtSearchNameGame.getText().toString().trim();
        AndroidUtils.hideKeyboard(txtSearchNameGame, getActivity());

        if (swtSearchOnline.isChecked()) {
            LogUtils.i(TAG, "processDataSearchGame: Search game in server!");
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_SEARCH_GAME_ONLINE);

            final Fragment currentFragment = this;
            new ProgressApp(this.getActivity(), R.string.msg_action_searching, false) {
                List<GameResponse> gameResponses;

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
        } else {
            LogUtils.i(TAG, "processDataSearchGame: Search game in BD!");
            if (gameMap.containsKey(nameGame)) {
                gameSelected = gameMap.get(nameGame);
                fillCardGame();
            } else {
                AndroidUtils.snackbar(viewContainer, R.string.msg_game_dont_found_search);
            }
        }
    }

    private void fillCardGame() {
        LogUtils.i(TAG, "processDataSearchGame: Fill the data Game!");

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


    private void setupRecycleViewPlayersAndFriends(View view) {
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
        recycleViewPlayersFriends.setItemViewCacheSize(20);
        recycleViewPlayersFriends.setDrawingCacheEnabled(true);
        recycleViewPlayersFriends.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

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
                .append(decimalFormatScore.format(month + 1))
                .append("/")
                .append(year);

        txtCreateAt.setText(newDate.toString());
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        DecimalFormat decimalFormatScore = new DecimalFormat("#00");
        StringBuilder newTime = new StringBuilder();
        newTime.append(decimalFormatScore.format(hour))
                .append(":")
                .append(decimalFormatScore.format(minute));

        txtScheduleTime.setText(newTime.toString());
    }
}
