package br.com.battista.bgscore.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.constants.BundleConstant;
import br.com.battista.bgscore.constants.CrashlyticsConstant;
import br.com.battista.bgscore.constants.PermissionConstant;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.BackupDto;
import br.com.battista.bgscore.model.enuns.ActionDatabaseEnum;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.AnswersUtils;
import br.com.battista.bgscore.util.BackupUtils;
import br.com.battista.bgscore.util.DateUtils;
import br.com.battista.bgscore.util.LogUtils;

import static br.com.battista.bgscore.constants.DialogConstant.DIALOG_EXPORT_IMPORT_DATA_FRAGMENT;

public class ExportImportDataDialog extends DialogFragment {

    private static final String TAG = ExportImportDataDialog.class.getSimpleName();
    private static final String DIALOG_EXPORT_IMPORT_DATA = "dialog_export_import_data";

    private Button btnExport;
    private Button btnImport;
    private Button btnClose;

    private TextView txtBackupExport;
    private TextView txtPathDirExport;
    private TextView txtPathDirImport;

    private MaterialCardView cardViewBackup;
    private TextView txtBackupDataVersion;
    private TextView txtBackupDataUser;
    private TextView txtBackupDataDate;

    public ExportImportDataDialog() {
    }

    public static ExportImportDataDialog newInstance() {
        ExportImportDataDialog fragment = new ExportImportDataDialog();
        Bundle args = new Bundle();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        fragment.setArguments(args);
        return fragment;
    }

    public void showDialog(@NonNull Fragment fragment) {
        LogUtils.i(TAG, "showAbout: Show dialog export/import data!");

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
        LogUtils.i(TAG, "loadViews: load all views!");

        final User user = MainApplication.instance().getUser();
        final BackupDto backup = MainApplication.instance().getBackup();

        txtBackupExport = viewFragment.findViewById(R.id.dialog_view_export_data_info_03);

        final Resources resources = getContext().getResources();
        String statusBackup = user.isAutomaticBackup() ? resources.getString(R.string.text_active) : resources.getString(R.string.text_inactive);
        String lastBackup = backup != null ? DateUtils.formatWithHours(backup.getCreatedAt()) : "-";
        txtBackupExport.setText(resources.getString(R.string.text_dialog_export_data_info_03, statusBackup, lastBackup));

        cardViewBackup = viewFragment.findViewById(R.id.dialog_view_import_card_view_backup_info);
        txtBackupDataVersion = viewFragment.findViewById(R.id.dialog_view_import_card_view_backup_info_version);
        txtBackupDataUser = viewFragment.findViewById(R.id.dialog_view_import_card_view_backup_info_user);
        txtBackupDataDate = viewFragment.findViewById(R.id.dialog_view_import_card_view_backup_info_date);

        btnClose = viewFragment.findViewById(R.id.dialog_view_export_import_data_btn_close);
        btnImport = viewFragment.findViewById(R.id.dialog_view_export_import_data_btn_import);
        btnExport = viewFragment.findViewById(R.id.dialog_view_export_import_data_btn_export);

        final BackupDto backupFile = BackupUtils.getBackupData(getContext());
        if (backupFile != null && BackupUtils.existsData(getContext())) {
            cardViewBackup.setVisibility(View.VISIBLE);
            btnImport.setEnabled(true);

            txtBackupDataVersion.setText(resources.getString(R.string.text_dialog_import_last_data_version, backupFile.getVersionName()));
            txtBackupDataUser.setText(resources.getString(R.string.text_dialog_import_last_data_user, backupFile.getUser()));
            txtBackupDataDate.setText(resources.getString(R.string.text_dialog_import_last_data_date, DateUtils.formatWithHours(backupFile.getCreatedAt())));
        } else {
            cardViewBackup.setVisibility(View.GONE);
            btnImport.setEnabled(false);
        }

        txtPathDirExport = viewFragment.findViewById(R.id.dialog_view_export_data_info_02);
        txtPathDirImport = viewFragment.findViewById(R.id.dialog_view_import_data_info_02);

        File dirBackup = BackupUtils.getFileDir(getContext());
        txtPathDirExport.setText(
                resources.getString(R.string.text_dialog_export_data_info_02, dirBackup.getAbsolutePath()));
        txtPathDirImport.setText(
                resources.getString(R.string.text_dialog_import_data_info_02, dirBackup.getAbsolutePath()));

        btnClose.setOnClickListener(view -> {
            getTargetFragment().onActivityResult(getTargetRequestCode(),
                    Activity.RESULT_CANCELED, getActivity().getIntent());
            getDialog().dismiss();
        });

        btnImport.setOnClickListener(view -> {
            AndroidUtils.checkPermissions(getActivity(), PermissionConstant.STORAGE_PERMISSIONS);
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_IMPORT_DATA);

            createDialogImportBackup(backupFile);
        });

        btnExport.setOnClickListener(view -> {
            AndroidUtils.checkPermissions(getActivity(), PermissionConstant.STORAGE_PERMISSIONS);
            AnswersUtils.onActionMetric(CrashlyticsConstant.Actions.ACTION_CLICK_BUTTON,
                    CrashlyticsConstant.ValueActions.VALUE_ACTION_CLICK_BUTTON_EXPORT_DATA);

            AndroidUtils.toast(getContext(), R.string.toast_start_export_data);
            AndroidUtils.postAction(ActionDatabaseEnum.EXPORT_ALL_DATA);

            final Intent intent = getActivity().getIntent();
            intent.putExtra(BundleConstant.ACTION, BundleConstant.Action.EXPORT);
            getTargetFragment().onActivityResult(getTargetRequestCode(),
                    Activity.RESULT_OK, getActivity().getIntent());
            getDialog().dismiss();
        });

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.dialog_title_backup);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationPopup;
        return dialog;
    }

    private void createDialogImportBackup(BackupDto backupDto) {
        final Context context = getContext();
        String message = context.getResources().getString(R.string.alert_confirmation_dialog_text_import_backup,
                DateUtils.formatWithHours(backupDto.getCreatedAt()));
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.alert_confirmation_dialog_title_backup)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.btn_confirmation_dialog_restore, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        btnImport.setEnabled(false);

                        final Intent intent = getActivity().getIntent();
                        intent.putExtra(BundleConstant.ACTION, BundleConstant.Action.IMPORT);

                        try {
                            LogUtils.i(TAG, "createDialogImportBackup: import data from backup!");
                            if (BackupUtils.existsData(getContext())) {
                                final MainApplication instance = MainApplication.instance();
                                instance.setUser(BackupUtils.getUserData(getContext()));
                                instance.setBackup(BackupUtils.getBackupData(getContext()));
                                BackupUtils.importDatabase(getContext());
                            }

                            intent.putExtra(BundleConstant.IMPORT_BACKUP, Boolean.TRUE);
                        } catch (Exception e) {
                            LogUtils.e(TAG, "createDialogImportBackup: error import data from backup!", e);
                            intent.putExtra(BundleConstant.IMPORT_BACKUP, Boolean.FALSE);
                        }

                        getTargetFragment().onActivityResult(getTargetRequestCode(),
                                Activity.RESULT_OK, getActivity().getIntent());
                        getDialog().dismiss();
                    }
                })
                .setNegativeButton(R.string.btn_confirmation_dialog_cancel, null).create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.animationAlert;
        alertDialog.show();
    }

}
