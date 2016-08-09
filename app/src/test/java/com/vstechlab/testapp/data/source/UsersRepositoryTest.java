package com.vstechlab.testapp.data.source;

import android.content.Context;

import com.vstechlab.testapp.data.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UsersRepositoryTest {
    private static final int USER_ID1 = 1;

    private static final String USERNAME1= "username1";

    private static final String PROFILE_PATH1 = "http://path1.jpg";

    private static final int USER_ID2 = 2;

    private static final String USERNAME2= "username2";

    private static final String PROFILE_PATH2 = "http://path2.jpg";

    private static List<User> USERS = new ArrayList<User>();

    private UsersRepository mUserRepository;

    @Mock
    private UsersDataSource mUserRemoteDataSource;

    @Mock
    private UsersDataSource mUserLocalDataSource;

    static {
        USERS.add(new User(USER_ID1, USERNAME1, PROFILE_PATH1));
        USERS.add(new User(USER_ID2, USERNAME2, PROFILE_PATH2));
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mUserRepository = UsersRepository.
                getInstance(mUserRemoteDataSource, mUserLocalDataSource);
    }

    @After
    public void destroyRepositoryInstance() {
        UsersRepository.destroyInstance();
    }

    @Test
    public void getUsers_repositoryCachesAfterFirstApiCall() {
        twoUsersLoadCallsToRepository();

        verify(mUserRemoteDataSource).getUsers();
    }

    @Test
    public void getUsers_refreshesLocalDataSource() {
        mUserRepository.refreshUsers();

        setUsersAvailable(mUserRemoteDataSource, USERS);

        mUserRepository.getUsers();

        verify(mUserLocalDataSource, times(USERS.size())).saveUser(any(User.class));
    }

    private void setUsersAvailable(UsersDataSource mUserRemoteDataSource, List<User> users) {
        when(mUserRemoteDataSource.getUsers()).thenReturn(users);
    }

    private void twoUsersLoadCallsToRepository() {
        when(mUserLocalDataSource.getUsers()).thenReturn(null);

        when(mUserRemoteDataSource.getUsers()).thenReturn(USERS);

        mUserRepository.getUsers();

        verify(mUserLocalDataSource).getUsers();

        verify(mUserRemoteDataSource).getUsers();

        mUserRepository.getUsers(); // Second call to Api;
    }
}