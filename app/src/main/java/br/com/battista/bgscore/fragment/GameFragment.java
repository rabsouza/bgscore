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

import java.util.ArrayList;
import java.util.List;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.GameActivity;
import br.com.battista.bgscore.activity.ImportCollectionActivity;
import br.com.battista.bgscore.adpater.EndlessRecyclerViewScrollListener;
import br.com.battista.bgscore.adpater.GameAdapter;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.constants.ViewConstant;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.FilterByDto;
import br.com.battista.bgscore.model.dto.OrderByDto;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.repository.contract.DatabaseContract.GameEntry;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.LogUtils;
import br.com.battista.bgscore.util.QueryBuilderUtils;

import static br.com.battista.bgscore.constants.EntityConstant.SIZE_PAGE_DATA;
import static br.com.battista.bgscore.repository.contract.DatabaseContract.BaseEntry.COLUMN_NAME_UPDATED_AT;
import static br.com.battista.bgscore.util.QueryBuilderUtils.Order.ASC;
import static br.com.battista.bgscore.util.QueryBuilderUtils.Order.DESC;

public class GameFragment extends BaseFragment {

    private static final String TAG = GameFragment.class.getSimpleName();
    private static final int PAGE_INITIAL = 0;
    private RecyclerView recycleViewGames;
    private List<Game> games = new ArrayList<>();
    private GameAdapter gameAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Button btnImportCollection;
    private SwipeRefreshLayout refreshLayout;
    private int lastSelectedItemSortedPosition = 0;
    private int lastSelectedItemFilteredPosition = 0;

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

        ImageButton btnFilterList = getActivity().findViewById(R.id.btn_filter_list);
        btnFilterList.setVisibility(View.VISIBLE);
        btnFilterList.setOnClickListener(viewClicked -> processFilterListGames(view, viewClicked));

        ImageButton btnBrokenImg = getActivity().findViewById(R.id.btn_broken_img);
        btnBrokenImg.setVisibility(View.GONE);
        btnBrokenImg.setOnClickListener(viewClicked -> processBrokenImg(btnBrokenImg));

        ImageButton btnImportCollection = getActivity().findViewById(R.id.btn_import_collection);
        btnImportCollection.setVisibility(View.GONE);
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


