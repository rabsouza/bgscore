package br.com.battista.bgscore.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.GameActivity;
import br.com.battista.bgscore.activity.MatchActivity;
import br.com.battista.bgscore.adpater.RankingGamesAdapter;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.custom.ScoreboardView;
import br.com.battista.bgscore.fragment.dialog.RankingGamesDialog;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.RankingGamesDto;
import br.com.battista.bgscore.service.Inject;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.DateUtils;
import br.com.battista.bgscore.util.ImageLoadUtils;
import br.com.battista.bgscore.util.LogUtils;

public class HomeFragment extends BaseFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int MAX_MATCHES_LIST = 5;

    private SwipeRefreshLayout refreshLayout;

    private RecycleEmptyErrorView recycleViewRankingGames;
    private TextView emptyMsgRankingGames;
    private TextView errorMsgRankingGames;

    private TextView txtUsername;
    private TextView txtLastPlay;
    private ImageView imgAvatar;

    private ScoreboardView scoreGames;
    private ScoreboardView scoreMatches;
    private ScoreboardView scoreTotalTime;

    private ImageView imgHelpRankingGame;
    private ImageView imgDataRankingGame;

    private Boolean isFabOpen = Boolean.FALSE;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;


    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.d(TAG, "onCreateView: Create new HomeFragment!");
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() -> loadAllData(view));

        setupFabs(view);
        setupRecycleRanking(view);
        setupHelpRankingGame(view);
        setupDataRankingGame(view);

        return view;
    }

    private void setupFabs(View view) {
        LogUtils.d(TAG, "setupFabs: Stup all FloatingActionButton!");

        FloatingActionButton fabNewMatch = view.findViewById(R.id.fab_new_match);
        fabNewMatch.setOnClickListener(view1 -> {
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_ADD_MATCH);

            Bundle args = new Bundle();
            Intent intent = new Intent(getContext(), MatchActivity.class);
            intent.putExtras(args);

            getContext().startActivity(intent);
        });

        FloatingActionButton fabNewGame = view.findViewById(R.id.fab_new_game);
        fabNewGame.setOnClickListener(view1 -> {
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_ADD_GAME);

            Bundle args = new Bundle();
            Intent intent = new Intent(getContext(), GameActivity.class);
            intent.putExtras(args);

            getContext().startActivity(intent);
        });

        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);

        FloatingActionButton fabNewOption = view.findViewById(R.id.fab_new_option);
        fabNewOption.setOnClickListener(view1 -> {
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_ADD_OPTION);

            if (isFabOpen) {
                fabNewOption.startAnimation(rotate_backward);
                fabNewMatch.startAnimation(fab_close);
                fabNewGame.startAnimation(fab_close);
                fabNewMatch.setClickable(false);
                fabNewGame.setClickable(false);
                isFabOpen = false;
            } else {
                fabNewOption.startAnimation(rotate_forward);
                fabNewMatch.startAnimation(fab_open);
                fabNewGame.startAnimation(fab_open);
                fabNewMatch.setClickable(true);
                fabNewGame.setClickable(true);
                isFabOpen = true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAllSyncData(getView());
    }

    private void setupHelpRankingGame(View view) {
        imgHelpRankingGame = view.findViewById(R.id.card_view_stats_help);
        imgHelpRankingGame.setOnClickListener(view1 -> {
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_LEGEND_RANKING_GAME);

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.dialog_help_ranking_game, null);

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.title_help)
                    .setView(customView)
                    .setPositiveButton(R.string.btn_ok,
                            (dialog, whichButton) -> dialog.dismiss()
                    )
                    .create();
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.animationAlert;
            alertDialog.show();
        });
    }

    private void setupDataRankingGame(View view) {
        imgDataRankingGame = view.findViewById(R.id.card_view_ranking_games);
        imgDataRankingGame.setOnClickListener(view1 -> {
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_DATA_RANKING_GAME);

            final FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
            new RankingGamesDialog().newInstance().showDialog(supportFragmentManager.findFragmentById(R.id.container));
        });
    }

    private void loadAllData(final View view) {
        LogUtils.i(TAG, "loadAllData: Load all data in BD and update cache!");
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new LoadAllDataAsyncTask(view).execute();
    }

    private void loadAllSyncData(final View view) {
        LogUtils.i(TAG, "loadAllData: Load all data in BD!");
        loadUserInfo(view);
        loadAllRankingGames();
    }

    private void loadAllRankingGames() {
        LogUtils.i(TAG, "loadAllRankingGames: Load all Ranking Games in BD!");
        User user = MainApplication.instance().getUser();

        List<RankingGamesDto> matches = Lists.newLinkedList(user.getRankingGames());
        Collections.sort(matches, new Ordering<RankingGamesDto>() {
            @Override
            public int compare(RankingGamesDto left, RankingGamesDto right) {
                return left.compareTo(right);
            }
        });
        Collections.reverse(matches);

        if (matches.size() > MAX_MATCHES_LIST) {
            matches = matches.subList(0, MAX_MATCHES_LIST);
        }
        final RankingGamesAdapter rankingGamesAdapter = new RankingGamesAdapter(getContext(), matches);
        recycleViewRankingGames.setAdapter(rankingGamesAdapter);

    }

    private void loadUserInfo(final View view) {
        LogUtils.i(TAG, "loadUserInfo: Load all info user to cache and update user statistics!");
        User user = MainApplication.instance().getUser();

        imgAvatar = view.findViewById(R.id.card_view_home_img);
        if (user.isSignedSuccessfully() && user.getUrlAvatar() != null) {
            ImageLoadUtils.loadImage(view.getContext(),
                    user.getUrlAvatar(),
                    imgAvatar);
        } else {
            imgAvatar.setImageResource(user.getAvatar().getIdResDrawable());
        }

        txtUsername = view.findViewById(R.id.card_view_home_username);
        txtUsername.setText(getString(R.string.text_home_username, user.getUsername()));

        txtLastPlay = view.findViewById(R.id.card_view_home_last_play);
        String lastPlay = "-";
        if (user.getLastPlayed() != null && user.getNumMatches() > 0) {
            Calendar lastPlayCalendar = Calendar.getInstance();
            lastPlayCalendar.setTime(user.getLastPlayed());
            lastPlay = DateUtils.format(lastPlayCalendar);
        }
        txtLastPlay.setText(getString(R.string.text_home_last_play, lastPlay));

        DecimalFormat decimalFormatScore = new DecimalFormat("#00");
        scoreGames = view.findViewById(R.id.card_view_score_games);
        scoreGames.setScoreText(decimalFormatScore.format(user.getNumGames()));
        scoreGames.setOnClickListener(v -> view.getRootView().findViewById(R.id.action_games).callOnClick());

        scoreMatches = view.findViewById(R.id.card_view_score_matches);
        scoreMatches.setScoreText(decimalFormatScore.format(user.getNumMatches()));
        scoreMatches.setOnClickListener(v -> view.getRootView().findViewById(R.id.action_matches).callOnClick());

        scoreTotalTime = view.findViewById(R.id.card_view_score_total_time);
        scoreTotalTime.setScoreText(DateUtils.formatTime(user.getTotalTime()));
        scoreTotalTime.setOnClickListener(view1 -> {
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_DATA_RANKING_GAME);

            final FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
            new RankingGamesDialog().newInstance().showDialog(supportFragmentManager.findFragmentById(R.id.container));
        });
    }

    private void setupRecycleRanking(View view) {
        recycleViewRankingGames = view.findViewById(R.id.card_view_stats_recycler_view);
        emptyMsgRankingGames = view.findViewById(R.id.card_view_stats_empty_view);
        errorMsgRankingGames = view.findViewById(R.id.card_view_stats_error_view);
        recycleViewRankingGames.setEmptyView(emptyMsgRankingGames);
        recycleViewRankingGames.setErrorView(errorMsgRankingGames);

        recycleViewRankingGames.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewRankingGames.setItemAnimator(new DefaultItemAnimator());
        recycleViewRankingGames.setHasFixedSize(true);
        recycleViewRankingGames.setItemViewCacheSize(10);
        recycleViewRankingGames.setDrawingCacheEnabled(true);
        recycleViewRankingGames.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    private class LoadAllDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private final View view;

        public LoadAllDataAsyncTask(View view) {
            this.view = view;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Inject.provideCacheManageService().reloadSyncAllDataCache();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadUserInfo(view);
            loadAllRankingGames();
            refreshLayout.setRefreshing(false);
        }
    }
}
