package br.com.battista.bgscore.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.CrashlyticsConstant.Actions;
import br.com.battista.bgscore.constants.CrashlyticsConstant.ValueActions;
import br.com.battista.bgscore.constants.PermissionConstant;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.enuns.ActionDatabaseEnum;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.LogUtils;

import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_EDIT_PROFILE_FRAGMENT;
import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_GOOGLE_SIGN_IN;

public class EditProfileDialog extends DialogFragment {

    private static final String TAG = EditProfileDialog.class.getSimpleName();
    private static final String DIALOG_EDIT_PROFILE = "dialog_edit_profile";

    private Button btnSaveChange;
    private Button btnCancelChange;

    private EditText txtUsername;
    private EditText txtMail;
    private Switch swtReset;
    private Switch swtCustomFont;
    private Switch swtAutomaticBackup;

    private Boolean signedSuccessfully = Boolean.FALSE;

    private GoogleSignInClient googleSignInClient;
    private SignInButton signInButton;
    private Button signOutButton;

    public EditProfileDialog() {
    }

    public static EditProfileDialog newInstance() {
        EditProfileDialog fragment = new EditProfileDialog();
        Bundle args = new Bundle();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        fragment.setArguments(args);
        return fragment;
    }

    public void showDialog(@NonNull Fragment fragment) {
        LogUtils.i(TAG, "showAbout: Show dialog edit profile!");

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        loadViews(viewFragment);
        return viewFragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null) {
            updateUI(account, account.isExpired());
        }
    }

    private void updateUI(GoogleSignInAccount account, Boolean singOut) {
        if (account != null) {
            LogUtils.i(TAG, String.format("updateUI - SignIn: Update user with data [name=%s, e-mail:%s].",
                    account.getDisplayName(), account.getEmail()));
            signedSuccessfully = Boolean.TRUE;

            swtReset.setEnabled(Boolean.FALSE);
            swtReset.setChecked(Boolean.FALSE);

            txtUsername.setEnabled(Boolean.FALSE);
            txtUsername.setText(account.getDisplayName());

            txtMail.setEnabled(Boolean.FALSE);
            txtMail.setText(account.getEmail());

            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);

            User user = MainApplication.instance().getUser();
            Uri photoUrl = account.getPhotoUrl();
            if (photoUrl != null) {
                user.setUrlAvatar(photoUrl.toString());
            }
        } else if (singOut) {
            LogUtils.i(TAG, "updateUI - SingOut: Update user with data ");
            signedSuccessfully = Boolean.FALSE;

            swtReset.setEnabled(Boolean.TRUE);
            txtUsername.setEnabled(Boolean.TRUE);
            txtMail.setEnabled(Boolean.TRUE);
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        } else {
            signedSuccessfully = Boolean.FALSE;
            AndroidUtils.toast(getContext(), R.string.toast_error_login_google);
        }
    }

    private void loadViews(View viewFragment) {
        LogUtils.i(TAG, "loadViews: load all views!");
        final MainApplication instance = MainApplication.instance();
        User user = instance.getUser();

        swtReset = viewFragment.findViewById(R.id.dialog_view_edit_profile_reset);
        swtCustomFont = viewFragment.findViewById(R.id.dialog_view_edit_custom_font);
        swtCustomFont.setChecked(user.isCustomFont());
        swtAutomaticBackup = viewFragment.findViewById(R.id.dialog_view_edit_automatic_backup);
        swtAutomaticBackup.setChecked(user.isAutomaticBackup());

        signInButton = viewFragment.findViewById(R.id.dialog_view_edit_profile_sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(view -> {
            AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                    ValueActions.VALUE_ACTION_CLICK_BUTTON_SIGN_IN);

            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, DIALOG_GOOGLE_SIGN_IN);
        });

        signOutButton = viewFragment.findViewById(R.id.dialog_view_edit_profile_sign_out_button);
        signOutButton.setOnClickListener(view -> {
            AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                    ValueActions.VALUE_ACTION_CLICK_BUTTON_SIGN_OUT);

            googleSignInClient.signOut()
                    .addOnCompleteListener(getActivity(), task -> {
                        updateUI(null, Boolean.TRUE);
                    });
        });

        btnCancelChange = viewFragment.findViewById(R.id.dialog_view_edit_profile_btn_cancel);
        btnCancelChange.setOnClickListener(view -> {
            getTargetFragment().onActivityResult(getTargetRequestCode(),
                    Activity.RESULT_CANCELED, getActivity().getIntent());
            getDialog().dismiss();
        });

        btnSaveChange = viewFragment.findViewById(R.id.dialog_view_edit_profile_btn_save);
        btnSaveChange.setOnClickListener(view -> {
            User currentUser;
            if (swtReset.isChecked()) {
                currentUser = new User().username(getString(R.string.text_default_username));
                currentUser.initEntity();
            } else {
                currentUser = instance.getUser();
                if (Strings.isNullOrEmpty(txtUsername.getText().toString())) {
                    String msgErrorUsername = getContext().getString(R.string.msg_username_required);
                    AndroidUtils.changeErrorEditText(txtUsername, msgErrorUsername, true);
                    return;
                }
                AndroidUtils.changeErrorEditText(txtUsername);
                String username = txtUsername.getText().toString().trim();
                currentUser.username(username);

                String mail = txtMail.getText().toString();
                currentUser.mail(mail);
                currentUser.customFont(swtCustomFont.isChecked());
                currentUser.automaticBackup(swtAutomaticBackup.isChecked());
                if (swtAutomaticBackup.isChecked()) {
                    AndroidUtils.checkPermissions(getActivity(), PermissionConstant.STORAGE_PERMISSIONS);
                    AnswersUtils.onActionMetric(Actions.ACTION_CLICK_BUTTON,
                            ValueActions.VALUE_ACTION_CLICK_BUTTON_BACKUP_DATA);
                    AndroidUtils.postAction(ActionDatabaseEnum.BACKUP_ALL_DATA);
                }
            }
            currentUser.signedSuccessfully(signedSuccessfully);
            instance.setUser(currentUser);

            getTargetFragment().onActivityResult(getTargetRequestCode(),
                    Activity.RESULT_OK, getActivity().getIntent());
            getDialog().dismiss();
        });

        txtUsername = viewFragment.findViewById(R.id.dialog_view_edit_profile_username);
        txtUsername.setText(user.getUsername());

        txtMail = viewFragment.findViewById(R.id.dialog_view_edit_profile_mail);
        txtMail.setText(user.getMail());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DIALOG_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                LogUtils.i(TAG, "Signed in successfully, show authenticated UI." + account.getDisplayName());
                updateUI(account, Boolean.FALSE);
            } catch (ApiException e) {
                LogUtils.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                updateUI(null, Boolean.FALSE);
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.dialog_title_edit_profile);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationPopup;
        return dialog;
    }

}
