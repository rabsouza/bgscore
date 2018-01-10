package br.com.battista.bgscore.fragment;


import static br.com.battista.bgscore.repository.contract.DatabaseContract.BaseEntry.COLUMN_NAME_CREATED_AT;
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

import java.util.List;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.activity.MatchActivity;
import br.com.battista.bgscore.adpater.MatchAdapter;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.constants.ViewConstant;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.OrderByDto;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.repository.contract.DatabaseContract.MatchEntry;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.QueryBuilderUtils;


public class MatchFragment extends BaseFragment {

    private static final String TAG = MatchFragment.class.getSimpleName();

    private RecyclerView recycleViewMatches;
    private TextView emptyMsgMatches;
    private TextView errorMsgMatches;
    private ImageView imgHelpMatch;
    private Spinner spnSortList;

    private SwipeRefreshLayout refreshLayout;
    private int lastSelectedItemPosition = 0;

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

        setupRecycleMatches(view);
        setupHelpMath(view);

        return view;
    }

    private void processSortListGames(View view, View viewClicked) {
        Log.i(TAG, "processSortListGames: Show sort list games!");

        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_view_sort_list_match, null);
        final PopupWindow popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.animationPopup);

        spnSortList = layout.findViewById(R.id.card_view_sort_list_value);
        spnSortList.setSelected(true);
        final User user = MainApplication.instance().getUser();
        if (user.containsOrderBy(ViewConstant.MATCH_VIEW) && lastSelectedItemPosition != 0) {
            spnSortList.setSelection(user.getOrderBy(ViewConstant.MATCH_VIEW).getPosition());
        }
        spnSortList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final int selectedItemPosition = spnSortList.getSelectedItemPosition();
                if (lastSelectedItemPosition != selectedItemPosition) {
                    String selectedItem = (String) spnSortList.getSelectedItem();
                    processNewOrderByMatches(selectedItem, selectedItemPosition);
                    popupWindow.dismiss();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        popupWindow.showAsDropDown(viewClicked);
    }

    private void processNewOrderByMatches(@NonNull String selectedItem, int selectedItemPosition) {
        Log.i(TAG, "processNewOrderByMatches: Process new order by!");
        AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_SORT_LIST_MATCH);

        QueryBuilderUtils builder = QueryBuilderUtils.newInstance();
        String[] optionsSortList = getResources().getStringArray(R.array.sort_list_matches);
        OrderByDto orderBy = new OrderByDto();
        orderBy.value(selectedItem);
        orderBy.position(selectedItemPosition);
        if (selectedItem.equals(optionsSortList[1])) {
            builder.addPropOrderBy(MatchEntry.COLUMN_NAME_ALIAS, ASC);
            builder.addPropOrderBy(COLUMN_NAME_CREATED_AT, DESC);
        } else {
            if (selectedItem.equals(optionsSortList[3])) {
                builder.addPropOrderBy(MatchEntry.COLUMN_NAME_FINISHED, DESC);
            } else if (selectedItem.equals(optionsSortList[4])) {
                builder.addPropOrderBy(MatchEntry.COLUMN_NAME_FINISHED, ASC);
            } else if (selectedItem.equals(optionsSortList[5])) {
                builder.addPropOrderBy(MatchEntry.COLUMN_NAME_DURATION, DESC);
                builder.addPropOrderBy(MatchEntry.COLUMN_NAME_FINISHED, DESC);
            } else if (selectedItem.equals(optionsSortList[6])) {
                builder.addPropOrderBy(MatchEntry.COLUMN_NAME_DURATION, ASC);
                builder.addPropOrderBy(MatchEntry.COLUMN_NAME_FINISHED, DESC);
            }
            builder.addPropOrderBy(COLUMN_NAME_CREATED_AT, DESC);
            builder.addPropOrderBy(MatchEntry.COLUMN_NAME_ALIAS, ASC);
        }

        MainApplication instance = MainApplication.instance();
        User user = instance.getUser();

        orderBy.query(builder.buildOrderBy());
        user.putOrderBy(ViewConstant.MATCH_VIEW, orderBy);
        instance.setUser(user);

        loadAllMatches();
    }

    private void setupHelpMath(View view) {
        imgHelpMatch = view.findViewById(R.id.card_view_matches_help);
        imgHelpMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                        CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_LEGEND_MATCH);

                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.dialog_help_match, null);

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
        loadAllMatches();
    }

    private void loadAllMatches() {
        Log.i(TAG, "loadAllMatches: Load all Matches in BD!");
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        new LoadAllMatchesAsyncTask().execute();
    }

    private void setupRecycleMatches(View view) {
        recycleViewMatches = view.findViewById(R.id.card_view_matches_recycler_view);
        emptyMsgMatches = view.findViewById(R.id.card_view_matches_empty_view);
        errorMsgMatches = view.findViewById(R.id.card_view_matches_error_view);

        recycleViewMatches.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewMatches.setItemAnimator(new DefaultItemAnimator());
        recycleViewMatches.setHasFixedSize(true);
        recycleViewMatches.setItemViewCacheSize(50);
        recycleViewMatches.setDrawingCacheEnabled(true);
        recycleViewMatches.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    private class LoadAllMatchesAsyncTask extends AsyncTask<Void, Void, Void> {
        List<Match> matches;

        @Override
        protected Void doInBackground(Void... voids) {
            User user = MainApplication.instance().getUser();
            if (user.containsOrderBy(ViewConstant.MATCH_VIEW)) {
                final OrderByDto orderBy = user.getOrderBy(ViewConstant.MATCH_VIEW);
                lastSelectedItemPosition = orderBy.getPosition();
                matches = new MatchRepository().findAll(orderBy.getQuery());
            } else {
                matches = new MatchRepository().findAll();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            recycleViewMatches.setAdapter(new MatchAdapter(getContext(), matches));
            if (matches.isEmpty()) {
                emptyMsgMatches.setVisibility(View.VISIBLE);
            } else {
                emptyMsgMatches.setVisibility(View.GONE);
            }
            refreshLayout.setRefreshing(false);
        }
    }

}
