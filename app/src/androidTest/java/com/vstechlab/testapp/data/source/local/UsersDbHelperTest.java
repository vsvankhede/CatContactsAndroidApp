package com.vstechlab.testapp.data.source.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.vstechlab.testapp.data.source.local.UsersPersistenceContract.UserEntry;

import org.junit.Before;

import java.util.HashSet;

import static org.junit.Assert.*;

public class UsersDbHelperTest extends AndroidTestCase {

    void deleteDatabase() {
        mContext.deleteDatabase(UsersDbHelper.DATABASE_NAME);
    }

    @Before
    public void setUp() throws Exception {
        deleteDatabase();
    }

    public void testCreateDb() {
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(UserEntry.TABLE_NAME);

        SQLiteDatabase db = new UsersDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: Database has not been created properly.", c.moveToFirst());

        do{
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertTrue("Error: The database created without expected tables.", tableNameHashSet.isEmpty());
    }
}