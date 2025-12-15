package com.example.projet_front.api;

import com.example.projet_front.models.UserLoginRequest;
import com.example.projet_front.models.UserRegisterRequest;
import com.example.projet_front.models.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/users/register")
    Call<UserResponse> register(@Body UserRegisterRequest request);

    @POST("api/users/login")
    Call<UserResponse> login(@Body UserLoginRequest request);
}
