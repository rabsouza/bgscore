package br.com.battista.bgscore.util;

import java.text.NumberFormat;
import java.text.ParseException;

public class RatingUtils {

    private static NumberFormat nf = NumberFormat.getNumberInstance();

    private RatingUtils() {
    }

    public static String convertTo(Float rating) {
        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);
        nf.setGroupingUsed(false);
        return nf.format(rating);
    }

    public static Float convertFrom(String rating) {
        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);
        nf.setGroupingUsed(false);
        try {
            return nf.parse(rating).floatValue();
        } catch (ParseException e) {
            return 0F;
        }
    }

}
