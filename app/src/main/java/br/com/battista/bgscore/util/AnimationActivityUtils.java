package br.com.battista.bgscore.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.Fade;
import android.transition.Transition;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;

public class AnimationActivityUtils {

    private static final int DURATION = 250;

    /**
     * This method is used to remove the Status Bar and the Navbar from
     * the shared elements transition, otherwise they will blink.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Transition makeEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        return fade;
    }

    /**
     * This method animates a view's Z position when touched.
     *
     * @param viewToClick   view that, when clicked, will activate the animation
     * @param viewToAnimate view that will have it's Z position changed
     * @param upWhenClicked true = makes the view go up when touched, false = makes the view to gown
     *                      when touched
     */
    public static void setAnimationElevation(View viewToClick, final View viewToAnimate,
                                             final boolean upWhenClicked) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            final float start = DimensionsUtils.convertDpToPixel(2, viewToAnimate.getContext());
            final float finalZ;
            if (!upWhenClicked)
                finalZ = 0f;
            else
                finalZ = start * 2f;
            viewToAnimate.setElevation(0);
            viewToAnimate.setTranslationZ(start);

            viewToClick.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int action = motionEvent.getActionMasked();
                    ViewPropertyAnimator animation = null;
                    /* Raise view on ACTION_DOWN and lower it on ACTION_UP. */
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            animateZ(viewToAnimate, finalZ);
                            break;
                        case MotionEvent.ACTION_UP:
                            animateZ(viewToAnimate, start);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            animateZ(viewToAnimate, start);
                            break;
                    }
                    return false;
                }
            });

        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static ViewPropertyAnimator animateZ(View v, float elevation) {
        return v.animate().translationZ(elevation).setDuration(DURATION).setStartDelay(0);
    }

}
