package br.com.battista.bgscore.util;

import static br.com.battista.bgscore.constants.EntityConstant.DEFAULT_BACKUP_DATABASE_NAME;
import static br.com.battista.bgscore.constants.EntityConstant.DEFAULT_DATABASE_NAME;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import br.com.battista.bgscore.BuildConfig;
import br.com.battista.bgscore.MainApplication;
import br.com.battista.bgscore.constants.EntityConstant;
import br.com.battista.bgscore.model.User;
import br.com.battista.bgscore.model.dto.BackupDto;

public class BackupUtils {

    public static final String DEFAULT_DIR_APP = "bgscore";
    private static final String TAG = BackupUtils.class.getSimpleName();

    private BackupUtils() {
    }

    public static Boolean existsData(@NonNull Context context) {

        File sd = getFileDir(context);
        File backupFile = new File(sd, EntityConstant.DEFAULT_BACKUP_INFO_NAME);
        File userFile = new File(sd, EntityConstant.DEFAULT_BACKUP_SHARED_PREFERENCES_NAME);
        File dbFile = new File(sd, EntityConstant.DEFAULT_BACKUP_DATABASE_NAME);

        return backupFile.exists() && userFile.exists() && dbFile.exists();
    }

    public static BackupDto getBackupData(@NonNull Context context) {
        final BackupDto backup = MainApplication.instance().getBackup();

        BackupDto backupFile;
        File sd = getFileDir(context);
        File backupDB = new File(sd, EntityConstant.DEFAULT_BACKUP_INFO_NAME);
        if (backupDB.exists()) {
            try {
                backupFile = new ObjectMapper().readValue(new FileReader(backupDB), BackupDto.class);
            } catch (IOException e) {
                LogUtils.e(TAG, "getBackup: invalid backup file.", e);
                backupFile = backup;
            }
        } else {
            backupFile = backup;
        }
        return backupFile;
    }

    public static User getUserData(@NonNull Context context) {
        final User user = MainApplication.instance().getUser();

        User userFile;
        File sd = getFileDir(context);
        File userDB = new File(sd, EntityConstant.DEFAULT_BACKUP_SHARED_PREFERENCES_NAME);
        if (userDB.exists()) {
            try {
                userFile = new ObjectMapper().readValue(new FileReader(userDB), User.class);
            } catch (IOException e) {
                LogUtils.e(TAG, "getBackup: invalid user file.", e);
                userFile = user;
            }
        } else {
            userFile = user;
        }
        return userFile;
    }

    public static void exportDatabase(@NonNull Context context) throws IOException {
        LogUtils.i(TAG, "exportDatabase: start exporting the database!");
        String currentDBPath = String.format("/data/%1$s/databases/%2$s", BuildConfig.APPLICATION_ID, DEFAULT_DATABASE_NAME);

        File sd = getFileDir(context);
        File data = Environment.getDataDirectory();
        File currentDB = new File(data, currentDBPath);
        if (!currentDB.exists()) {
            currentDB.createNewFile();
        }
        File backupDB = new File(sd, DEFAULT_BACKUP_DATABASE_NAME);
        if (!backupDB.exists()) {
            backupDB.createNewFile();
        }

        FileChannel source = new FileInputStream(currentDB).getChannel();
        FileChannel destination = new FileOutputStream(backupDB).getChannel();
        destination.transferFrom(source, 0, source.size());

        source.close();
        destination.close();

        LogUtils.i(TAG, "exportDatabase: finished exporting the database!");
    }

    public static void importDatabase(@NonNull Context context) throws IOException {
        LogUtils.i(TAG, "importDatabase: start importing the database!");
        String currentDBPath = String.format("/data/%1$s/databases/%2$s", BuildConfig.APPLICATION_ID, DEFAULT_DATABASE_NAME);

        File sd = getFileDir(context);
        File data = Environment.getDataDirectory();
        File currentDB = new File(data, currentDBPath);
        if (!currentDB.exists()) {
            currentDB.createNewFile();
        }
        File backupDB = new File(sd, DEFAULT_BACKUP_DATABASE_NAME);
        if (!backupDB.exists()) {
            backupDB.createNewFile();
        }

        FileChannel source = new FileInputStream(backupDB).getChannel();
        FileChannel destination = new FileOutputStream(currentDB).getChannel();
        destination.transferFrom(source, 0, source.size());

        source.close();
        destination.close();

        LogUtils.i(TAG, "importDatabase: finished importing the database!");
    }

    public static File getFileDir(@NonNull Context context) {
        File fileDir = AndroidUtils.getFileDir(context);
        File backupDir = new File(fileDir, DEFAULT_DIR_APP);
        backupDir.mkdirs();
        return backupDir;
    }

    public static void flushFileData(File file, String json) throws IOException {
        InputStream source = new ByteArrayInputStream(json.getBytes());
        byte[] buffer = new byte[source.available()];
        source.read(buffer);
        OutputStream destination = new FileOutputStream(file);
        destination.write(buffer);

        source.close();
        destination.flush();
        destination.close();
    }

}