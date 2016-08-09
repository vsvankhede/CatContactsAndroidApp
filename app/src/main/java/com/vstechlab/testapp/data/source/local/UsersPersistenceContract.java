package com.vstechlab.testapp.data.source.local;

import android.provider.BaseColumns;

public final class UsersPersistenceContract {
    public UsersPersistenceContract() {
    }

    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PROFILE = "profile";
    }
}
