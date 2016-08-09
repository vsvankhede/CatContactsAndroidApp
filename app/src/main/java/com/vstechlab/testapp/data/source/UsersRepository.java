package com.vstechlab.testapp.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vstechlab.testapp.data.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UsersRepository implements UsersDataSource{
    private static UsersRepository INSTANCE;

    private final UsersDataSource mUsersLocalDataSource;

    private final UsersDataSource mUsersRemoteDataSource;

    private List<UsersRepositoryObserver> mObserver = new ArrayList<UsersRepositoryObserver>();

    Map<Integer, User> mCachedUsers;

    boolean mCacheIsDirty;

    private UsersRepository(@NonNull UsersDataSource usersRemoteDataSource,
                            @NonNull UsersDataSource userLocalDataSource) {
        mUsersRemoteDataSource = usersRemoteDataSource;
        mUsersLocalDataSource = userLocalDataSource;
    }

    public static UsersRepository getInstance(@NonNull UsersDataSource userRemoteDataSource,
                                              @NonNull UsersDataSource userLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UsersRepository(userRemoteDataSource, userLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public void addContentObserver(UsersRepositoryObserver observer) {
        if (!mObserver.contains(observer)) {
            mObserver.add(observer);
        }
    }

    public void removeObserver(UsersRepositoryObserver observer) {
        if (mObserver.contains(observer)) {
            mObserver.remove(observer);
        }
    }

    private void notifyContentObserver() {
        for (UsersRepositoryObserver observer : mObserver) {
            observer.onUserChanged();
        }
    }

    @NonNull
    @Override
    public List<User> getUsers() {
        List<User> users = null;

        if (!mCacheIsDirty) {
            if (mCachedUsers != null) {
                users = getCachedUsers();
                return users;
            } else {
                users = mUsersLocalDataSource.getUsers();
            }
        }

        if (users == null || users.isEmpty()) {
            users = mUsersRemoteDataSource.getUsers();

            try {
                saveUsersInLocalStorage(users);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        processLoadedUsers(users);

        return getCachedUsers();
    }

    public boolean cachedUserAvailable() {
        return mCachedUsers != null && !mCacheIsDirty;
    }

    public User getCachedUser(int userId) {
        return mCachedUsers.get(userId);
    }

    private void processLoadedUsers(List<User> users) {
        if (users == null) {
            mCachedUsers = null;
            mCacheIsDirty = false;
            return;
        }

        if (mCachedUsers == null) {
            mCachedUsers = new LinkedHashMap<>();
        }

        mCachedUsers.clear();

        for (User user : users) {
            mCachedUsers.put(user.getId(), user);
        }
        mCacheIsDirty = false;
    }

    private void saveUsersInLocalStorage(List<User> users) throws IOException {
        if (users != null) {
            for (User user : users) {
                mUsersLocalDataSource.saveUser(user);
            }
        }
    }

    @Nullable
    @Override
    public User getUser(int id) {
        User cachedUser = getCachedUser(id);

        if (cachedUser != null) {
            return cachedUser;
        }

        return mUsersLocalDataSource.getUser(id);
    }

    @Override
    public void editUser(@NonNull User user) {
        mUsersLocalDataSource.editUser(user);

        if (mCachedUsers == null) {
            mCachedUsers = new LinkedHashMap<>();
        }

        mCachedUsers.put(user.getId(), user);

        notifyContentObserver();
    }

    @Override
    public void refreshUsers() {
        mCacheIsDirty = true;
        notifyContentObserver();
    }

    @Override
    public void saveUser(@NonNull User user) {
        // No use
    }

    @Override
    public void deleteUser(@NonNull User user) {
        mUsersLocalDataSource.deleteUser(user);

        mCachedUsers.remove(user.getId());

        notifyContentObserver();
    }

    public List<User> getCachedUsers() {
        return mCachedUsers == null ? null : new ArrayList<>(mCachedUsers.values());
    }

    public interface UsersRepositoryObserver {
        void onUserChanged();
    }
}
