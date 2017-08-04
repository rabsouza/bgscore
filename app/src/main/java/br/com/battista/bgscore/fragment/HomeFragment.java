package br.com.battista.bgscore.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import br.com.battista.bgscore.activity.MatchActivity;
import br.com.battista.bgscore.adpater.RankingGamesAdapter;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.custom.ScoreboardView;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.RankingGamesDto;
import br.com.battista.bgscore.service.CacheManageService;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.DateUtils;

public class HomeFragment extends BaseFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int MAX_MATCHES_LIST = 5;

    private SwipeRefreshLayout refreshLayout;

    private RecycleEmptyErrorView recycleViewRankingGames;
    private TextView emptyMsgRankingGames;
    private TextView errorMsgRankingGames;

    private TextView txtUsername;
    private TextView txtLastPlay;
    private ImageView txtAvatar;

    private ScoreboardView scoreGames;
    private ScoreboardView scoreMatches;
    private ScoreboardView scoreTotalTime;

    private ImageView imgHelpRankingGame;

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
        Log.d(TAG, "onCreateView: Create new HomeFragment!");

        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new CacheManageService().reloadAllDataCache();
                loadUserInfo(view);
                loadAllRankingGames();
                refreshLayout.setRefreshing(false);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_new_match);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                        CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_ADD_MATCH);
                Bundle args = new Bundle();
                Intent intent = new Intent(getContext(), MatchActivity.class);
                intent.putExtras(args);

                getContext().startActivity(intent);
            }
        });

        new CacheManageService().reloadAllDataCache();
        setupRecycleRanking(view);
        setupHelpRankingGame(view);

        return view;
    }

    private void setupHelpRankingGame(View view) {
        imgHelpRankingGame = view.findViewById(R.id.card_view_stats_help);
        imgHelpRankingGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                        CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_LEGEND_RANKING_GAME);

                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.dialog_help_ranking_game, null);

                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.title_help)
                        .setView(customView)
                        .setPositiveButton(R.string.btn_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .create().show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo(getView());
        loadAllRankingGames();
    }

    private void loadAllRankingGames() {
        Log.i(TAG, "loadAllRankingGames: Load all Ranking Games in BD!");
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

    private void loadUserInfo(View view) {
        Log.i(TAG, "loadUserInfo: Load all info user to cache and update user statistics!");
        User user = MainApplication.instance().getUser();

        txtAvatar = view.findViewById(R.id.card_view_home_img);
        txtAvatar.setImageResource(user.getIdResAvatar());

        txtUsername = view.findViewById(R.id.card_view_home_username);
        txtUsername.setText(getString(R.string.text_home_username, user.getUsername()));

        txtLastPlay = view.findViewById(R.id.card_view_last_play);
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

        scoreMatches = view.findViewById(R.id.card_view_score_matches);
        scoreMatches.setScoreText(decimalFormatScore.format(user.getNumMatches()));

        scoreTotalTime = view.findViewById(R.id.card_view_score_total_time);
        scoreTotalTime.setScoreText(DateUtils.formatTime(user.getTotalTime()));
    }

    private void setupRecycleRanking(View view) {
        recycleViewRankingGames = view.findViewById(R.id.card_view_stats_recycler_view);
        emptyMsgRankingGames = view.findViewById(R.id.card_view_stats_empty_view);
        errorMsgRankingGames = view.findViewById(R.id.card_view_stats_error_view);
        recycleViewRankingGames.setEmptyView(emptyMsgRankingGames);
        recycleViewRankingGames.setErrorView(errorMsgRankingGames);

        recycleViewRankingGames.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewRankingGames.setItemAnimator(new DefaultItemAnimator());
        recycleViewRankingGames.setHasFixedSize(false);
    }
}
