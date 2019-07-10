package br.com.battista.bgscore.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ImageLoadUtils {

    private ImageLoadUtils() {
    }

    public static void loadImage(@NonNull Context context, @NonNull String url,
                                 @NonNull ImageView img) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(img);
    }

    public static void loadImage(@NonNull Context context, @NonNull Integer resId,
                                 @NonNull ImageView img) {
        Glide.with(context)
                .load(resId)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(img);
    }

    public static void loadImageWithImageError(@NonNull Context context,
                                               @NonNull String url, @NonNull ImageView img,
                                               int resIdError) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(resIdError)
                .into(img);
    }

    public static void loadImageWithPlaceHolderAndError(@NonNull Context context,
                                                        @NonNull String url, @NonNull ImageView img,
                                                        int resIdError) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(resIdError)
                .into(img);
    }

}
