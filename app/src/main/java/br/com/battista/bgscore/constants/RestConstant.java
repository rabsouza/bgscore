package br.com.battista.bgscore.constants;

public class RestConstant {

    public static final String REST_API_VERSION = "xmlapi/";
    public static final String REST_API_ENDPOINT = "https://www.boardgamegeek.com/";

    public static final String HEADER_CACHE_CONTROL_MAX_AGE_KEY = "Cache-Control";
    public static final String HEADER_CACHE_CONTROL_MAX_AGE_VALUE = "public, max-age=9600, max-stale=3600";
    public static final String HEADER_USER_AGENT_KEY = "User-Agent";
    public static final String HEADER_USER_AGENT_VALUE = "bg-score";
    public static final String HEADER_LOCALE_KEY = "locale";

    private RestConstant() {
    }
}