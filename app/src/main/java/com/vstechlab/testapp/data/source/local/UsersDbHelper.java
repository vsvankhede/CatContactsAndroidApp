package com.vstechlab.testapp.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vstechlab.testapp.data.source.local.UsersPersistenceContract.UserEntry;

public class UsersDbHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "UsersEntity.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String NUMBER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_USER =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + COMMA_SEP +
                    UserEntry.COLUMN_USER_ID + NUMBER_TYPE + " UNIQUE" + COMMA_SEP +
                    UserEntry.COLUMN_USERNAME + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_PROFILE + TEXT_TYPE + ");";

    public UsersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
