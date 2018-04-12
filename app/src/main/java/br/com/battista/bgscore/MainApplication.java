package br.com.battista.bgscore;

import static br.com.battista.bgscore.constants.EntityConstant.DEFAULT_DATABASE_NAME;
import static br.com.battista.bgscore.constants.FontsConstant.DEFAULT;
import static br.com.battista.bgscore.constants.FontsConstant.DEFAULT_FONT;
import static br.com.battista.bgscore.constants.FontsConstant.MONOSPACE;
import static br.com.battista.bgscore.constants.FontsConstant.SANS_SERIF;
import static br.com.battista.bgscore.constants.FontsConstant.SANS_SERIF_FONT;
import static br.com.battista.bgscore.constants.FontsConstant.SERIF;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.orm.SugarContext;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;

import br.com.battista.bgscore.adpater.FontsAdapter;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.BackupDto;
import br.com.battista.bgscore.model.enuns.SharedPreferencesKeyEnum;
import br.com.battista.bgscore.repository.GameRepository;
import br.com.battista.bgscore.repository.MatchRepository;
import br.com.battista.bgscore.repository.PlayerRepository;
import br.com.battista.bgscore.service.CacheManageService;
import br.com.battista.bgscore.service.DatabaseManageService;
import br.com.battista.bgscore.util.LogUtils;
import io.fabric.sdk.android.Fabric;

public class MainApplication extends MultiDexApplication {

    private static final String TAG = MainApplication.class.getSimpleName();
    private static MainApplication instance = null;
    private final SharedPreferencesKeyEnum keyUser = SharedPreferencesKeyEnum.SAVED_USER;
    private final SharedPreferencesKeyEnum keyBackup = SharedPreferencesKeyEnum.SAVED_BACKUP;
    private User user;
    private BackupDto backupDto;
    private SharedPreferences preferences;
    private Boolean cleanDB = Boolean.FALSE;

    public static MainApplication instance() {
        return instance;
    }

    public static void init(Application application, boolean cleanDB) {
        instance = (MainApplication) application;
        instance.cleanDB = cleanDB;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }

        Fabric.with(instance, new Crashlytics());
        Fabric.with(instance, new Answers());
        LogUtils.d(TAG, "onCreate: MainApplication!");

        initializePreferences();
        initializeLoadImage();

        initializeDB();
        if (cleanDB) {
            cleanDB();
        }
        initializeCacheManager();
    }

    public User getUser() {
        synchronized (this) {
            if ((user == null
                    || Strings.isNullOrEmpty(user.getUsername())
                    || user.getCreatedAt() == null)
                    && preferences.contains(keyUser.name())) {
                try {
                    String jsonUSer = getPreferences(keyUser);
                    user = new ObjectMapper().readValue(jsonUSer, User.class);
                } catch (IOException e) {
                    LogUtils.e(TAG, "getUser: error convert user!", e);
                }
            }
            LogUtils.d(TAG, MessageFormat.format("Load user by cache with data: {0}", user));
        }
        return user;
    }

    public void setUser(User user) {
        synchronized (this) {
            LogUtils.d(TAG, MessageFormat.format("Update the cache user with data: {0}", user));
            instance.user = user;
            try {
                String jsonUser = new ObjectMapper().writeValueAsString(user);
                putPreferences(keyUser, jsonUser);
            } catch (JsonProcessingException e) {
                LogUtils.e(TAG, "setUser: error convert user!", e);
            }
        }
    }

    public BackupDto getBackup() {
        synchronized (this) {
            if (backupDto == null
                    && preferences.contains(keyBackup.name())) {
                try {
                    String jsonBackup = getPreferences(keyBackup);
                    backupDto = new ObjectMapper().readValue(jsonBackup, BackupDto.class);
                } catch (IOException e) {
                    LogUtils.e(TAG, "getBackup: error convert backup!", e);
                }
            }
            LogUtils.d(TAG, MessageFormat.format("Load backup by cache with data: {0}", backupDto));
        }
        return backupDto;
    }

    public void setBackup(BackupDto backup) {
        synchronized (this) {
            LogUtils.d(TAG, MessageFormat.format("Update the cache backup with data: {0}", backup));
            instance.backupDto = backup;
            try {
                String jsonBackup = new ObjectMapper().writeValueAsString(backup);
                putPreferences(keyBackup, jsonBackup);
            } catch (JsonProcessingException e) {
                LogUtils.e(TAG, "setUser: error convert backup!", e);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public Locale getCurrentLocale() {
        return getResources().getConfiguration().locale;
    }

    private void initializeLoadImage() {
        LogUtils.i(TAG, "initializeLoadImage: Initialize Glide to load image!");
    }

    private void initializeCacheManager() {
        LogUtils.i(TAG, "initializeCacheManager: Initialize event cache manager!");
        getApplicationContext().startService(new Intent(getApplicationContext(), CacheManageService.class));
        getApplicationContext().startService(new Intent(getApplicationContext(), DatabaseManageService.class));
    }

    private void initializePreferences() {
        preferences = getSharedPreferences(getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
    }

    public String getPreferences(@NonNull SharedPreferencesKeyEnum key) {
        return getPreferences(key, null);
    }

    public String getPreferences(@NonNull SharedPreferencesKeyEnum key, String defaultValue) {
        return preferences.getString(key.name(), defaultValue);
    }

    public Boolean putPreferences(@NonNull SharedPreferencesKeyEnum key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key.name(), value);
        return editor.commit();
    }

    public void clearPreferences() {
        preferences.edit().clear();
    }

    protected void initializeDB() {
        LogUtils.i(TAG, "initializeDB: Initialize Database to App.");
        SugarContext.init(getApplicationContext());
    }

    private void initializeSystemFont() {
        LogUtils.d(TAG, "initializeSystemFont: Add custom fonts to App.");
        FontsAdapter.setDefaultFont(instance, DEFAULT, DEFAULT_FONT);
        FontsAdapter.setDefaultFont(instance, MONOSPACE, DEFAULT_FONT);
        FontsAdapter.setDefaultFont(instance, SERIF, DEFAULT_FONT);
        FontsAdapter.setDefaultFont(instance, SANS_SERIF, SANS_SERIF_FONT);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogUtils.d(TAG, "onTerminate: MainApplication!");
        terminateCacheManager();
    }

    private void terminateCacheManager() {
        LogUtils.i(TAG, "terminateCacheManager: Terminate event cache manager!");
        getApplicationContext().stopService(new Intent(getApplicationContext(), CacheManageService.class));
        getApplicationContext().stopService(new Intent(getApplicationContext(), DatabaseManageService.class));
        SugarContext.terminate();
    }

    private void cleanDB() {
        LogUtils.i(TAG, "cleanDB: Clean Database to App.");
        getApplicationContext().deleteDatabase(DEFAULT_DATABASE_NAME);
    }

    public void cleanAllDataDB() {
        LogUtils.i(TAG, "cleanDB: Clean Database to App.");
        new GameRepository().deleteAll();
        new MatchRepository().deleteAll();
        new PlayerRepository().deleteAll();
    }

}
