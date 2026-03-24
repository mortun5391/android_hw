package com.khalilbek.hw9.api;

import com.khalilbek.hw9.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserApi {
    @GET("users")
    Call<List<User>> getUsers();
}
