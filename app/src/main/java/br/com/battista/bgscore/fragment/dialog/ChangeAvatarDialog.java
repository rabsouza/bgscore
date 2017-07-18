package br.com.battista.bgscore.fragment.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.BundleConstant;

public class ChangeAvatarDialog extends DialogFragment {

    public static final int DIALOG_SHARE_CAMPAIGN_FRAGMENT = 1;
    private static final String TAG = ChangeAvatarDialog.class.getSimpleName();
    private static final String DIALOG_OPEN_CAMPAIGN = "dialog_open_campaign";
    private Button btnConfirmChange;
    private Button btnCancelChange;

    public ChangeAvatarDialog() {
    }

    public static ChangeAvatarDialog newInstance() {
        ChangeAvatarDialog fragment = new ChangeAvatarDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void showDialog(@NonNull Fragment fragment) {
        Log.i(TAG, "showAbout: Show dialog open campaign!");

        setTargetFragment(fragment, DIALOG_SHARE_CAMPAIGN_FRAGMENT);

        FragmentManager fm = fragment.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(DIALOG_OPEN_CAMPAIGN);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, DIALOG_OPEN_CAMPAIGN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragment = inflater.inflate(R.layout.dialog_change_avatar, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

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
                getActivity().getIntent().putExtra(BundleConstant.KEY, "");
                getTargetFragment().onActivityResult(getTargetRequestCode(),
                        Activity.RESULT_OK, getActivity().getIntent());
                getDialog().dismiss();
            }
        });
    }

}
