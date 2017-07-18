package br.com.battista.bgscore.repository.contract;

import android.provider.BaseColumns;

public class DatabaseContract {

    public DatabaseContract() {
    }

    public static abstract class BaseEntry implements BaseColumns {

        public static final String COLUMN_NAME_ID = "ID";
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
        public static final String COLUMN_NAME_URL_AVATAR = "URL_AVATAR";
        public static final String COLUMN_NAME_LAST_PLAY = "LAST_PLAY";
        public static final String COLUMN_NAME_NUM_GAMES = "NUM_GAMES";
        public static final String COLUMN_NAME_NUM_MATCHES = "NUM_MATCHES";
        public static final String COLUMN_NAME_TOTAL_TIME = "TOTAL_TIME";
    }

}
