package br.com.battista.bgscore.constants;

public class CrashlyticsConstant {
    public static final String KEY_ACTIVITY = "Activity";
    public static final String KEY_OPEN_ACTIVITY = "Open Activity";

    public static final String KEY_FRAGMENT = "Fragment";
    public static final String KEY_OPEN_FRAGMENT = "Open Fragment";
    public static final String KEY_ACTION = "Action";

    public interface Actions {
        String ACTION_CLICK_MENU = "ActionClickMenu";
        String ACTION_CLICK_BUTTON = "ActionClickButton";
        String ACTION_EXIT = "ActionExit";
        String ACTION_OPEN = "ActionOpen";
    }

    public interface ValueActions {
        String VALUE_CLICK_MENU_HOME = "HOME";
        String VALUE_CLICK_MENU_MATCHES = "MATCHES";
        String VALUE_CLICK_MENU_GAMES = "GAMES";
        String VALUE_CLICK_MENU_PROFILE = "PROFILE";
        String VALUE_CLICK_MENU_INFO = "INFO";
        String VALUE_ACTION_EXIT = "EXIT";
        String VALUE_ACTION_OPEN = "OPEN";
        String VALUE_ACTION_CLICK_BUTTON_CHANGE_AVATAR = "CHANGE_AVATAR";
        String VALUE_ACTION_CLICK_BUTTON_EDIT_PROFILE = "EDIT_PROFILE";
        String VALUE_ACTION_CLICK_BUTTON_ADD_FRIEND = "ADD_FRIEND";
        String VALUE_ACTION_CLICK_BUTTON_ADD_MATCH = "ADD_MATCH";
        String VALUE_ACTION_CLICK_BUTTON_ADD_GAME = "ADD_GAME";
    }

}
