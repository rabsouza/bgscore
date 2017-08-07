package br.com.battista.bgscore.fragment.dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.Calendar;

import br.com.battista.bgscore.BuildConfig;
import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.util.DateUtils;

import static br.com.battista.bgscore.constants.BundleConstant.WELCOME_DIALOG_CONTENT;

public class WelcomeDialog extends DialogFragment {

    private static final String TAG = WelcomeDialog.class.getSimpleName();

    public static WelcomeDialog newInstance(@StringRes int resStringContent) {
        WelcomeDialog welcomeDialog = new WelcomeDialog();

        Bundle args = new Bundle();
        if (resStringContent != 0) {
            args.putInt(WELCOME_DIALOG_CONTENT, resStringContent);
        }
        welcomeDialog.setArguments(args);

        return welcomeDialog;
    }

    public void showAbout(android.support.v4.app.FragmentManager fm) {
        Log.i(TAG, "showAbout: Show welcome about!");
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("welcome_about");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, "welcome_about");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SpannableStringBuilder aboutBody = new SpannableStringBuilder();
        String versionName = BuildConfig.VERSION_NAME;

        Calendar buildDate = Calendar.getInstance();
        buildDate.setTimeInMillis(BuildConfig.BUILD_UPDATED);

        String info;
        if (getArguments().containsKey(WELCOME_DIALOG_CONTENT)) {
            info = MessageFormat.format(getString(
                    getArguments().getInt(WELCOME_DIALOG_CONTENT, R.string.welcome_dialog_text)),
                    versionName, DateUtils.format(buildDate));
        } else {
            info = MessageFormat.format(getString(R.string.welcome_dialog_text), versionName,
                    DateUtils.format(buildDate));
        }
        aboutBody.append(Html.fromHtml(info));

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        TextView view = (TextView) inflater.inflate(R.layout.dialog_welcome, null);
        view.setText(aboutBody);
        view.setMovementMethod(new LinkMovementMethod());

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_welcome)
                .setView(view)
                .setPositiveButton(R.string.btn_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                final MainApplication instance = MainApplication.instance();
                                final User user = instance.getUser();
                                user.welcome(false);
                                user.lastBuildVersion(BuildConfig.VERSION_CODE);
                                instance.setUser(user);
                                dialog.dismiss();
                            }
                        }
                )
                .create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animationPopup;
        return alertDialog;
    }

}
