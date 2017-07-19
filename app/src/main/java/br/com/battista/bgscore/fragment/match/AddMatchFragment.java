package br.com.battista.bgscore.fragment.match;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.adpater.FriendAdapter;
import br.com.battista.bgscore.fragment.BaseFragment;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.FriendDto;
import br.com.battista.bgscore.view.RecycleEmptyErrorView;

public class AddMatchFragment extends BaseFragment {
    private static final String TAG = AddMatchFragment.class.getSimpleName();

    private RecycleEmptyErrorView recycleViewFriends;
    private TextView emptyMsgFriends;
    private TextView errorMsgFriends;

    public AddMatchFragment() {
    }

    public static AddMatchFragment newInstance() {
        AddMatchFragment fragment = new AddMatchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create new fragment Profile!");

        final View view = inflater.inflate(R.layout.fragment_add_match, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_next_edit_match);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        setupRecycleViewFriends(view);

        return view;
    }

    private void setupRecycleViewFriends(View view) {
        final MainApplication instance = MainApplication.instance();
        final User user = instance.getUser();

        recycleViewFriends = view.findViewById(R.id.card_view_friends_recycler_view);
        emptyMsgFriends = view.findViewById(R.id.card_view_friends_empty_view);
        errorMsgFriends = view.findViewById(R.id.card_view_friends_error_view);
        recycleViewFriends.setEmptyView(emptyMsgFriends);
        recycleViewFriends.setErrorView(errorMsgFriends);

        recycleViewFriends.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycleViewFriends.setItemAnimator(new DefaultItemAnimator());
        recycleViewFriends.setHasFixedSize(false);
        final ArrayList<FriendDto> friends = new ArrayList<>(user.getFriends());
        recycleViewFriends.setAdapter(new FriendAdapter(getContext(), friends, false, true));

    }

}
