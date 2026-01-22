package com.example.projet_front.api;

import com.example.projet_front.models.AccommodationProvider;
import com.example.projet_front.models.EventProvider;
import com.example.projet_front.models.FavoritePlaceResponse;
import com.example.projet_front.models.FavoriteProvider;
import com.example.projet_front.models.FavoriteRequest;
import com.example.projet_front.models.TransportProvider;
import com.example.projet_front.models.UserLoginRequest;
import com.example.projet_front.models.UserRegisterRequest;
import com.example.projet_front.models.UserResponse;
import com.example.projet_front.models.PlaceResponse;
import com.example.projet_front.models.LoginResponse;
import com.example.projet_front.models.UserUpdateRequest;
import retrofit2.http.PUT;
import retrofit2.http.Header;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Path;
import retrofit2.http.Query;
import java.util.List;

public interface ApiService {

    // ================= USERS =================
    @POST("api/users/register")
    Call<UserResponse> register(@Body UserRegisterRequest request);

    @POST("api/users/login")
    Call<LoginResponse> login(@Body UserLoginRequest request);

    @GET("api/users/profile")
    Call<UserResponse> getProfile();

    @PUT("api/users/profile")
    Call<UserResponse> updateProfile(@Body UserUpdateRequest request);


    @POST("api/users/logout")
    Call<Void> logout();

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

    // 🔹 Filtre complet (une seule méthode !)
    /*@GET("api/places/filter")
    Call<List<PlaceResponse>> filterPlaces(
            @Query("q") String q,
            @Query("type") String type,
            @Query("minPrice") Double minPrice,
            @Query("maxPrice") Double maxPrice,
            @Query("opening") String opening
    );*/


    // ================= ACCOMMODATIONS =================
    // 🔹 GET ALL accommodations
    @GET("accommodations")
    Call<List<AccommodationProvider>> getAllAccommodations();

    // 🔹 GET accommodations by type
    @GET("accommodations/type/{type}")
    Call<List<AccommodationProvider>> getAccommodationsByType(
            @Path("type") String type
    );

    // ================= TRANSPORTATIONS =================
    // 🔹 GET ALL transportations
    @GET("transports")
    Call<List<TransportProvider>> getAllTransportations();

    // 🔹 GET transportations by type
    @GET("transports/type/{type}")
    Call<List<TransportProvider>> getTransportationsByType(
            @Path("type") String type
    );

    // ================= Events =================
    // 🔹 GET ALL events
    @GET("events")
    Call<List<EventProvider>> getAllEvents();

    @POST("chat")
    Call<ChatResponse> sendMessage(@Body ChatRequest request);



    //================= Favorites ===============

    @POST("favorites")
    Call<Void> addFavorite(@Body FavoriteRequest request);

    @HTTP(method = "DELETE", path = "favorites", hasBody = true)
    Call<Void> removeFavorite(@Body FavoriteRequest request);

    /*@GET("favorites/user/{userId}")
    Call<List<Integer>> getFavorites(@Path("userId") int userId);*/

    /*@GET("api/favorites/user/{userId}")
    Call<List<FavoritePlaceResponse>> getFavoritesByUser(
            @Path("userId") int userId
    );*/

    @GET("api/favorites/{userId}")
    Call<List<FavoriteProvider>> getFavoritesByUser(
            @Path("userId") long userId
    );
}
