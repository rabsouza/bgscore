package br.com.battista.bgscore.fragment.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.adpater.AvatarAdapter;
import br.com.battista.bgscore.constants.BundleConstant;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

public class ChangeAvatarDialog extends DialogFragment {

    public static final int DIALOG_CHANGE_AVATAR_FRAGMENT = 1;
    private static final String TAG = ChangeAvatarDialog.class.getSimpleName();
    private static final String DIALOG_CHANGE_AVATAR = "dialog_change_avatar";

    private Button btnConfirmChange;
    private Button btnCancelChange;
    private RecyclerView recyclerViewAvatars;
    private AvatarAdapter avatarAdapter;
    @DrawableRes
    private int currentAvatar;

    public ChangeAvatarDialog() {
    }

    public static ChangeAvatarDialog newInstance(@DrawableRes int currentAvatar) {
        ChangeAvatarDialog fragment = new ChangeAvatarDialog();
        Bundle args = new Bundle();
        args.putInt(BundleConstant.CURRENT_AVATAR, currentAvatar);
        fragment.setArguments(args);
        return fragment;
    }

    public void showDialog(@NonNull Fragment fragment) {
        Log.i(TAG, "showAbout: Show dialog change avatar!");

        setTargetFragment(fragment, DIALOG_CHANGE_AVATAR_FRAGMENT);

        FragmentManager fm = fragment.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(DIALOG_CHANGE_AVATAR);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, DIALOG_CHANGE_AVATAR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragment = inflater.inflate(R.layout.dialog_change_avatar, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        currentAvatar = getArguments().getInt(BundleConstant.CURRENT_AVATAR);

        loadViews(viewFragment);
        return viewFragment;
    }

    private void loadViews(View viewFragment) {
        Log.i(TAG, "loadViews: load all views!");

        btnCancelChange = viewFragment.findViewById(R.id.dialog_view_change_avatar_btn_cancel);
        btnCancelChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTargetFragment().onActivityResult(getTargetRequestCode(),
                        Activity.RESULT_CANCELED, getActivity().getIntent());
                getDialog().dismiss();
            }
        });

        btnConfirmChange = viewFragment.findViewById(R.id.dialog_view_change_avatar_btn_confirm);
        btnConfirmChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = getActivity().getIntent();
                intent.putExtra(BundleConstant.CURRENT_AVATAR, avatarAdapter.getCurrentAvatar());
                getTargetFragment().onActivityResult(getTargetRequestCode(),
                        Activity.RESULT_OK, intent);
                getDialog().dismiss();
            }
        });

        recyclerViewAvatars = viewFragment.findViewById(R.id.dialog_view_change_avatar_recycler_view);
        recyclerViewAvatars.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
        recyclerViewAvatars.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAvatars.setHasFixedSize(true);
        avatarAdapter = new AvatarAdapter(getContext(), currentAvatar);
        recyclerViewAvatars.setAdapter(avatarAdapter);
    }

}
