package br.com.battista.bgscore.fragment.match;


import com.google.common.collect.Lists;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.adpater.FriendAdapter;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.fragment.BaseFragment;
import br.com.battista.bgscore.model.Game;
import br.com.battista.bgscore.model.Match;
import br.com.battista.bgscore.model.Player;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.FriendDto;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.repository.PlayerRepository;
import br.com.battista.bgscore.service.CacheManageService;
import br.com.battista.bgscore.util.PopupMenuUtils;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

public class NewMatchFragment extends BaseFragment {
    private static final String TAG = NewMatchFragment.class.getSimpleName();

    private RecycleEmptyErrorView recycleViewPlayers;
    private TextView emptyMsgPlayers;
    private TextView errorMsgPlayers;

    private CardView cardViewGame;
    private ImageView imageMoreActions;

    public NewMatchFragment() {
    }

    public static NewMatchFragment newInstance() {
        NewMatchFragment fragment = new NewMatchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create new NewMatchFragment!");

        final View view = inflater.inflate(R.layout.fragment_new_match, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab_next_edit_match);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Game game = new Game().name("Game" + Math.random());
                game.initEntity();
                new GameRepository().save(game);
                new CacheManageService().onActionCache(ActionCacheEnum.LOAD_DATA_GAME);

                final Player player01 = new Player().name("Player01" + Math.random()).winner(false);
                player01.initEntity();
                final PlayerRepository playerRepository = new PlayerRepository();
                playerRepository.save(player01);
                final Player player02 = new Player().name("Player02" + Math.random()).winner(true);
                player02.initEntity();
                playerRepository.save(player02);

                int result = new Random().nextInt(200) + 100;
                final Match match = new Match()
                        .alias("Alias" + Math.random())
                        .duration(Long.valueOf(result))
                        .finish(true).game(game)
                        .players(Lists.newArrayList(player01, player02));
                Log.i(TAG, "***********************************************************");
                Log.i(TAG, "MATCH SAVE:" + match);
                Log.i(TAG, "***********************************************************");
                new MatchRepository().save(match);

                final List<Match> matches = new MatchRepository().findAll();
                Log.i(TAG, "***********************************************************");
                for (Match matchLoad : matches) {
                    Log.i(TAG, "MATCH LOAD:" + matchLoad);
                }
                Log.i(TAG, "***********************************************************");
                new CacheManageService().onActionCache(ActionCacheEnum.LOAD_DATA_MATCHES);
            }
        });

        setupRecycleViewPlayers(view);
        setupCardViewGame(view);

        return view;
    }

    private void setupCardViewGame(final View view) {

        cardViewGame = view.findViewById(R.id.card_view_game_info);

        imageMoreActions = view.findViewById(R.id.card_view_game_info_more_actions);
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

    private void setupRecycleViewPlayers(View view) {
        final MainApplication instance = MainApplication.instance();
        final User user = instance.getUser();

        recycleViewPlayers = view.findViewById(R.id.card_view_players_recycler_view);
        emptyMsgPlayers = view.findViewById(R.id.card_view_players_empty_view);
        errorMsgPlayers = view.findViewById(R.id.card_view_players_error_view);
        recycleViewPlayers.setEmptyView(emptyMsgPlayers);
        recycleViewPlayers.setErrorView(errorMsgPlayers);

        recycleViewPlayers.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
        recycleViewPlayers.setItemAnimator(new DefaultItemAnimator());
        recycleViewPlayers.setHasFixedSize(false);
        final List<FriendDto> players = Lists.newLinkedList(user.getFriends());
        recycleViewPlayers.setAdapter(new FriendAdapter(getContext(), players, false, true));

    }

}
