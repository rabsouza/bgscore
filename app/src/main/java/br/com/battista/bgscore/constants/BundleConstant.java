package br.com.battista.bgscore.constants;

public class BundleConstant {

    public static final String ACTION = "ACTION";
    public static final String DATA = "DATA";
    public static final String POSITION = "POSITION";
    public static final String KEY = "KEY";
    public static final String CURRENT_AVATAR = "CURRENT_AVATAR";
    public static final String NAVIGATION_TO = "NAVIGATION_TO";

    public interface NavigationTo {
        int HOME_FRAGMENT = 0;
        int MATCH_FRAGMENT = 1;
        int GAME_FRAGMENT = 2;
        int PROFILE_FRAGMENT = 3;
    }
}
