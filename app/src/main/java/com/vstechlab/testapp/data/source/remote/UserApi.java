package com.vstechlab.testapp.data.source.remote;


import com.vstechlab.testapp.data.UsersEntity;

import retrofit.Call;
import retrofit.http.GET;

public interface UserApi {
    String BASE_URL = "http://demo7244483.mockable.io/";

    @GET("api/users")
    Call<UsersEntity> getUsers();
}
