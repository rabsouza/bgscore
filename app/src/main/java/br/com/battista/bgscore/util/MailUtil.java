package br.com.battista.bgscore.util;

import com.google.common.base.Strings;

import java.util.regex.Pattern;

public class MailUtil {

    private static final String CHAR_MAIL = "@";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private MailUtil() {
    }

    public static boolean isValidEmailAddress(final String mail) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(mail).matches();
    }

    public static String extractUsernameByMail(String mail) {
        if (Strings.isNullOrEmpty(mail) || !isValidEmailAddress(mail)) {
            return mail;
        } else {
            return mail.substring(0, mail.indexOf(CHAR_MAIL));
        }
    }

}
