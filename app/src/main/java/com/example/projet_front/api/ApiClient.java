package com.example.projet_front.api;

import com.example.projet_front.utils.Constants;
import com.example.projet_front.utils.TokenManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;
    private static OkHttpClient client;
    private static TokenManager tokenManager;

    // Initialisation avec TokenManager
    public static void init(TokenManager tm) {
        tokenManager = tm;

        client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder();

                    // Ajouter le token JWT si disponible
                    if (tokenManager != null && tokenManager.getToken() != null) {
                        requestBuilder.header("Authorization", "Bearer " + tokenManager.getToken());
                    }

                    return chain.proceed(requestBuilder.build());
                })
                .build();

        // Réinitialiser retrofit pour prendre en compte le nouveau client
        retrofit = null;
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Créer un client par défaut si init() n'a pas été appelé
            if (client == null) {
                client = new OkHttpClient.Builder().build();
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Réinitialiser après logout
    public static void resetClient() {
        retrofit = null;
        client = null;
        tokenManager = null;
    }
}