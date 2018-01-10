package br.com.battista.bgscore.fragment;

import static br.com.battista.bgscore.repository.contract.DatabaseContract.BaseEntry.COLUMN_NAME_UPDATED_AT;
import static br.com.battista.bgscore.util.QueryBuilderUtils.Order.ASC;
import static br.com.battista.bgscore.util.QueryBuilderUtils.Order.DESC;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.GameActivity;
import br.com.battista.bgscore.adpater.GameAdapter;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.constants.ViewConstant;
import br.com.battista.bgscore.custom.ScoreboardView;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.OrderByDto;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.repository.contract.DatabaseContract.GameEntry;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.QueryBuilderUtils;

public class GameFragment extends BaseFragment {

    private static final String TAG = GameFragment.class.getSimpleName();

    private RecyclerView recycleViewGames;
    private TextView emptyMsgGames;
    private TextView errorMsgGames;
    private ImageView imgHelpGame;
    private Spinner spnSortList;

    private ScoreboardView scoreMyGame;
    private ScoreboardView scoreWantGame;
    private ScoreboardView scoreFavorite;

    private SwipeRefreshLayout refreshLayout;
    private int lastSelectedItemPosition = 0;

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
            }
        });

        ImageButton btnSortList = getActivity().findViewById(R.id.btn_sort_list);
        btnSortList.setVisibility(View.VISIBLE);
        btnSortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewClicked) {
                processSortListGames(view, viewClicked);
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

        setupRecycleGames(view);
        setupHelpGame(view);
        setupDataGame(view);

        return view;
    }

    private void setupDataGame(View view) {
        Log.i(TAG, "setupDataGame: Setup the data to games.");

        scoreMyGame = view.findViewById(R.id.card_view_games_score_my_game);
        scoreFavorite = view.findViewById(R.id.card_view_games_score_favorite);
        scoreWantGame = view.findViewById(R.id.card_view_games_score_want_game);
    }

    private void processSortListGames(View view, View viewClicked) {
        Log.i(TAG, "processSortListGames: Show sort list games!");

        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_view_sort_list_game, null);
        final PopupWindow popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.animationPopup);

        spnSortList = layout.findViewById(R.id.card_view_sort_list_value);
        spnSortList.setSelected(true);
        final User user = MainApplication.instance().getUser();
        if (user.containsOrderBy(ViewConstant.GAME_VIEW) && lastSelectedItemPosition != 0) {
            spnSortList.setSelection(user.getOrderBy(ViewConstant.GAME_VIEW).getPosition());
        }
        spnSortList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final int selectedItemPosition = spnSortList.getSelectedItemPosition();
                if (lastSelectedItemPosition != selectedItemPosition) {
                    String selectedItem = (String) spnSortList.getSelectedItem();
                    processNewOrderByGames(selectedItem, selectedItemPosition);
                    popupWindow.dismiss();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        popupWindow.showAsDropDown(viewClicked);
    }

    private void processNewOrderByGames(@NonNull String selectedItem, int selectedItemPosition) {
        Log.i(TAG, "processNewOrderByGames: Process new order by!");
        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_SORT_LIST_GAME);

        QueryBuilderUtils builder = QueryBuilderUtils.newInstance();
        String[] optionsSortList = getResources().getStringArray(R.array.sort_list_games);
        OrderByDto orderBy = new OrderByDto();
        orderBy.value(selectedItem);
        orderBy.position(selectedItemPosition);
        if (selectedItem.equals(optionsSortList[1])) {
            builder.addPropOrderBy(GameEntry.COLUMN_NAME_NAME, ASC);
            builder.addPropOrderBy(COLUMN_NAME_UPDATED_AT, DESC);
        } else {
            if (selectedItem.equals(optionsSortList[3])) {
                builder.addPropOrderBy("CAST( " + GameEntry.COLUMN_NAME_RATING + " AS INTEGER)", DESC);
            } else if (selectedItem.equals(optionsSortList[4])) {
                builder.addPropOrderBy(GameEntry.COLUMN_NAME_FAVORITE, DESC);
                builder.addPropOrderBy(GameEntry.COLUMN_NAME_MY_GAME, DESC);
            } else if (selectedItem.equals(optionsSortList[5])) {
                builder.addPropOrderBy(GameEntry.COLUMN_NAME_MY_GAME, DESC);
            } else if (selectedItem.equals(optionsSortList[6])) {
                builder.addPropOrderBy(GameEntry.COLUMN_NAME_WANT_GAME, DESC);
                builder.addPropOrderBy(GameEntry.COLUMN_NAME_FAVORITE, DESC);
                builder.addPropOrderBy(GameEntry.COLUMN_NAME_MY_GAME, DESC);
            } else if (selectedItem.equals(optionsSortList[7])) {
                builder.addPropOrderBy("CAST( " + GameEntry.COLUMN_NAME_MAX_PLAYERS + " AS INTEGER)", DESC);
                builder.addPropOrderBy("CAST( " + GameEntry.COLUMN_NAME_MIN_PLAYERS + " AS INTEGER)", DESC);
            } else if (selectedItem.equals(optionsSortList[8])) {
                builder.addPropOrderBy("CAST( " + GameEntry.COLUMN_NAME_MAX_PLAY_TIME + " AS INTEGER)", DESC);
                builder.addPropOrderBy("CAST( " + GameEntry.COLUMN_NAME_MIN_PLAY_TIME + " AS INTEGER)", DESC);
            } else if (selectedItem.equals(optionsSortList[9])) {
                builder.addPropOrderBy("CAST( " + GameEntry.COLUMN_NAME_MAX_PLAY_TIME + " AS INTEGER)", ASC);
                builder.addPropOrderBy("CAST( " + GameEntry.COLUMN_NAME_MIN_PLAY_TIME + " AS INTEGER)", ASC);
            }
            builder.addPropOrderBy(COLUMN_NAME_UPDATED_AT, DESC);
            builder.addPropOrderBy(GameEntry.COLUMN_NAME_NAME, ASC);
        }

        MainApplication instance = MainApplication.instance();
        User user = instance.getUser();
        orderBy.query(builder.buildOrderBy());
        user.putOrderBy(ViewConstant.GAME_VIEW, orderBy);
        instance.setUser(user);

        loadAllGames();
    }

    private void setupHelpGame(View view) {
        imgHelpGame = view.findViewById(R.id.card_view_games_help);
        imgHelpGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                        CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_LEGEND_GAME);

                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.dialog_help_game, null);

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.title_help)
                        .setView(customView)
                        .setPositiveButton(R.string.btn_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.animationAlert;
                alertDialog.show();
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
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new LoadAllGamesAsyncTask().execute();
    }

    private void updateScoresGames(List<Game> games) {
        Log.i(TAG, "updateScoresGames: Udapte the scores to games!");

        int countMyGame = 0;
        int countFavorite = 0;
        int countWantGame = 0;
        for (Game game : games) {
            if (game.isMyGame()) {
                countMyGame++;
            }
            if (game.isFavorite()) {
                countFavorite++;
            }
            if (game.isWantGame()) {
                countWantGame++;
            }
        }

        DecimalFormat decimalFormatScore = new DecimalFormat("#00");
        scoreMyGame.setScoreText(decimalFormatScore.format(countMyGame));
        scoreFavorite.setScoreText(decimalFormatScore.format(countFavorite));
        scoreWantGame.setScoreText(decimalFormatScore.format(countWantGame));

    }

    private void setupRecycleGames(View view) {
        recycleViewGames = view.findViewById(R.id.card_view_games_recycler_view);
        emptyMsgGames = view.findViewById(R.id.card_view_games_empty_view);
        errorMsgGames = view.findViewById(R.id.card_view_games_error_view);

        recycleViewGames.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewGames.setItemAnimator(new DefaultItemAnimator());
        recycleViewGames.setHasFixedSize(true);
        recycleViewGames.setItemViewCacheSize(50);
        recycleViewGames.setDrawingCacheEnabled(true);
        recycleViewGames.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    private class LoadAllGamesAsyncTask extends AsyncTask<Void, Void, Void> {
        List<Game> games;

        @Override
        protected Void doInBackground(Void... voids) {
            User user = MainApplication.instance().getUser();
            if (user.containsOrderBy(ViewConstant.GAME_VIEW)) {
                final OrderByDto orderBy = user.getOrderBy(ViewConstant.GAME_VIEW);
                lastSelectedItemPosition = orderBy.getPosition();
                games = new GameRepository().findAll(orderBy.getQuery());
            } else {
                games = new GameRepository().findAll();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            updateScoresGames(games);
            recycleViewGames.setAdapter(new GameAdapter(getContext(), games));
            if (games.isEmpty()) {
                emptyMsgGames.setVisibility(View.VISIBLE);
            } else {
                emptyMsgGames.setVisibility(View.GONE);
            }
            refreshLayout.setRefreshing(false);
        }
    }
}
