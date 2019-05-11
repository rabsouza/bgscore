package br.com.battista.bgscore.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {

    private static final String TAG = CurrencyUtils.class.getSimpleName();

    private static Locale locale = Locale.getDefault();
    private static NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

    private CurrencyUtils() {
    }

    public static String format(BigDecimal value) {
        LogUtils.d(TAG, "format: Format the value for string with formatting: $xxx.xxx,xx!");
        if (value == null) {
            return "";
        }

        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        return numberFormat.format(value);
    }
}
