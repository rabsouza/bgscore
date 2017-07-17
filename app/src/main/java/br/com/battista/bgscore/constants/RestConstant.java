package br.com.battista.bgscore.constants;

public class RestConstant {

    public static final String REST_API_V1 = "api/v1/";
    public static final String REST_API_ENDPOINT = "https://arcadia-caller.appspot.com/";
    public static final String APP_STATUS = "app.status";
    public static final String APP_STATUS_UP = "UP";
    public static final String APP_STATUS_HEAL = "HEAL";

    public static final String HEADER_CACHE_CONTROL_MAX_AGE_KEY = "Cache-Control";
    public static final String HEADER_CACHE_CONTROL_MAX_AGE_VALUE = "public, max-age=9600, max-stale=3600";
    public static final String HEADER_USER_AGENT_KEY = "User-Agent";
    public static final String HEADER_USER_AGENT_VALUE = "arcadia-caller-app";
    public static final String HEADER_LOCALE_KEY = "locale";

    private RestConstant() {
    }
}