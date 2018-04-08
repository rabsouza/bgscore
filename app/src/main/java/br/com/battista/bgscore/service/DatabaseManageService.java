package br.com.battista.bgscore.service;

import static br.com.battista.bgscore.constants.EntityConstant.DEFAULT_BACKUP_DATABASE_NAME;
import static br.com.battista.bgscore.constants.EntityConstant.DEFAULT_BACKUP_INFO_NAME;
import static br.com.battista.bgscore.constants.EntityConstant.DEFAULT_BACKUP_SHARED_PREFERENCES_NAME;
import static br.com.battista.bgscore.constants.EntityConstant.DEFAULT_DATABASE_NAME;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
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

public class DatabaseManageService extends Service {

    private static final String TAG = DatabaseManageService.class.getSimpleName();
    public static final String DEFAULT_USER_BACKUP = "backup";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: Register event bus to Action!");
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onActionDatabase(ActionDatabaseEnum action) {
        Log.i(TAG, MessageFormat.format("ActionDatabaseEnum: Process to action: {0}.", action));

        if (ActionDatabaseEnum.EXPORT_ALL_DATA.equals(action)) {
            exportAllData();
        } else if (ActionDatabaseEnum.BACKUP_ALL_DATA.equals(action)) {
            backupAllData();
        }
    }

    private synchronized void backupAllData() {
        Log.i(TAG, "backupAllData: Export all data from database and SharedPreferences!");

        try {
            exportDatabase();
            exportSharedPreferences();
            createDatabaseInfo();

        } catch (IOException e) {
            Log.e(TAG, "Error export all database!", e);
        }
    }

    private synchronized void exportAllData() {
        Log.i(TAG, "exportAllData: Export all data from database and SharedPreferences!");

        try {
            exportDatabase();
            exportSharedPreferences();
            createDatabaseInfo();

            AndroidUtils.postAction(ActionToastMainEnum.FINISH_EXPORT_DATA);
        } catch (IOException e) {
            Log.e(TAG, "Error export all database!", e);
        }
    }

    private void createDatabaseInfo() throws IOException {
        Log.i(TAG, "createDatabaseInfo: Start create database info!");
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
        instance.setBackupData(backupDto);

        File sd = AndroidUtils.getFileDir(getBaseContext());
        File backupDB = new File(sd, DEFAULT_BACKUP_INFO_NAME);

        String jsonBackup = new ObjectMapper().writeValueAsString(backupDto);
        InputStream source = new ByteArrayInputStream(jsonBackup.getBytes());
        byte[] buffer = new byte[source.available()];
        source.read(buffer);
        OutputStream destination = new FileOutputStream(backupDB);
        destination.write(buffer);

        source.close();
        destination.close();

        Log.i(TAG, "createDatabaseInfo: Finished create database info!");
    }

    private void exportSharedPreferences() throws IOException {
        Log.i(TAG, "exportSharedPreferences: Start exporting the sharedPreferences!");
        String userData = MainApplication.instance().getPreferences(SharedPreferencesKeyEnum.SAVED_USER);

        File sd = AndroidUtils.getFileDir(getBaseContext());
        File backupDB = new File(sd, DEFAULT_BACKUP_SHARED_PREFERENCES_NAME);

        InputStream source = new ByteArrayInputStream(userData.getBytes());
        byte[] buffer = new byte[source.available()];
        source.read(buffer);
        OutputStream destination = new FileOutputStream(backupDB);
        destination.write(buffer);

        source.close();
        destination.close();

        Log.i(TAG, "exportDatabase: Finished exporting the sharedPreferences!");
    }

    private void exportDatabase() throws IOException {
        Log.i(TAG, "exportDatabase: start exporting the database!");
        String currentDBPath = String.format("/data/%1$s/databases/%2$s", BuildConfig.APPLICATION_ID, DEFAULT_DATABASE_NAME);

        File sd = AndroidUtils.getFileDir(getBaseContext());
        File data = Environment.getDataDirectory();
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, DEFAULT_BACKUP_DATABASE_NAME);

        FileChannel source = new FileInputStream(currentDB).getChannel();
        FileChannel destination = new FileOutputStream(backupDB).getChannel();
        destination.transferFrom(source, 0, source.size());

        source.close();
        destination.close();

        Log.i(TAG, "exportDatabase: Finished exporting the database!");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActionToastMainEnum(ActionToastMainEnum action) {
        Log.i(TAG, MessageFormat.format("ActionToastMainEnum: Process to action: {0}.", action));

        if (ActionToastMainEnum.FINISH_EXPORT_DATA.equals(action)) {
            AndroidUtils.toast(getBaseContext(), R.string.toast_finish_export_data);
        }
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "onCreate: Unregister event bus to Action!");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
