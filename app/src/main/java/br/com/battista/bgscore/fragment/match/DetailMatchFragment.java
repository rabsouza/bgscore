package br.com.battista.bgscore.fragment.match;


import static br.com.battista.bgscore.constants.BundleConstant.DATA;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.adpater.PlayerAdapter;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.fragment.BaseFragment;
import br.com.battista.bgscore.fragment.dialog.ShareMatchFullDialog;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.Player;
import br.com.battista.bgscore.util.DateUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class DetailMatchFragment extends BaseFragment {

    private static final String TAG = DetailMatchFragment.class.getSimpleName();
    private final List<Player> players = Lists.newLinkedList();
    private final List<Player> playersWinners = Lists.newLinkedList();
    private Match match;
    private Game gameSelected;
    private EditText txtMatchAlias;
    private EditText txtCreateAt;
    private EditText txtDuration;
    private CardView cardViewGame;
    private ImageView imgInfoGame;
    private TextView txtInfoName;
    private TextView txtInfoTime;
    private TextView txtInfoPlayers;
    private TextView txtInfoAges;
    private TextView txtInfoYear;
    private RecycleEmptyErrorView recycleViewPlayers;
    private CardView cardViewPlayersWinners;
    private RecycleEmptyErrorView recycleViewPlayersWinners;
    private ImageView imgFeedback;
    private TextView txtFeedback;
    private EditText txtFeedbackObs;

    private ViewGroup containerDetailMatch;

    public DetailMatchFragment() {
    }

    public static DetailMatchFragment newInstance(Match match) {
        DetailMatchFragment fragment = new DetailMatchFragment();
        Bundle args = new Bundle();
        if (match != null) {
            args.putSerializable(DATA, match);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create new DetailMatchFragment!");

        final View view = inflater.inflate(R.layout.fragment_detail_match, container, false);

        final Fragment currentFragment = this;
        FloatingActionButton fab = view.findViewById(R.id.fab_share_detail_match);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewClicked) {
                Log.i(TAG, "processShareMatch: Share the match!");
                new ShareMatchFullDialog().newInstance(match).showDialog(currentFragment);
            }
        });

        setupRecycleViewPlayersAndWinners(view);
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
        recycleViewPlayers.setAdapter(new PlayerAdapter(getContext(),
                players, false, false, false, true, false));

        if (playersWinners.isEmpty()) {
            cardViewPlayersWinners.setVisibility(View.GONE);
        } else {
            Collections.sort(playersWinners, new Ordering<Player>() {
                @Override
                public int compare(Player left, Player right) {
                    return left.compareTo(right);
                }
            });
            recycleViewPlayersWinners.setAdapter(new PlayerAdapter(getContext(),
                    playersWinners, false, false, true, true, false));
        }
    }

    private void processDataFragment(View viewFragment, Bundle bundle) {
        Log.d(TAG, "processDataFragment: Process bundle data Fragment!");
        if (bundle != null && bundle.containsKey(BundleConstant.DATA)) {
            match = (Match) bundle.getSerializable(BundleConstant.DATA);
            match.reloadId();

            txtMatchAlias.setText(match.getAlias());
            txtCreateAt.setText(DateUtils.format(match.getCreatedAt()));

            gameSelected = match.getGame();
            fillCardGame();

            players.addAll(match.getPlayers());
            for (Player player : players) {
                if (player.isWinner()) {
                    playersWinners.add(player);
                }
            }

            txtDuration.setText(DateUtils.formatTime(match.getDuration()));
            txtFeedbackObs.setText(match.getObs());
            if (Strings.isNullOrEmpty(match.getObs())) {
                txtFeedbackObs.setVisibility(View.GONE);
            }

            imgFeedback.setImageResource(match.getFeedback().getIdResDrawable());
            final int colorFeedbackDissatisfied = ContextCompat.getColor(getContext(), R.color.colorImgFeedbackDissatisfied);
            final int colorFeedbackNeutral = ContextCompat.getColor(getContext(), R.color.colorImgFeedbackNeutral);
            final int colorFeedbackSatisfied = ContextCompat.getColor(getContext(), R.color.colorImgFeedbackSatisfied);
            switch (match.getFeedback().getIdResDrawable()) {
                case R.drawable.ic_feedback_very_dissatisfied:
                    imgFeedback.setColorFilter(
                            colorFeedbackDissatisfied);
                    txtFeedback.setText(R.string.feedback_very_dissatisfied);
                    txtFeedback.setTextColor(colorFeedbackDissatisfied);
                    break;
                case R.drawable.ic_feedback_dissatisfied:
                    imgFeedback.setColorFilter(
                            colorFeedbackDissatisfied);
                    txtFeedback.setText(R.string.feedback_dissatisfied);
                    txtFeedback.setTextColor(colorFeedbackDissatisfied);
                    break;
                case R.drawable.ic_feedback_neutral:
                    imgFeedback.setColorFilter(
                            colorFeedbackNeutral);
                    txtFeedback.setText(R.string.feedback_neutral);
                    txtFeedback.setTextColor(colorFeedbackNeutral);
                    break;
                case R.drawable.ic_feedback_satisfied:
                    imgFeedback.setColorFilter(
                            colorFeedbackSatisfied);
                    txtFeedback.setText(R.string.feedback_satisfied);
                    txtFeedback.setTextColor(colorFeedbackSatisfied);
                    break;
                case R.drawable.ic_feedback_very_satisfied:
                    imgFeedback.setColorFilter(
                            colorFeedbackSatisfied);
                    txtFeedback.setText(R.string.feedback_very_satisfied);
                    txtFeedback.setTextColor(colorFeedbackSatisfied);
                    break;
            }
        } else {
            match = new Match();
            match.initEntity();
        }
    }

    private void setupRecycleViewPlayersAndWinners(View view) {
        recycleViewPlayers = view.findViewById(R.id.card_view_players_recycler_view);
        recycleViewPlayers.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycleViewPlayers.setItemAnimator(new DefaultItemAnimator());
        recycleViewPlayers.setHasFixedSize(true);

        cardViewPlayersWinners = view.findViewById(R.id.card_view_players_winners);
        recycleViewPlayersWinners = view.findViewById(R.id.card_view_players_winners_recycler_view);
        recycleViewPlayersWinners.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycleViewPlayersWinners.setItemAnimator(new DefaultItemAnimator());
        recycleViewPlayersWinners.setHasFixedSize(true);

    }

    private void setupDataForm(final View view) {
        Log.i(TAG, "setupDataForm: Load all form fields!");

        txtMatchAlias = view.findViewById(R.id.card_view_match_info_alias);
        txtCreateAt = view.findViewById(R.id.card_view_match_info_created_at);

        txtDuration = view.findViewById(R.id.card_view_match_info_duration);
        txtDuration.addTextChangedListener(new MaskEditTextChangedListener("##:##", txtDuration));

        cardViewGame = view.findViewById(R.id.card_view_game_info);

        imgInfoGame = view.findViewById(R.id.card_view_game_info_image);
        txtInfoName = view.findViewById(R.id.card_view_game_info_name);
        txtInfoPlayers = view.findViewById(R.id.card_view_game_info_players);
        txtInfoTime = view.findViewById(R.id.card_view_game_info_time);
        txtInfoAges = view.findViewById(R.id.card_view_game_info_ages);
        txtInfoYear = view.findViewById(R.id.card_view_game_info_year);

        txtFeedbackObs = view.findViewById(R.id.card_view_match_obs_text);
        imgFeedback = view.findViewById(R.id.card_view_feedback_img);
        txtFeedback = view.findViewById(R.id.card_view_feedback_text);

        containerDetailMatch = view.findViewById(R.id.container_detail_view);

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

}