    private void processFilterListGames(View view, View viewClicked) {
        LogUtils.i(TAG, "processSortListGames: Show filter list games!");

        final int resourcePopupWindow = R.layout.custom_view_filter_list_game;
        final PopupWindow popupWindow = AndroidUtils.createPopupWindow(getContext(), resourcePopupWindow);

        Spinner spnFilterList = popupWindow.getContentView().findViewById(R.id.card_view_filter_list_value);
        spnFilterList.setSelected(true);
        final User user = MainApplication.instance().getUser();
        if (user.containsFilterBy(ViewConstant.GAME_VIEW) && lastSelectedItemFilteredPosition != 0) {
            spnFilterList.setSelection(user.getFilterBy(ViewConstant.GAME_VIEW).getPosition());
        }
        spnFilterList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final int selectedItemPosition = spnFilterList.getSelectedItemPosition();
                if (lastSelectedItemFilteredPosition != selectedItemPosition) {
                    String selectedItem = (String) spnFilterList.getSelectedItem();
                    processFilteredListByGames(selectedItem, selectedItemPosition);
                    popupWindow.dismiss();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        popupWindow.showAsDropDown(viewClicked);
    }

    private void processSortListGames(View view, View viewClicked) {
        LogUtils.i(TAG, "processSortListGames: Show sort list games!");

        final int resourcePopupWindow = R.layout.custom_view_sort_list_game;
        final PopupWindow popupWindow = AndroidUtils.createPopupWindow(getContext(), resourcePopupWindow);

        Spinner spnSortList = popupWindow.getContentView().findViewById(R.id.card_view_sort_list_value);
        spnSortList.setSelected(true);
        final User user = MainApplication.instance().getUser();
        if (user.containsOrderBy(ViewConstant.GAME_VIEW) && lastSelectedItemSortedPosition != 0) {
            spnSortList.setSelection(user.getOrderBy(ViewConstant.GAME_VIEW).getPosition());
        }
        spnSortList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final int selectedItemPosition = spnSortList.getSelectedItemPosition();
                if (lastSelectedItemSortedPosition != selectedItemPosition) {
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

    private void processFilteredListByGames(@NonNull String selectedItem, int selectedItemPosition) {
        LogUtils.i(TAG, "processNewOrderByGames: Process new order by!");
        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_FILTER_LIST_GAME);
        MainApplication instance = MainApplication.instance();
        User user = instance.getUser();

        String[] optionsFilterList = getResources().getStringArray(R.array.filter_list_games);

        FilterByDto filterByDto = new FilterByDto();
        filterByDto.position(selectedItemPosition);
        if (selectedItemPosition > 0 && selectedItemPosition < optionsFilterList.length) {
            if (selectedItem.equals(optionsFilterList[1])) {
                filterByDto.value("1");
                filterByDto.query(GameEntry.COLUMN_NAME_MY_GAME);
            } else if (selectedItem.equals(optionsFilterList[2])) {
                filterByDto.value("1");
                filterByDto.query(GameEntry.COLUMN_NAME_FAVORITE);
            } else if (selectedItem.equals(optionsFilterList[3])) {
                filterByDto.value("1");
                filterByDto.query(GameEntry.COLUMN_NAME_WANT_GAME);
            }
            user.putFilterBy(ViewConstant.GAME_VIEW, filterByDto);
        } else {
            lastSelectedItemFilteredPosition = 0;
            user.clearFilterBy();
        }

        instance.setUser(user);
        loadAllGames();
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

        // Reajustar o scrool infinito
        games.clear();
        scrollListener.resetState();

        new LoadAllGamesAsyncTask(PAGE_INITIAL).execute();
    }


    private void setupRecycleGames(View view) {
        recycleViewGames = view.findViewById(R.id.card_view_games_recycler_view);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycleViewGames.setLayoutManager(linearLayoutManager);
        recycleViewGames.setItemAnimator(new DefaultItemAnimator());
        recycleViewGames.setHasFixedSize(false);
        recycleViewGames.setItemViewCacheSize(SIZE_PAGE_DATA);
        recycleViewGames.setDrawingCacheEnabled(true);
        recycleViewGames.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                new LoadAllGamesAsyncTask(page * SIZE_PAGE_DATA).execute();
            }
        };
        recycleViewGames.addOnScrollListener(scrollListener);
        gameAdapter = new GameAdapter(getContext(), games);
        recycleViewGames.setAdapter(gameAdapter);
    }

    private class LoadAllGamesAsyncTask extends AsyncTask<Void, Void, Void> {

        private int offset;

        LoadAllGamesAsyncTask(int offset) {
            this.offset = offset;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            User user = MainApplication.instance().getUser();

            if (user.containsFilterBy(ViewConstant.GAME_VIEW)) {
                final FilterByDto filterBy = user.getFilterBy(ViewConstant.GAME_VIEW);
                lastSelectedItemFilteredPosition = filterBy.getPosition();

                OrderByDto orderBy = user.getOrderBy(ViewConstant.GAME_VIEW);
                if (orderBy == null) {
                    QueryBuilderUtils builder = QueryBuilderUtils.newInstance();
                    builder.addPropOrderBy(COLUMN_NAME_UPDATED_AT, DESC);
                    builder.addPropOrderBy(GameEntry.COLUMN_NAME_NAME, ASC);

                    orderBy = new OrderByDto();
                    orderBy.setPosition(0);
                    orderBy.setQuery(builder.buildOrderBy());
                }
                lastSelectedItemSortedPosition = orderBy.getPosition();

                if (filterBy.getQuery() == null || filterBy.getQuery().trim().isEmpty()) {
                    games.addAll(new GameRepository().findAll(orderBy.getQuery(), offset, SIZE_PAGE_DATA));
                } else {
                    games.addAll(new GameRepository().findAll(filterBy.getQuery(), filterBy.getValue(),
                            orderBy.getQuery(), offset, SIZE_PAGE_DATA));
                }

            } else if (user.containsOrderBy(ViewConstant.GAME_VIEW)) {
                final OrderByDto orderBy = user.getOrderBy(ViewConstant.GAME_VIEW);
                lastSelectedItemSortedPosition = orderBy.getPosition();
                games.addAll(new GameRepository().findAll(orderBy.getQuery(), offset, SIZE_PAGE_DATA));

            } else {
                games.addAll(new GameRepository().findAll(offset, SIZE_PAGE_DATA));

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            gameAdapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }
}
