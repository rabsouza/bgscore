package br.com.battista.bgscore.fragment.dialog;

import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_EXPORT_IMPORT_DATA_FRAGMENT;

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
import android.widget.TextView;

import java.io.File;

import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.enuns.ActionDatabaseEnum;
import br.com.battista.bgscore.util.AndroidUtils;

public class ExportImportDataDialog extends DialogFragment {

    private static final String TAG = ExportImportDataDialog.class.getSimpleName();
    private static final String DIALOG_EXPORT_IMPORT_DATA = "dialog_export_import_data";

    private Button btnExport;
    private Button btnImport;
    private Button btnClose;

    private TextView txtPathDirExport;
    private TextView txtPathDirImport;

    public ExportImportDataDialog() {
    }

    public static ExportImportDataDialog newInstance() {
        ExportImportDataDialog fragment = new ExportImportDataDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void showDialog(@NonNull Fragment fragment) {
        Log.i(TAG, "showAbout: Show dialog export/import data!");

        setTargetFragment(fragment, DIALOG_EXPORT_IMPORT_DATA_FRAGMENT);

        FragmentManager fm = fragment.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(DIALOG_EXPORT_IMPORT_DATA);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, DIALOG_EXPORT_IMPORT_DATA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragment = inflater.inflate(R.layout.dialog_export_import_data, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        loadViews(viewFragment);
        return viewFragment;
    }

    private void loadViews(final View viewFragment) {
        Log.i(TAG, "loadViews: load all views!");

        txtPathDirExport = viewFragment.findViewById(R.id.dialog_view_export_data_info_02);
        txtPathDirImport = viewFragment.findViewById(R.id.dialog_view_import_data_info_02);

        File dirBackup = AndroidUtils.getFileDir(getContext());
        txtPathDirExport.setText(
                getContext().getResources().getString(R.string.text_dialog_export_data_info_02, dirBackup.getAbsolutePath()));
        txtPathDirImport.setText(
                getContext().getResources().getString(R.string.text_dialog_import_data_info_02, dirBackup.getAbsolutePath()));

        btnClose = viewFragment.findViewById(R.id.dialog_view_export_import_data_btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTargetFragment().onActivityResult(getTargetRequestCode(),
                        Activity.RESULT_OK, getActivity().getIntent());
                getDialog().dismiss();
            }
        });

        btnImport = viewFragment.findViewById(R.id.dialog_view_export_import_data_btn_import);
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btnExport = viewFragment.findViewById(R.id.dialog_view_export_import_data_btn_export);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnExport.setEnabled(Boolean.FALSE);
                btnExport.setAlpha(0.5f);

                AndroidUtils.toast(getContext(), R.string.toast_start_export_data);
                AndroidUtils.postAction(ActionDatabaseEnum.EXPORT_ALL_DATA);
            }
        });

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationPopup;
        return dialog;
    }

}
