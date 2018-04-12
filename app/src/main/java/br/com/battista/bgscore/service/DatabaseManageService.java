package br.com.battista.bgscore.service;

import static br.com.battista.bgscore.constants.EntityConstant.DEFAULT_BACKUP_INFO_NAME;
import static br.com.battista.bgscore.constants.EntityConstant.DEFAULT_BACKUP_SHARED_PREFERENCES_NAME;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import br.com.battista.bgscore.BuildConfig;
import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.R;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.BackupDto;
import br.com.battista.bgscore.model.enuns.ActionDatabaseEnum;
import br.com.battista.bgscore.model.enuns.ActionToastMainEnum;
import br.com.battista.bgscore.model.enuns.SharedPreferencesKeyEnum;
import br.com.battista.bgscore.util.AndroidUtils;
import br.com.battista.bgscore.util.BackupUtils;
import br.com.battista.bgscore.util.LogUtils;

public class DatabaseManageService extends Service {

    public static final String DEFAULT_USER_BACKUP = "backup";
    private static final String TAG = DatabaseManageService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG, "onCreate: Register event bus to Action!");
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onActionDatabase(ActionDatabaseEnum action) {
        LogUtils.i(TAG, MessageFormat.format("ActionDatabaseEnum: Process to action: {0}.", action));

        if (ActionDatabaseEnum.EXPORT_ALL_DATA.equals(action)) {
            exportAllData();
        } else if (ActionDatabaseEnum.BACKUP_ALL_DATA.equals(action)) {
            backupAllData();
        }
    }

    private synchronized void backupAllData() {
        LogUtils.i(TAG, "backupAllData: Export all data from database and SharedPreferences!");

        try {
            BackupUtils.exportDatabase(getBaseContext());
            exportSharedPreferences();
            createDatabaseInfo();

        } catch (IOException e) {
            LogUtils.e(TAG, "Error export all database!", e);
        }
    }

    private synchronized void exportAllData() {
        LogUtils.i(TAG, "exportAllData: Export all data from database and SharedPreferences!");

        try {
            BackupUtils.exportDatabase(getBaseContext());
            exportSharedPreferences();
            createDatabaseInfo();

            AndroidUtils.postAction(ActionToastMainEnum.FINISH_EXPORT_DATA);
        } catch (IOException e) {
            LogUtils.e(TAG, "Error export all database!", e);
        }
    }

    private void createDatabaseInfo() throws IOException {
        LogUtils.i(TAG, "createDatabaseInfo: Start create database info!");
        final MainApplication instance = MainApplication.instance();
        final User user = instance.getUser();
        BackupDto backupDto = new BackupDto();
        backupDto.initEntity();
        backupDto.versionName(BuildConfig.VERSION_NAME);
        backupDto.deviceId(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        if (user == null) {
            backupDto.user(DEFAULT_USER_BACKUP);
        } else {
            backupDto.user(user.getUsername());
        }
        instance.setBackup(backupDto);

        File sd = BackupUtils.getFileDir(getBaseContext());
        File backupDB = new File(sd, DEFAULT_BACKUP_INFO_NAME);
        String jsonBackup = new ObjectMapper().writeValueAsString(backupDto);
        BackupUtils.flushFileData(backupDB, jsonBackup);

        LogUtils.i(TAG, "createDatabaseInfo: Finished create database info!");
    }

    private void exportSharedPreferences() throws IOException {
        LogUtils.i(TAG, "exportSharedPreferences: Start exporting the sharedPreferences!");
        String userData = MainApplication.instance().getPreferences(SharedPreferencesKeyEnum.SAVED_USER);

        File sd = BackupUtils.getFileDir(getBaseContext());
        File backupDB = new File(sd, DEFAULT_BACKUP_SHARED_PREFERENCES_NAME);
        BackupUtils.flushFileData(backupDB, userData);

        LogUtils.i(TAG, "exportDatabase: Finished exporting the sharedPreferences!");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActionToastMainEnum(ActionToastMainEnum action) {
        LogUtils.i(TAG, MessageFormat.format("ActionToastMainEnum: Process to action: {0}.", action));

        if (ActionToastMainEnum.FINISH_EXPORT_DATA.equals(action)) {
            AndroidUtils.toast(getBaseContext(), R.string.toast_finish_export_data);
        }
    }


    @Override
    public void onDestroy() {
        LogUtils.i(TAG, "onCreate: Unregister event bus to Action!");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
