package com.example.projet_front.api;

import com.example.projet_front.models.AccommodationProvider;
import com.example.projet_front.models.EventProvider;
import com.example.projet_front.models.TransportProvider;
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

    // Tous les lieux
    @GET("api/places")
    Call<List<PlaceResponse>> getAllPlaces();

    // Par type (POPULAIRES, Monument, etc.)
    @GET("api/places/type/{type}")
    Call<List<PlaceResponse>> getPlacesByType(@Path("type") String type);

    // 🔹 Filtre complet (une seule méthode !)
    @GET("api/places/filter")
    Call<List<PlaceResponse>> filterPlaces(
            @Query("q") String q,
            @Query("type") String type,
            @Query("minPrice") Double minPrice,
            @Query("maxPrice") Double maxPrice,
            @Query("opening") String opening
    );


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
}
