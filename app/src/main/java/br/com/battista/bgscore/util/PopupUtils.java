package br.com.battista.bgscore.util;

import android.support.v7.widget.PopupMenu;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PopupUtils {

    private static final String TAG = PopupUtils.class.getSimpleName();

    private PopupUtils() {
    }

    public static void showPopupWindow(PopupMenu popup) {
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }

        } catch (Exception e) {
            LogUtils.e(TAG, "showPopupWindow: Error show popup menu!", e);
        }
    }
}
