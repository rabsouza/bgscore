package br.com.battista.bgscore.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.fragment.dialog.ChangeAvatarDialog;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.view.RecycleEmptyErrorView;


public class ProfileFragment extends BaseFragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private RecycleEmptyErrorView recycleViewFriends;
    private TextView emptyMsgFriends;
    private TextView errorMsgFriends;

    private Button btnChangeAvatar;
    private Button btnEditProfile;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create new fragment Profile!");

        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        setupRecycleViewFriends(view);
        setupButtonsProfile(view);

        return view;
    }

    private void setupRecycleViewFriends(View view) {
        recycleViewFriends = view.findViewById(R.id.card_view_friends_recycler_view);
        emptyMsgFriends = view.findViewById(R.id.card_view_friends_empty_view);
        errorMsgFriends = view.findViewById(R.id.card_view_friends_error_view);
        recycleViewFriends.setEmptyView(emptyMsgFriends);
        recycleViewFriends.setErrorView(errorMsgFriends);

        // TODO Remover ao add um adapter
        emptyMsgFriends.setVisibility(View.VISIBLE);
    }

    private void setupButtonsProfile(View view) {
        final Fragment currentFragment = this;
        btnChangeAvatar = view.findViewById(R.id.button_profile_change_avatar);
        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAvatarDialog.newInstance().showDialog(currentFragment);
            }
        });

        btnEditProfile = view.findViewById(R.id.button_profile_edit_profile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUtils.toast(getActivity(), "Não implementado!");
            }
        });
    }
}
