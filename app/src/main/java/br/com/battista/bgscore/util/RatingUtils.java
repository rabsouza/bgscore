package br.com.battista.bgscore.util;

import java.text.NumberFormat;
import java.text.ParseException;

public class RatingUtils {

    private static NumberFormat nf = NumberFormat.getCurrencyInstance();

    private RatingUtils() {
    }

    public static String convertTo(Float rating) {
        nf.setMaximumFractionDigits(1);
        nf.setMinimumFractionDigits(1);
        return nf.format(rating);
    }

    public static Float convertFrom(String rating) {
        nf.setMaximumFractionDigits(1);
        nf.setMinimumFractionDigits(1);
        try {
            return nf.parse(rating).floatValue();
        } catch (ParseException e) {
            return 0F;
        }
    }
}
