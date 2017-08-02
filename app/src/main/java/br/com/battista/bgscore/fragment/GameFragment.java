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

import java.util.List;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.GameActivity;
import br.com.battista.bgscore.adpater.GameAdapter;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.util.AnswersUtils;

public class GameFragment extends BaseFragment {

    private static final String TAG = GameFragment.class.getSimpleName();

    private RecycleEmptyErrorView recycleViewGames;
    private TextView emptyMsgGames;
    private TextView errorMsgGames;
    private ImageView imgHelpGame;

    private SwipeRefreshLayout refreshLayout;

    public GameFragment() {
    }

    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create new GameFragment!");

        final View view = inflater.inflate(R.layout.fragment_game, container, false);

        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAllGames();
                refreshLayout.setRefreshing(false);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_new_game);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                        CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_ADD_GAME);

                Bundle args = new Bundle();
                Intent intent = new Intent(getContext(), GameActivity.class);
                intent.putExtras(args);

                getContext().startActivity(intent);
            }
        });

        setupRecycleRanking(view);
        setupHelpGame(view);

        return view;
    }

    private void setupHelpGame(View view) {
        imgHelpGame = view.findViewById(R.id.card_view_games_help);
        imgHelpGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.custom_help_game, null);

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
        loadAllGames();
    }

    private void loadAllGames() {
        Log.i(TAG, "loadAllMatches: Load all Games in BD!");
        List<Game> games = new GameRepository().findAll();
        recycleViewGames.setAdapter(new GameAdapter(getContext(), games));

    }

    private void setupRecycleRanking(View view) {
        recycleViewGames = view.findViewById(R.id.card_view_games_recycler_view);
        emptyMsgGames = view.findViewById(R.id.card_view_games_empty_view);
        errorMsgGames = view.findViewById(R.id.card_view_games_error_view);
        recycleViewGames.setEmptyView(emptyMsgGames);
        recycleViewGames.setErrorView(errorMsgGames);

        recycleViewGames.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewGames.setItemAnimator(new DefaultItemAnimator());
        recycleViewGames.setHasFixedSize(true);
    }

}
