package com.vstechlab.testapp.data.source.local;

import android.test.AndroidTestCase;

import com.vstechlab.testapp.data.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class UsersLocalDataSourceTest extends AndroidTestCase {
    private static final int USER_ID1 = 1;

    private static final String USERNAME1= "username1";

    private static final String PROFILE_PATH1 = "http://path1.jpg";

    private static final int USER_ID2 = 2;

    private static final String USERNAME2= "username2";

    private static final String PROFILE_PATH2 = "http://path2.jpg";

    private UsersLocalDataSource mLocalDataSource;

    private static List<User> USERS = new ArrayList<User>();

    static {
        USERS.add(new User(USER_ID1, USERNAME1, PROFILE_PATH1));
        USERS.add(new User(USER_ID2, USERNAME2, PROFILE_PATH2));
    }

    private void deleteDb() {
        mContext.deleteDatabase(UsersDbHelper.DATABASE_NAME);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        deleteDb();
        mLocalDataSource = UsersLocalDataSource.getInstance(mContext);
    }

    public void testSaveUser_retrieveUser() {
        final User user = new User(USER_ID1, USERNAME1, PROFILE_PATH1);

        mLocalDataSource.saveUser(user);

        assertThat(mLocalDataSource.getUser(user.getId()), is(user));
    }

    public void testSaveUsers_retrieveUser() {
        for (User user : USERS) {
            mLocalDataSource.saveUser(user);
        }

        for (User user : mLocalDataSource.getUsers()) {
            USERS.remove(user);
        }
        assertTrue(USERS.isEmpty());
    }

    public void testEditUser_retrieveUser() {

        testSaveUser_retrieveUser();

        User user = new User(USER_ID1, USERNAME2, PROFILE_PATH1);

        mLocalDataSource.editUser(user);

        assertThat(mLocalDataSource.getUser(USER_ID1), is(user));
    }


}