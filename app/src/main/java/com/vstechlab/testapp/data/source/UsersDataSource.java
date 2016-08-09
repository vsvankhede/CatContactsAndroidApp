package com.vstechlab.testapp.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vstechlab.testapp.data.User;

import java.util.List;

public interface UsersDataSource {
    interface GetUserCallback {
        void onUserLoaded(User user);
        void onDataNotAvailable();
    }


    @NonNull
    List<User> getUsers();

    @Nullable
    User getUser(int id);

    void editUser(@NonNull User user);

    void refreshUsers();

    void saveUser(@NonNull User user);

    void deleteUser(@NonNull User user);
}
