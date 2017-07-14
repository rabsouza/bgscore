package br.com.battista.bgscore;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.orm.SugarContext;

import java.util.Locale;

import br.com.battista.bgscore.adpater.FontsAdapter;
import br.com.battista.bgscore.model.enuns.SharedPreferencesKeyEnum;
import br.com.battista.bgscore.service.CacheManageService;

import static br.com.battista.bgscore.constants.FontsConstant.DEFAULT;
import static br.com.battista.bgscore.constants.FontsConstant.DEFAULT_FONT;
import static br.com.battista.bgscore.constants.FontsConstant.MONOSPACE;
import static br.com.battista.bgscore.constants.FontsConstant.SANS_SERIF;
import static br.com.battista.bgscore.constants.FontsConstant.SANS_SERIF_FONT;
import static br.com.battista.bgscore.constants.FontsConstant.SERIF;

public class MainApplication extends MultiDexApplication {

    private static final String TAG = MainApplication.class.getSimpleName();

    private static MainApplication instance = null;

    private SharedPreferences preferences;

    public static MainApplication instance() {
        return instance;
    }

    public static void init(Application application) {
        instance = (MainApplication) application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: MainApplication!");

        initializeSystemFont();
        initializePreferences();
        initializeLoadImage();

        instance = this;
        initializeDB();
        initializeCacheManager();
    }

    public Locale getCurrentLocale() {
        return getResources().getConfiguration().locale;
    }

    private void initializeLoadImage() {
        Log.i(TAG, "initializeLoadImage: Initialize Glide to load image!");
    }

    private void initializeCacheManager() {
        Log.i(TAG, "initializeCacheManager: Initialize event cache manager!");
        getApplicationContext().startService(new Intent(getApplicationContext(), CacheManageService.class));
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


    protected void initializeDB() {
        Log.i(TAG, "initializeDB: Initialize Database to App.");
        SugarContext.init(getApplicationContext());
    }

    private void initializeSystemFont() {
        Log.d(TAG, "initializeSystemFont: Add custom fonts to App.");
        FontsAdapter.setDefaultFont(this, DEFAULT, DEFAULT_FONT);
        FontsAdapter.setDefaultFont(this, MONOSPACE, DEFAULT_FONT);
        FontsAdapter.setDefaultFont(this, SERIF, DEFAULT_FONT);
        FontsAdapter.setDefaultFont(this, SANS_SERIF, SANS_SERIF_FONT);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate: MainApplication!");
        terminateCacheManager();
    }

    private void terminateCacheManager() {
        Log.i(TAG, "terminateCacheManager: Terminate event cache manager!");
        getApplicationContext().stopService(new Intent(getApplicationContext(), CacheManageService.class));
        SugarContext.terminate();
    }

}
