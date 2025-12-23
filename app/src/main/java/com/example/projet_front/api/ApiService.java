package com.example.projet_front.api;

import com.example.projet_front.models.UserLoginRequest;
import com.example.projet_front.models.UserRegisterRequest;
import com.example.projet_front.models.UserResponse;
import com.example.projet_front.models.PlaceResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import java.util.List;

public interface ApiService {

    // ================= USERS =================
    @POST("api/users/register")
    Call<UserResponse> register(@Body UserRegisterRequest request);

    @POST("api/users/login")
    Call<UserResponse> login(@Body UserLoginRequest request);


    // ================= PLACES =================

    // 🔹 Tous les lieux
    @GET("api/places")
    Call<List<PlaceResponse>> getAllPlaces();

    // 🔹 Par catégorie
    @GET("api/places/type/{type}")
    Call<List<PlaceResponse>> getPlacesByType(@Path("type") String type);

    // 🔹 Recherche avec filtres
    @GET("api/places/search")
    Call<List<PlaceResponse>> searchPlaces(@Query("q") String keyword);

    @GET("api/places/{id}")
    Call<PlaceResponse> getPlaceById(@Path("id") Integer id);

}
