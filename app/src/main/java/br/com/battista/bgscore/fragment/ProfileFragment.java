package br.com.battista.bgscore.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.adpater.FriendAdapter;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant.Actions;
import br.com.battista.bgscore.constants.CrashlyticsConstant.ValueActions;
import br.com.battista.bgscore.custom.ProgressApp;
import br.com.battista.bgscore.custom.RecycleEmptyErrorView;
import br.com.battista.bgscore.fragment.dialog.ChangeAvatarDialog;
import br.com.battista.bgscore.fragment.dialog.EditProfileDialog;
import br.com.battista.bgscore.fragment.dialog.ExportImportDataDialog;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.FriendDto;
import br.com.battista.bgscore.model.enuns.ActionCacheEnum;
import br.com.battista.bgscore.model.enuns.AvatarEnum;
import br.com.battista.bgscore.service.Inject;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.DateUtils;
import br.com.battista.bgscore.util.LogUtils;

import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_CHANGE_AVATAR_FRAGMENT;
import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_EDIT_PROFILE_FRAGMENT;
import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_EXPORT_IMPORT_DATA_FRAGMENT;


public class ProfileFragment extends BaseFragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private RecycleEmptyErrorView recycleViewFriends;
    private TextView emptyMsgFriends;
    private TextView errorMsgFriends;

    private Button btnExportImportData;
    private Button btnEditProfile;
    private ImageButton btnAddFriend;
    private EditText txtUsernameFriend;

    private TextView txtUsername;
    private TextView txtMail;
    private TextView txtDateCreated;
    private ImageView txtAvatar;

    private SwipeRefreshLayout refreshLayout;

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
        LogUtils.d(TAG, "onCreateView: Create new ProfileFragment!");

        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() -> {
            loadUserInfo(getView());
            loadDataFriends(getView());
            refreshLayout.setRefreshing(false);
        });

        setupRecycleViewFriends(view);
        setupButtonsProfile(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo(getView());
        loadDataFriends(getView());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case DIALOG_CHANGE_AVATAR_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    final int currentAvatar = data.getIntExtra(BundleConstant.CURRENT_AVATAR,
                            R.drawable.avatar_profile);
                    LogUtils.i(TAG, MessageFormat.format("onActivityResult: Change avatar to res: {0}",
                            currentAvatar));

                    updateAvatarUser(currentAvatar);
                    loadUserInfo(getView());
                    AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                            ValueActions.VALUE_ACTION_CLICK_BUTTON_CHANGE_AVATAR);
                }
                break;
            case DIALOG_EDIT_PROFILE_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    LogUtils.i(TAG, "onActivityResult: Edit my user!");

                    loadUserInfo(getView());
                    loadDataFriends(getView());
                    AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                            ValueActions.VALUE_ACTION_CLICK_BUTTON_EDIT_PROFILE);
                }
                break;
            case DIALOG_EXPORT_IMPORT_DATA_FRAGMENT:
                if (resultCode == Activity.RESULT_OK &&
                        BundleConstant.Action.IMPORT == data.getIntExtra(BundleConstant.ACTION, 0)) {
                    LogUtils.i(TAG, "onActivityResult: Import data from backup!");

                    new ProfileProgressApp(data).execute();

                }
                AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                        ValueActions.VALUE_ACTION_CLICK_BUTTON_EXPORT_IMPORT_DATA);
                break;
        }
    }

    private void updateAvatarUser(int currentAvatar) {
        final MainApplication instance = MainApplication.instance();
        User user = instance.getUser();
        user.avatar(AvatarEnum.get(currentAvatar));
        instance.setUser(user);
    }

    private void loadUserInfo(View view) {
        LogUtils.i(TAG, "loadUserInfo: Load all info user to cache and update user statistics!");
        User user = MainApplication.instance().getUser();
        final Fragment currentFragment = this;

        txtAvatar = view.findViewById(R.id.card_view_profile_img);
        txtAvatar.setImageResource(user.getAvatar().getIdResDrawable());
        txtAvatar.setOnClickListener(view1 -> {
            AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                    ValueActions.VALUE_ACTION_CLICK_BUTTON_CHANGE_AVATAR);

            final User user1 = MainApplication.instance().getUser();
            ChangeAvatarDialog.newInstance(user1.getAvatar().getIdResDrawable())
                    .showDialog(currentFragment);
        });

        txtUsername = view.findViewById(R.id.card_view_profile_username);
        txtUsername.setText(getString(R.string.text_username, user.getUsername()));

        txtMail = view.findViewById(R.id.card_view_profile_mail);
        txtMail.setText(getString(R.string.text_mail,
                MoreObjects.firstNonNull(user.getMail(), "-")));

        txtDateCreated = view.findViewById(R.id.card_view_profile_date_created);
        Calendar dateCreatedCalendar = Calendar.getInstance();
        dateCreatedCalendar.setTime(user.getCreatedAt());
        txtDateCreated.setText(getString(R.string.text_date_created,
                DateUtils.format(dateCreatedCalendar)));

    }

    private void setupRecycleViewFriends(View view) {
        recycleViewFriends = view.findViewById(R.id.card_view_friends_recycler_view);
        emptyMsgFriends = view.findViewById(R.id.card_view_friends_empty_view);
        errorMsgFriends = view.findViewById(R.id.card_view_friends_error_view);
        recycleViewFriends.setEmptyView(emptyMsgFriends);
        recycleViewFriends.setErrorView(errorMsgFriends);

        recycleViewFriends.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycleViewFriends.setItemAnimator(new DefaultItemAnimator());
        recycleViewFriends.setHasFixedSize(false);
        recycleViewFriends.setItemViewCacheSize(20);
        recycleViewFriends.setDrawingCacheEnabled(true);
        recycleViewFriends.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    private void loadDataFriends(View view) {
        final MainApplication instance = MainApplication.instance();
        final User user = instance.getUser();

        final List<FriendDto> friends = Lists.newLinkedList(user.getFriends());
        Collections.sort(friends, new Ordering<FriendDto>() {
            @Override
            public int compare(FriendDto left, FriendDto right) {
                return left.compareTo(right);
            }
        });
        recycleViewFriends.setAdapter(new FriendAdapter(getContext(), friends));

        txtUsernameFriend = view.findViewById(R.id.card_view_friends_username);
        txtUsernameFriend.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                processDataFriends(friends, user, instance);
            }
            return false;
        });

        btnAddFriend = view.findViewById(R.id.card_view_friends_button_add);
        btnAddFriend.setOnClickListener(view1 -> processDataFriends(friends, user, instance));
    }

    private void processDataFriends(List<FriendDto> friends, User user, MainApplication instance) {
        final FriendDto friendDto = addNewFriend();
        if (friendDto != null) {
            friends.add(0, friendDto);
            user.addFriend(friendDto);
            instance.setUser(user);
            txtUsernameFriend.setText(null);
            recycleViewFriends.getAdapter().notifyDataSetChanged();

            AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                    ValueActions.VALUE_ACTION_CLICK_BUTTON_ADD_FRIEND);
        }
    }

    private FriendDto addNewFriend() {
        LogUtils.i(TAG, "addNewFriend: Add new friend!");

        if (Strings.isNullOrEmpty(txtUsernameFriend.getText().toString())) {
            String msgErrorUsername = getContext().getString(R.string.msg_username_friend_required);
            AndroidUtils.changeErrorEditText(txtUsernameFriend, msgErrorUsername, true);
            return null;
        }
        AndroidUtils.changeErrorEditText(txtUsernameFriend);
        final String username = txtUsernameFriend.getText().toString().trim();
        txtUsernameFriend.setText(null);

        LogUtils.d(TAG, MessageFormat.format("Create new friend with username: {0}.", username));
        return new FriendDto().username(username);
    }

    private void setupButtonsProfile(View view) {
        final Fragment currentFragment = this;
        btnExportImportData = view.findViewById(R.id.button_profile_export_import_data);
        btnExportImportData.setOnClickListener(view12 -> {
            AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                    ValueActions.VALUE_ACTION_CLICK_BUTTON_EXPORT_IMPORT_DATA);
            ExportImportDataDialog.newInstance().showDialog(currentFragment);
        });

        btnEditProfile = view.findViewById(R.id.button_profile_edit_profile);
        btnEditProfile.setOnClickListener(view1 -> {
            AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                    ValueActions.VALUE_ACTION_CLICK_BUTTON_EDIT_PROFILE);

            EditProfileDialog.newInstance().showDialog(currentFragment);
        });
    }

    private class ProfileProgressApp extends ProgressApp {

        private final Intent data;

        ProfileProgressApp(Intent data) {
            super(ProfileFragment.this.getActivity(), R.string.msg_action_loading, false);
            this.data = data;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (data.getBooleanExtra(BundleConstant.IMPORT_BACKUP, Boolean.FALSE)) {
                AndroidUtils.toastLong(getContext(), R.string.toast_finish_import_data);
            } else {
                AndroidUtils.toastLong(getContext(), R.string.toast_error_import_data);
            }
            loadUserInfo(getView());
            loadDataFriends(getView());
            AndroidUtils.postAction(ActionCacheEnum.LOAD_ALL_DATA);
            dismissProgress();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Inject.provideCacheManageService().reloadSyncAllDataCache();
            return true;
        }
    }
}
