package com.vstechlab.testapp.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vstechlab.testapp.data.User;
import com.vstechlab.testapp.data.source.UsersDataSource;
import com.vstechlab.testapp.data.source.local.UsersPersistenceContract.UserEntry;

import java.util.ArrayList;
import java.util.List;

public class UsersLocalDataSource implements UsersDataSource{
    private static UsersLocalDataSource INSTANCE;

    private UsersDbHelper mDbHelper;

    private SQLiteDatabase mDb;

    private UsersLocalDataSource(@NonNull Context context) {
        mDbHelper = new UsersDbHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    public static UsersLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UsersLocalDataSource(context);
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            Cursor c = mDb.query(UserEntry.TABLE_NAME, null, null, null, null, null,null);
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    int id = c.getInt(c.getColumnIndex(UserEntry.COLUMN_USER_ID));
                    String username = c.getString(c.getColumnIndex(UserEntry.COLUMN_USERNAME));
                    String profilePath = c.getString(c.getColumnIndex(UserEntry.COLUMN_PROFILE));

                    User user = new User(id, username, profilePath);

                    users.add(user);
                }
            }

            if (c != null) {
                c.close();
            }

        } catch (IllegalStateException e) {
            // Todo handle exception
        }
        return users;
    }

    @Nullable
    @Override
    public User getUser(int id) {
        try {
            String selection = UserEntry.COLUMN_USER_ID  + " = ?";
            String[] selectionArgs = {
                    String.valueOf(id)
            };

            Cursor c = mDb.query(
                    UserEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null, null);

            User user = null;

            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                int userId = c.getInt(c.getColumnIndex(UserEntry._ID));
                String username = c.getString(c.getColumnIndex(UserEntry.COLUMN_USERNAME));
                String profilePath = c.getString(c.getColumnIndex(UserEntry.COLUMN_PROFILE));

                user = new User(userId, username, profilePath);
            }

            if (c != null) {
                c.close();
            }
            return user;
        } catch (IllegalStateException e) {

        }

        return null;
    }

    @Override
    public void editUser(@NonNull User user) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(UserEntry.COLUMN_USERNAME, user.getUsername());
            cv.put(UserEntry.COLUMN_PROFILE, user.getProfile());
            String selection = UserEntry.COLUMN_USER_ID + " = ?";

            String[] selectionArgs = {
                    String.valueOf(user.getId())
            };

            mDb.update(UserEntry.TABLE_NAME, cv, selection, selectionArgs);
        } catch (IllegalStateException e) {

        }
    }

    @Override
    public void refreshUsers() {
        // No required
    }

    @Override
    public void saveUser(@NonNull User user) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(UserEntry.COLUMN_USER_ID, user.getId());
            cv.put(UserEntry.COLUMN_USERNAME, user.getUsername());
            cv.put(UserEntry.COLUMN_PROFILE, user.getProfile());

            mDb.insertWithOnConflict(UserEntry.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(@NonNull User user) {
        try {
            String selection = UserEntry.COLUMN_USER_ID + " = ?";
            String[] selectionArgs = {
                    String.valueOf(user.getId())
            };
            mDb.delete(UserEntry.TABLE_NAME, selection, selectionArgs);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
