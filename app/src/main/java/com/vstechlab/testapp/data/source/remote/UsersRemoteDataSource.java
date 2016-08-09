package com.vstechlab.testapp.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;
import com.vstechlab.testapp.data.User;
import com.vstechlab.testapp.data.source.UsersDataSource;
import com.vstechlab.testapp.data.UsersEntity;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class UsersRemoteDataSource implements UsersDataSource{

    private static UsersRemoteDataSource INSTANCE;

    private final UserApi mService;

    private UsersRemoteDataSource() {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        mService = retrofit.create(UserApi.class);
    }

    public static UsersRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersRemoteDataSource();
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public List<User> getUsers(){
        try {
            Call<UsersEntity> call = mService.getUsers();
            UsersEntity usersEntity = call.execute().body();
            return usersEntity.getUsers();

        } catch (IOException e) {
            // Todo: handle exception
        }
        return null;
    }

    @Nullable
    @Override
    public User getUser(int id) {
        // Not used
        return null;
    }

    @Override
    public void editUser(@NonNull User user) {
        // Not used
    }

    @Override
    public void refreshUsers() {
        // Not used
    }

    @Override
    public void saveUser(@NonNull User user) {
        // No use
    }

    @Override
    public void deleteUser(@NonNull User user) {
        // No use
    }
}
