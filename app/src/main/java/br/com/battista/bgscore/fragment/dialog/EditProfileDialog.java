package br.com.battista.bgscore.fragment.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.User;

public class EditProfileDialog extends DialogFragment {

    public static final int DIALOG_EDIT_PROFILE_FRAGMENT = 2;
    private static final String TAG = EditProfileDialog.class.getSimpleName();
    private static final String DIALOG_EDIT_PROFILE = "dialog_edit_profile";

    private Button btnSaveChange;
    private Button btnCancelChange;

    private EditText txtUsername;
    private EditText txtMail;

    public EditProfileDialog() {
    }

    public static EditProfileDialog newInstance() {
        EditProfileDialog fragment = new EditProfileDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void showDialog(@NonNull Fragment fragment) {
        Log.i(TAG, "showAbout: Show dialog edit profile!");

        setTargetFragment(fragment, DIALOG_EDIT_PROFILE_FRAGMENT);

        FragmentManager fm = fragment.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(DIALOG_EDIT_PROFILE);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, DIALOG_EDIT_PROFILE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragment = inflater.inflate(R.layout.dialog_edit_profile, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        loadViews(viewFragment);
        return viewFragment;
    }

    private void loadViews(View viewFragment) {
        Log.i(TAG, "loadViews: load all views!");
        final MainApplication instance = MainApplication.instance();

        btnCancelChange = viewFragment.findViewById(R.id.dialog_view_edit_profile_btn_cancel);
        btnCancelChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTargetFragment().onActivityResult(getTargetRequestCode(),
                        Activity.RESULT_CANCELED, getActivity().getIntent());
                getDialog().dismiss();
            }
        });

        btnSaveChange = viewFragment.findViewById(R.id.dialog_view_edit_profile_btn_save);
        btnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = instance.getUser();

                String username = txtUsername.getText().toString().trim();
                if (!TextUtils.isEmpty(username)) {
                    user.setUsername(username);
                }
                String mail = txtMail.getText().toString();
                if (!TextUtils.isEmpty(mail)) {
                    user.setMail(mail);
                }
                instance.setUser(user);

                getTargetFragment().onActivityResult(getTargetRequestCode(),
                        Activity.RESULT_OK, getActivity().getIntent());
                getDialog().dismiss();
            }
        });

        User user = instance.getUser();
        txtUsername = viewFragment.findViewById(R.id.dialog_view_edit_profile_username);
        txtUsername.setText(user.getUsername());
        txtMail = viewFragment.findViewById(R.id.dialog_view_edit_profile_mail);
        txtMail.setText(user.getMail());
    }

}
