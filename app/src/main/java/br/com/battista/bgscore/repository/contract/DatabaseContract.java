package br.com.battista.bgscore.repository.contract;

import android.provider.BaseColumns;

public class DatabaseContract {

    public DatabaseContract() {
    }

    public static abstract class BaseEntry implements BaseColumns {

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_PK = "pk";
        public static final String COLUMN_NAME_CREATED_AT = "CREATED_AT";
        public static final String COLUMN_NAME_UPDATED_AT = "UPDATED_AT";
        public static final String COLUMN_NAME_VERSION = "VERSION";
        public static final String COLUMN_NAME_SYNCHRONIZED_AT = "SYNCHRONIZED_AT";
        public static final String COLUMN_NAME_ENTITY_SYNCHRONIZED = "ENTITY_SYNCHRONIZED";
    }

    public static abstract class UserEntry extends BaseEntry {

        public static final String TABLE_NAME = "USER";
        public static final String COLUMN_NAME_USERNAME = "USERNAME";
        public static final String COLUMN_NAME_MAIL = "MAIL";
        public static final String COLUMN_NAME_AVATAR = "AVATAR";
        public static final String COLUMN_NAME_LAST_PLAY = "LAST_PLAY";
        public static final String COLUMN_NAME_NUM_GAMES = "NUM_GAMES";
        public static final String COLUMN_NAME_NUM_MATCHES = "NUM_MATCHES";
        public static final String COLUMN_NAME_TOTAL_TIME = "TOTAL_TIME";
        public static final String COLUMN_NAME_FRIENDS = "FRIENDS";
        public static final String COLUMN_NAME_RANKING_GAMES = "RANKING_GAMES";
        public static final String COLUMN_NAME_WELCOME = "WELCOME";
        public static final String COLUMN_NAME_LAST_BUILD_VERSION = "LAST_BUILD_VERSION";
        public static final String COLUMN_NAME_ORDER_BY = "ORDER_BY";
        public static final String COLUMN_NAME_CUSTOM_FONT = "CUSTOM_FONT";
        public static final String COLUMN_NAME_AUTOMATC_BACKUP = "AUTOMATC_BACKUP";
    }

    public static abstract class GameEntry extends BaseEntry {

        public static final String TABLE_NAME = "GAME";
        public static final String COLUMN_NAME_ID_BGG = "ID_BGG";
        public static final String COLUMN_NAME_NAME = "NAME";
        public static final String COLUMN_NAME_URL_THUMBNAIL = "URL_THUMBNAIL";
        public static final String COLUMN_NAME_URL_IMAGE = "URL_IMAGE";
        public static final String COLUMN_NAME_URL_INFO = "URL_INFO";
        public static final String COLUMN_NAME_YEAR_PUBLISHED = "YEAR_PUBLISHED";
        public static final String COLUMN_NAME_MIN_PLAYERS = "MIN_PLAYERS";
        public static final String COLUMN_NAME_MAX_PLAYERS = "MAX_PLAYERS";
        public static final String COLUMN_NAME_MIN_PLAY_TIME = "MIN_PLAY_TIME";
        public static final String COLUMN_NAME_MAX_PLAY_TIME = "MAX_PLAY_TIME";
        public static final String COLUMN_NAME_AGE = "AGE";
        public static final String COLUMN_NAME_RATING = "RATING";
        public static final String COLUMN_NAME_MY_GAME = "MY_GAME";
        public static final String COLUMN_NAME_FAVORITE = "FAVORITE";
        public static final String COLUMN_NAME_WANT_GAME = "WANT_GAME";
        public static final String COLUMN_NAME_BADGE_GAME = "BADGE_GAME";
        public static final String COLUMN_NAME_EXPANSIONS = "EXPANSIONS";
    }

    public static abstract class ExpansionGameEntry extends BaseEntry {

        public static final String TABLE_NAME = "GAME_EXPANSION";
        public static final String COLUMN_NAME_ID_BGG = "ID_BGG";
        public static final String FK_GAME_ID = "FK_GAME_ID";
        public static final String COLUMN_NAME_NAME = "NAME";
        public static final String COLUMN_NAME_YEAR_PUBLISHED = "YEAR_PUBLISHED";
        public static final String COLUMN_NAME_URL_THUMBNAIL = "URL_THUMBNAIL";
        public static final String COLUMN_NAME_MIN_PLAYERS = "MIN_PLAYERS";
        public static final String COLUMN_NAME_MAX_PLAYERS = "MAX_PLAYERS";
        public static final String COLUMN_NAME_MIN_PLAY_TIME = "MIN_PLAY_TIME";
        public static final String COLUMN_NAME_MAX_PLAY_TIME = "MAX_PLAY_TIME";
    }

    public static abstract class PlayerEntry extends BaseEntry {

        public static final String TABLE_NAME = "PLAYER";
        public static final String FK_MATCH_ID = "FK_MATCH_ID";
        public static final String COLUMN_NAME_NAME = "NAME";
        public static final String COLUMN_NAME_PUNCTUATION = "PUNCTUATION";
        public static final String COLUMN_NAME_WINNER = "WINNER";
        public static final String COLUMN_NAME_TYPE_PLAYER = "TYPE_PLAYER";
    }

    public static abstract class MatchEntry extends BaseEntry {

        public static final String TABLE_NAME = "MATCH";
        public static final String COLUMN_NAME_ALIAS = "ALIAS";
        public static final String COLUMN_NAME_GAME = "GAME";
        public static final String FK_GAME_ID = "FK_GAME_ID";
        public static final String COLUMN_NAME_PLAYERS = "PLAYERS";
        public static final String COLUMN_NAME_I_PLAYING = "I_PLAYING";
        public static final String COLUMN_NAME_FINISHED = "FINISHED";
        public static final String COLUMN_NAME_DURATION = "DURATION";
        public static final String COLUMN_NAME_FEEDBACK = "FEEDBACK";
        public static final String COLUMN_NAME_FEEDBACK_ENUM = "FEEDBACK_ENUM";
        public static final String COLUMN_NAME_OBS = "OBS";
    }
}
