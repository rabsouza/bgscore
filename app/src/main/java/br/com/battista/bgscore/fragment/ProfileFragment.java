package br.com.battista.bgscore.fragment;


import com.google.common.base.MoreObjects;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.fragment.dialog.ChangeAvatarDialog;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.DateUtils;
import br.com.battista.bgscore.view.RecycleEmptyErrorView;


public class ProfileFragment extends BaseFragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private RecycleEmptyErrorView recycleViewFriends;
    private TextView emptyMsgFriends;
    private TextView errorMsgFriends;

    private Button btnChangeAvatar;
    private Button btnEditProfile;

    private TextView usernameView;
    private TextView mailView;
    private TextView dateCreatedView;
    private ImageView avatarView;

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
        loadUserInfo(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo(getView());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ChangeAvatarDialog.DIALOG_CHANGE_AVATAR_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    loadUserInfo(getView());
                }
        }
    }

    private void loadUserInfo(View view) {
        User user = MainApplication.instance().getUser();

        avatarView = view.findViewById(R.id.card_view_profile_img);
        avatarView.setImageResource(user.getIdResAvatar());

        usernameView = view.findViewById(R.id.card_view_profile_username);
        usernameView.setText(getString(R.string.text_username, user.getUsername()));

        mailView = view.findViewById(R.id.card_view_profile_mail);
        mailView.setText(getString(R.string.text_mail,
                MoreObjects.firstNonNull(user.getMail(), "-")));

        dateCreatedView = view.findViewById(R.id.card_view_profile_date_created);
        Calendar dateCreatedCalendar = Calendar.getInstance();
        dateCreatedCalendar.setTime(user.getCreatedAt());
        dateCreatedView.setText(getString(R.string.text_date_created,
                DateUtils.format(dateCreatedCalendar)));

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
