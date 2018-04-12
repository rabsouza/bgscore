package br.com.battista.bgscore.constants;

public class EntityConstant {

    public static final String EMPTY_STRING = "";
    public static final Long DEFAULT_VERSION = 1L;
    public static final String DEFAULT_URL_INFO_GAME = "https://www.boardgamegeek.com/boardgame/";
    public static final String DEFAULT_URL_BUY_GAME = "https://pricequest.com.br/busca?q=";
    public static final String DEFAULT_URL_BUY_GAME_ENG = "https://www.boardgameprices.com/compare-prices-for?q=";
    public static final Integer DEFAULT_CACHE_SIZE = 10 * 1024 * 1024;
    public static final String DEFAULT_DATABASE_NAME = "bgscore_db.db";
    public static final String DEFAULT_BACKUP_DATABASE_NAME = "bkp_bgscore_db.db";
    public static final String DEFAULT_BACKUP_SHARED_PREFERENCES_NAME = "bkp_user_data.json";
    public static final String DEFAULT_BACKUP_INFO_NAME = "bkp_info.json";

    private EntityConstant() {
    }

}
