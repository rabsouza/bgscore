package br.com.battista.bgscore.fragment.dialog;

import com.google.common.base.Strings;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.Switch;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.service.CacheManageService;
import br.com.battista.bgscore.util.AndroidUtils;

import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_EDIT_PROFILE_FRAGMENT;

public class EditProfileDialog extends DialogFragment {

    private static final String TAG = EditProfileDialog.class.getSimpleName();
    private static final String DIALOG_EDIT_PROFILE = "dialog_edit_profile";

    private Button btnSaveChange;
    private Button btnCancelChange;

    private EditText txtUsername;
    private EditText txtMail;
    private Switch swtReset;

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

        swtReset = viewFragment.findViewById(R.id.dialog_view_edit_profile_reset);

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
                User user;
                if (swtReset.isChecked()) {
                    user = new User().username(getString(R.string.text_default_username));
                    user.initEntity();
                } else {
                    user = instance.getUser();
                    if (Strings.isNullOrEmpty(txtUsername.getText().toString())) {
                        String msgErrorUsername = getContext().getString(R.string.msg_username_required);
                        AndroidUtils.changeErrorEditText(txtUsername, msgErrorUsername, true);
                        return;
                    }
                    AndroidUtils.changeErrorEditText(txtUsername);
                    String username = txtUsername.getText().toString().trim();
                    user.username(username);

                    String mail = txtMail.getText().toString();
                    user.mail(mail);
                }
                instance.setUser(user);

                new CacheManageService().reloadAllDataCache();

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationPopup;
        return dialog;
    }

}
