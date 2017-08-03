package br.com.battista.bgscore.fragment.match;


import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.HomeActivity;
import br.com.battista.bgscore.adpater.PlayerAdapter;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.fragment.BaseFragment;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.Player;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.util.DateUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

import static br.com.battista.bgscore.constants.BundleConstant.DATA;
import static br.com.battista.bgscore.constants.BundleConstant.NAVIGATION_TO;
import static br.com.battista.bgscore.constants.BundleConstant.NavigationTo.MATCH_FRAGMENT;
import static br.com.battista.bgscore.constants.ViewConstant.SPACE_DRAWABLE;


public class FinishMatchFragment extends BaseFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = FinishMatchFragment.class.getSimpleName();

    private Match match;
    private Game gameSelected;

    private EditText txtMatchAlias;
    private EditText txtCreateAt;
    private EditText txtDuration;
    private ImageButton btnDuration;

    private CardView cardViewGame;
    private ImageView imgInfoGame;
    private TextView txtInfoName;
    private TextView txtInfoTime;
    private TextView txtInfoPlayers;
    private TextView txtInfoAges;
    private TextView txtInfoYear;

    private RecycleEmptyErrorView recycleViewPlayers;
    final List<Player> players = Lists.newLinkedList();

    public FinishMatchFragment() {
    }

    public static FinishMatchFragment newInstance(Match match) {
        FinishMatchFragment fragment = new FinishMatchFragment();
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
        Log.d(TAG, "onCreateView: Create new FinishMatchFragment!");

        final View view = inflater.inflate(R.layout.fragment_finish_match, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab_next_finish_match);
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

    private void loadDataPlayers() {
        Collections.sort(players, new Ordering<Player>() {
            @Override
            public int compare(Player left, Player right) {
                return left.compareTo(right);
            }
        });
        recycleViewPlayers.setAdapter(new PlayerAdapter(getContext(), players, false, true));
    }

    private void processDataFragment(View viewFragment, Bundle bundle) {
        Log.d(TAG, "processDataFragment: Process bundle data Fragment!");
        if (bundle.containsKey(BundleConstant.DATA)) {
            match = (Match) bundle.getSerializable(BundleConstant.DATA);
            match.reloadId();

            txtMatchAlias.setText(match.getAlias());
            txtCreateAt.setText(DateUtils.format(match.getCreatedAt()));

            gameSelected = match.getGame();
            fillCardGame();

            players.addAll(match.getPlayers());

        } else {
            match = new Match();
            match.initEntity();
        }
    }

    private void fillDataAndSave() {
        Log.i(TAG, "fillDataAndSave: Validate form data and fill data to save!");

        Log.i(TAG, "fillDataAndSave: Save the data in BD.");
        new MatchRepository().save(match);

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

    private void setupRecycleViewPlayers(View view) {
        recycleViewPlayers = view.findViewById(R.id.card_view_players_recycler_view);
        recycleViewPlayers.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycleViewPlayers.setItemAnimator(new DefaultItemAnimator());
        recycleViewPlayers.setHasFixedSize(false);

    }

    private void setupDataForm(final View view) {
        Log.i(TAG, "setupDataForm: Load all form fields!");

        txtMatchAlias = view.findViewById(R.id.card_view_match_info_alias);
        txtCreateAt = view.findViewById(R.id.card_view_match_info_created_at);

        txtDuration = view.findViewById(R.id.card_view_match_info_duration);
        txtDuration.addTextChangedListener(new MaskEditTextChangedListener("##:##", txtDuration));

        btnDuration = view.findViewById(R.id.card_view_match_info_btn_duration);
        final FinishMatchFragment currentFragment = this;
        btnDuration.setOnClickListener(new View.OnClickListener() {
            Calendar now = Calendar.getInstance();

            @Override
            public void onClick(View view) {
                if (!Strings.isNullOrEmpty(txtCreateAt.getText().toString())) {
                    now.setTime(DateUtils.parse(txtCreateAt.getText().toString().trim()));
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

        cardViewGame = view.findViewById(R.id.card_view_game_info);

        imgInfoGame = view.findViewById(R.id.card_view_game_info_image);
        txtInfoName = view.findViewById(R.id.card_view_game_info_name);
        txtInfoPlayers = view.findViewById(R.id.card_view_game_info_players);
        txtInfoTime = view.findViewById(R.id.card_view_game_info_time);
        txtInfoAges = view.findViewById(R.id.card_view_game_info_ages);
        txtInfoYear = view.findViewById(R.id.card_view_game_info_year);

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

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        DecimalFormat decimalFormatScore = new DecimalFormat("#00");
        StringBuilder newTime = new StringBuilder();
        newTime.append(decimalFormatScore.format(hour))
                .append(":")
                .append(decimalFormatScore.format(minute));

        txtDuration.setText(newTime.toString());

    }
}
