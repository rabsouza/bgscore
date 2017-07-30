package br.com.battista.bgscore.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.MatchActivity;
import br.com.battista.bgscore.adpater.MatchAdapter;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.custom.ScoreboardView;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.DateUtils;

public class HomeFragment extends BaseFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int MAX_MATCHES_LIST = 5;

    private SwipeRefreshLayout refreshLayout;

    private RecycleEmptyErrorView recycleViewRankingGames;
    private TextView emptyMsgRankingGames;
    private TextView errorMsgRankingGames;

    private TextView usernameView;
    private TextView lastPlayView;
    private ImageView avatarView;

    private ScoreboardView scoreGames;
    private ScoreboardView scoreMatches;
    private ScoreboardView scoreTotalTime;

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
                loadUserInfo(view);
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

        setupRecycleRanking(view);
        loadUserInfo(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo(getView());
        loadAllMatches();
    }

    private void loadAllMatches() {
        Log.i(TAG, "loadAllMatches: Load all Matches in BD!");
        List<Match> matches = new MatchRepository().findAll();
        if(matches.size() > MAX_MATCHES_LIST){
            matches = matches.subList(0, MAX_MATCHES_LIST);
        }
        recycleViewRankingGames.setAdapter(new MatchAdapter(getContext(), matches));

    }

    private void loadUserInfo(View view) {
        Log.i(TAG, "loadUserInfo: Load all info user to cache and update user statistics!");
        User user = MainApplication.instance().getUser();

        avatarView = view.findViewById(R.id.card_view_home_img);
        avatarView.setImageResource(user.getIdResAvatar());

        usernameView = view.findViewById(R.id.card_view_home_username);
        usernameView.setText(user.getUsername());

        lastPlayView = view.findViewById(R.id.card_view_last_play);
        String lastPlay = "-";
        if (user.getLastPlay() != null && user.getNumMatches() > 0) {
            Calendar lastPlayCalendar = Calendar.getInstance();
            lastPlayCalendar.setTime(user.getLastPlay());
            lastPlay = DateUtils.format(lastPlayCalendar);
        }
        lastPlayView.setText(getString(R.string.text_home_last_play, lastPlay));

        DecimalFormat decimalFormatScore = new DecimalFormat("#00");
        scoreGames = view.findViewById(R.id.card_view_score_games);
        scoreGames.setScoreText(decimalFormatScore.format(user.getNumGames()));

        scoreMatches = view.findViewById(R.id.card_view_score_matches);
        scoreMatches.setScoreText(decimalFormatScore.format(user.getNumMatches()));

        scoreTotalTime = view.findViewById(R.id.card_view_score_total_time);
        scoreTotalTime.setScoreText(DateUtils.convertSecToHours(user.getTotalTime()));
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
    }
}
