package br.com.battista.bgscore.fragment.game;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.fragment.BaseFragment;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.service.CacheManageService;
import br.com.battista.bgscore.util.PopupMenuUtils;

public class NewGameFragment extends BaseFragment {
    private static final String TAG = NewGameFragment.class.getSimpleName();

    private CardView cardViewGame;
    private ImageView imageMoreActions;

    public NewGameFragment() {
    }

    public static NewGameFragment newInstance() {
        NewGameFragment fragment = new NewGameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create new NewGameFragment!");

        final View view = inflater.inflate(R.layout.fragment_new_game, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab_next_done_game);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Game game = new Game().name("Game" + Math.random());
                game.initEntity();
                new GameRepository().save(game);
                new CacheManageService().onActionCache(ActionCacheEnum.LOAD_DATA_GAME);
            }
        });

        setupCardViewGame(view);
        return view;
    }

    private void setupCardViewGame(final View view) {

        cardViewGame = view.findViewById(R.id.card_view_search_game_info);

        imageMoreActions = view.findViewById(R.id.card_view_search_game_info_more_actions);
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
