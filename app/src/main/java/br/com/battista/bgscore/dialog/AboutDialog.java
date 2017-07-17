package br.com.battista.bgscore.dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.util.DateUtils;

public class AboutDialog extends DialogFragment {

    private static final String TAG = AboutDialog.class.getSimpleName();

    public static void showAbout(android.support.v4.app.FragmentManager fm) {
        Log.i(TAG, "showAbout: Show dialog about!");
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog_about");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        new AboutDialog().show(ft, "dialog_about");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SpannableStringBuilder aboutBody = new SpannableStringBuilder();
        String versionName = BuildConfig.VERSION_NAME;
        Integer versionNumber = BuildConfig.VERSION_CODE;

        Calendar buildDate = Calendar.getInstance();
        buildDate.setTimeInMillis(BuildConfig.BUILD_UPDATED);
        String info = MessageFormat.format(getString(R.string.about_dialog_text), versionName,
                versionNumber, DateUtils.format(buildDate));
        aboutBody.append(Html.fromHtml(info));

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        TextView view = (TextView) inflater.inflate(R.layout.dialog_about, null);
        view.setText(aboutBody);
        view.setMovementMethod(new LinkMovementMethod());

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_about)
                .setView(view)
                .setPositiveButton(R.string.btn_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .create();
    }

}
