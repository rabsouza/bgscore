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
import android.widget.TextView;

import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.MatchActivity;
import br.com.battista.bgscore.adpater.MatchAdapter;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.util.AnswersUtils;


public class MatchFragment extends BaseFragment {

    private static final String TAG = MatchFragment.class.getSimpleName();

    private RecycleEmptyErrorView recycleViewMatches;
    private TextView emptyMsgMatches;
    private TextView errorMsgMatches;

    private SwipeRefreshLayout refreshLayout;

    public MatchFragment() {
    }

    public static MatchFragment newInstance() {
        MatchFragment fragment = new MatchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create new MatchFragment!");

        final View view = inflater.inflate(R.layout.fragment_match, container, false);

        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAllMatches();
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAllMatches();
    }

    private void loadAllMatches() {
        Log.i(TAG, "loadAllMatches: Load all Matches in BD!");
        List<Match> matches = new MatchRepository().findAll();
        recycleViewMatches.setAdapter(new MatchAdapter(getContext(), matches));
    }

    private void setupRecycleRanking(View view) {
        recycleViewMatches = view.findViewById(R.id.card_view_matches_recycler_view);
        emptyMsgMatches = view.findViewById(R.id.card_view_matches_empty_view);
        errorMsgMatches = view.findViewById(R.id.card_view_matches_error_view);

        recycleViewMatches.setEmptyView(emptyMsgMatches);
        recycleViewMatches.setErrorView(errorMsgMatches);
        recycleViewMatches.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewMatches.setItemAnimator(new DefaultItemAnimator());
        recycleViewMatches.setHasFixedSize(false);

        recycleViewMatches.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewMatches.setItemAnimator(new DefaultItemAnimator());
        recycleViewMatches.setHasFixedSize(true);
    }

}
