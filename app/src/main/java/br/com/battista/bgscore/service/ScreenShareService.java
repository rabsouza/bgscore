package br.com.battista.bgscore.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;

import br.com.battista.bgscore.R;


public class ScreenShareService {

    private static final String TAG = "ScreenShareService";
    private static final String IMAGES_PATH_CACHE = "images";
    private static final String APP_PACKAGE = "br.com.battista.bgscore";
    private static final String DEFAULT_NAME_FILE = "bgscore_match-#KEY#.png";
    private static final String KEY_NAME_FILE = "#KEY#";

    private final Context context;

    public ScreenShareService(Context context) {
        this.context = context;
    }

    public void shareScreen(@NonNull View activity, String textShare) {
        String nameFile = DEFAULT_NAME_FILE.replace(KEY_NAME_FILE, String.valueOf(SystemClock.currentThreadTimeMillis()));
        Log.i(TAG, MessageFormat.format("shareScreen: Share to match in the file name: {0}.", nameFile));

        Bitmap bitmap = takeScreenshot(activity);
        saveImageToDisk(bitmap, nameFile);

        Log.i(TAG, "shareScreen: Success to save image to disk!");
        shareImage(nameFile, textShare);
    }

    private Bitmap takeScreenshot(View rootView) {
        Log.i(TAG, "takeScreenshot: Take Screenshot to view!");
        rootView.setDrawingCacheEnabled(true);
        rootView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rootView.buildDrawingCache(true);
        rootView.buildDrawingCache();

        Bitmap screenshot = Bitmap.createBitmap(rootView.getDrawingCache());

        rootView.setDrawingCacheEnabled(false);
        rootView.destroyDrawingCache();

        return screenshot;
    }

    private boolean saveImageToDisk(Bitmap screenshot, String nameFile) {
        Log.i(TAG, "saveImageToDisk: Save to img to disk!");
        try {
            File cachePath = new File(context.getCacheDir(), IMAGES_PATH_CACHE);
            cachePath.delete();
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath.getPath() + "/" + nameFile);
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();

        } catch (FileNotFoundException e) {
            Log.e(TAG, "saveImageToDisk: error in save to disk.", e);
            return false;
        } catch (IOException e) {
            Log.e(TAG, "saveImageToDisk: error in save to disk.", e);
            return false;
        }
        return true;
    }

    private void shareImage(String nameFile, String description) {
        Log.i(TAG, "shareImage: share to image!");
        File imagePath = new File(context.getCacheDir(), IMAGES_PATH_CACHE);
        File newFile = new File(imagePath, nameFile);
        Uri contentUri = FileProvider.getUriForFile(context, APP_PACKAGE,
                newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));

            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, description);
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.hint_title_share_match));

            context.startActivity(Intent.createChooser(shareIntent, description));
            Log.i(TAG, "shareImage: success and start to share!");
        }
    }

}
