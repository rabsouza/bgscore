package br.com.battista.bgscore.adpater;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

public abstract class BaseAdapterAnimation<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    // used to animate when first reaching the view
    private int lastPosition;

    // used to animate views
    private int screenWidth;

    // used as a proportional delay when animating views on the entry screen (otherwise all would animate together).
    private int firs_run_delay = 105;

    public BaseAdapterAnimation(Context context) {
        /*
            Used to get Screen Width to animate views when first seeing them
         */
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;

        /*
            Sets the delay to 0, as the other views will only animate when reached.
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                firs_run_delay = 0;
            }
        }, 700);
    }

    protected void setAnimationHolder(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {

            viewToAnimate.setTranslationX(-screenWidth);
            viewToAnimate.setAlpha(0f);
            viewToAnimate.animate().
                    translationX(0).
                    alpha(1f).
                    setStartDelay(firs_run_delay * position)
                    .setDuration(400)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
            lastPosition = position;


        }
    }

}
