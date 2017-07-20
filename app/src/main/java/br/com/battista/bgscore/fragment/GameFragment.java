package br.com.battista.bgscore.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.PopupMenuUtils;

public class GameFragment extends BaseFragment {

    private static final String TAG = GameFragment.class.getSimpleName();

    private SwipeRefreshLayout refreshLayout;

    private CardView cardViewGame;
    private ImageView imageMoreActions;

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

                refreshLayout.setRefreshing(false);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_new_match);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                        CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_ADD_GAME);
            }
        });

        setupCardViewGame(view);
        return view;
    }

    private void setupCardViewGame(final View view) {

        cardViewGame = view.findViewById(R.id.card_view_games_info);

        imageMoreActions = view.findViewById(R.id.card_view_games_info_more_actions);
        final PopupMenu popup = new PopupMenu(getContext(), imageMoreActions);
        PopupMenuUtils.showPopupWindow(popup);
        popup.getMenuInflater().inflate(R.menu.menu_actions_game, popup.getMenu());
        cardViewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();
            }
        });
    }

}
