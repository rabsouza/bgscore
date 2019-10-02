package br.com.battista.bgscore.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.util.List;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.GameActivity;
import br.com.battista.bgscore.activity.ImportCollectionActivity;
import br.com.battista.bgscore.adpater.GameAdapter;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.constants.ViewConstant;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.OrderByDto;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.repository.contract.DatabaseContract.GameEntry;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.LogUtils;
import br.com.battista.bgscore.util.QueryBuilderUtils;

import static br.com.battista.bgscore.repository.contract.DatabaseContract.BaseEntry.COLUMN_NAME_UPDATED_AT;
import static br.com.battista.bgscore.util.QueryBuilderUtils.Order.ASC;
import static br.com.battista.bgscore.util.QueryBuilderUtils.Order.DESC;

public class GameFragment extends BaseFragment {

    private static final String TAG = GameFragment.class.getSimpleName();

    private RecyclerView recycleViewGames;
    private Spinner spnSortList;
    private Button btnImportCollection;

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
        LogUtils.d(TAG, "onCreateView: Create new GameFragment!");

        final View view = inflater.inflate(R.layout.fragment_game, container, false);

        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() -> loadAllGames());

        ImageButton btnSortList = getActivity().findViewById(R.id.btn_sort_list);
        btnSortList.setVisibility(View.VISIBLE);
        btnSortList.setOnClickListener(viewClicked -> processSortListGames(view, viewClicked));

        ImageButton btnBrokenImg = getActivity().findViewById(R.id.btn_broken_img);
        btnBrokenImg.setVisibility(View.VISIBLE);
        btnBrokenImg.setOnClickListener(viewClicked -> processBrokenImg(btnBrokenImg));

        ImageButton btnImportCollection = getActivity().findViewById(R.id.btn_import_collection);
        btnImportCollection.setVisibility(View.VISIBLE);
        btnImportCollection.setOnClickListener(viewClicked -> processImportCollection(view, viewClicked));

        FloatingActionButton fab = view.findViewById(R.id.fab_new_game);
        fab.setOnClickListener(view1 -> {
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_ADD_GAME);

            Bundle args = new Bundle();
            Intent intent = new Intent(getContext(), GameActivity.class);
            intent.putExtras(args);

            getContext().startActivity(intent);
        });

        setupRecycleGames(view);
        return view;
    }

    private void processImportCollection(View view, View viewClicked) {
        LogUtils.i(TAG, "processImportCollection: Start flow to import collection.");

        final int resourcePopupWindow = R.layout.custom_view_import_collection;
        final PopupWindow popupWindow = AndroidUtils.createPopupWindow(getContext(), resourcePopupWindow);

        btnImportCollection = popupWindow.getContentView().findViewById(R.id.button_card_view_import_collection_bgg);
        btnImportCollection.setOnClickListener(v -> {
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_IMPORT_COLLECTION_BGG);

            startActivity(new Intent(getContext(), ImportCollectionActivity.class));
        });

        popupWindow.showAsDropDown(viewClicked);
    }


    private void processBrokenImg(ImageButton btnBrokenImg) {
        LogUtils.i(TAG, "processBrokenImg: Reload all games images.");

        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_BROKEN_IMG);
        AndroidUtils.toast(getContext(), R.string.toast_reload_all_img_data);

        AndroidUtils.postAction(ActionCacheEnum.RELOAD_ALL_GAME_IMAGES);
        btnBrokenImg.setVisibility(View.GONE);
    }


    private void processSortListGames(View view, View viewClicked) {
        LogUtils.i(TAG, "processSortListGames: Show sort list games!");

        final int resourcePopupWindow = R.layout.custom_view_sort_list_game;
        final PopupWindow popupWindow = AndroidUtils.createPopupWindow(getContext(), resourcePopupWindow);

        spnSortList = popupWindow.getContentView().findViewById(R.id.card_view_sort_list_value);
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
        LogUtils.i(TAG, "processNewOrderByGames: Process new order by!");
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

    @Override
    public void onResume() {
        super.onResume();
        loadAllGames();
    }

    private void loadAllGames() {
        LogUtils.i(TAG, "loadAllMatches: Load all Games in BD!");
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new LoadAllGamesAsyncTask().execute();
    }


    private void setupRecycleGames(View view) {
        recycleViewGames = view.findViewById(R.id.card_view_games_recycler_view);

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
            recycleViewGames.setAdapter(new GameAdapter(getContext(), games));
            refreshLayout.setRefreshing(false);
        }
    }
}
