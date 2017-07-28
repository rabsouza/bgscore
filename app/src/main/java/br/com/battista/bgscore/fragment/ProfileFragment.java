package br.com.battista.bgscore.fragment;


import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.adpater.FriendAdapter;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant.Actions;
import br.com.battista.bgscore.constants.CrashlyticsConstant.ValueActions;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.fragment.dialog.ChangeAvatarDialog;
import br.com.battista.bgscore.fragment.dialog.EditProfileDialog;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.FriendDto;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.DateUtils;

import static android.support.v7.widget.StaggeredGridLayoutManager.*;


public class ProfileFragment extends BaseFragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private RecycleEmptyErrorView recycleViewFriends;
    private TextView emptyMsgFriends;
    private TextView errorMsgFriends;

    private Button btnChangeAvatar;
    private Button btnEditProfile;
    private ImageButton btnAddFriend;
    private EditText usernameFriendView;

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
        Log.d(TAG, "onCreateView: Create new ProfileFragment!");

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
                    final int currentAvatar = data.getIntExtra(BundleConstant.CURRENT_AVATAR,
                            R.drawable.avatar_profile);
                    Log.i(TAG, MessageFormat.format("onActivityResult: Change avatar to res: {0}",
                            currentAvatar));
                    updateAvatarUser(currentAvatar);
                    loadUserInfo(getView());
                    AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                            ValueActions.VALUE_ACTION_CLICK_BUTTON_CHANGE_AVATAR);
                }
                break;
            case EditProfileDialog.DIALOG_EDIT_PROFILE_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    loadUserInfo(getView());
                    AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                            ValueActions.VALUE_ACTION_CLICK_BUTTON_EDIT_PROFILE);
                }
        }
    }

    private void updateAvatarUser(int currentAvatar) {
        final MainApplication instance = MainApplication.instance();
        User user = instance.getUser();
        user.setIdResAvatar(currentAvatar);
        instance.setUser(user);
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
        final MainApplication instance = MainApplication.instance();
        final User user = instance.getUser();

        recycleViewFriends = view.findViewById(R.id.card_view_friends_recycler_view);
        emptyMsgFriends = view.findViewById(R.id.card_view_friends_empty_view);
        errorMsgFriends = view.findViewById(R.id.card_view_friends_error_view);
        recycleViewFriends.setEmptyView(emptyMsgFriends);
        recycleViewFriends.setErrorView(errorMsgFriends);


        recycleViewFriends.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
        recycleViewFriends.setItemAnimator(new DefaultItemAnimator());
        recycleViewFriends.setHasFixedSize(false);
        final List<FriendDto> friends = Lists.newLinkedList(user.getFriends());
        recycleViewFriends.setAdapter(new FriendAdapter(getContext(), friends));

        usernameFriendView = view.findViewById(R.id.card_view_friends_username);

        btnAddFriend = view.findViewById(R.id.card_view_friends_button_add);
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FriendDto friendDto = addNewFriend();
                if (friendDto != null) {
                    friends.add(friendDto);
                    user.addFriend(friendDto);
                    instance.setUser(user);
                    usernameFriendView.setText(null);
                    recycleViewFriends.getAdapter().notifyDataSetChanged();

                    AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                            ValueActions.VALUE_ACTION_CLICK_BUTTON_ADD_FRIEND);
                }
            }
        });
    }

    private FriendDto addNewFriend() {
        Log.i(TAG, "addNewFriend: Add new friend!");

        if (Strings.isNullOrEmpty(usernameFriendView.getText().toString())) {
            String msgErrorUsername = getContext().getString(R.string.msg_username_friend_required);
            AndroidUtils.changeErrorEditText(usernameFriendView, msgErrorUsername, true);
            return null;
        }
        AndroidUtils.changeErrorEditText(usernameFriendView);
        final String username = usernameFriendView.getText().toString().trim();

        Log.d(TAG, MessageFormat.format("Create new friend with username: {0}.", username));
        return new FriendDto().username(username);
    }

    private void setupButtonsProfile(View view) {
        final Fragment currentFragment = this;
        btnChangeAvatar = view.findViewById(R.id.button_profile_change_avatar);
        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final User user = MainApplication.instance().getUser();
                ChangeAvatarDialog.newInstance(user.getIdResAvatar()).showDialog(currentFragment);
            }
        });

        btnEditProfile = view.findViewById(R.id.button_profile_edit_profile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileDialog.newInstance().showDialog(currentFragment);
            }
        });
    }
}
