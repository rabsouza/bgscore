package br.com.battista.bgscore.constants;

public class BundleConstant {

    public static final String ACTION = "ACTION";
    public static final String DATA = "DATA";
    public static final String POSITION = "POSITION";
    public static final String KEY = "KEY";
    public static final String CURRENT_AVATAR = "CURRENT_AVATAR";
    public static final String SEARCH_GAME_ID = "SEARCH_GAME_ID";
    public static final String NAVIGATION_TO = "NAVIGATION_TO";
    public static final String WELCOME_DIALOG_CONTENT = "WELCOME_DIALOG_CONTENT";

    public interface NavigationTo {
        int HOME_FRAGMENT = 0;
        int MATCH_FRAGMENT = 1;
        int GAME_FRAGMENT = 2;
        int PROFILE_FRAGMENT = 3;
        int NEW_MATCH_FRAGMENT = 4;
        int FINISH_MATCH_FRAGMENT = 5;
        int DETAIL_MATCH_FRAGMENT = 6;
    }
}
