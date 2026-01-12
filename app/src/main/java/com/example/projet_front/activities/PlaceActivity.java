package com.example.projet_front.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;
import com.example.projet_front.adapters.PopularPlaceAdapter;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.PlaceResponse;
import com.example.projet_front.utils.BottomNavBar;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PopularPlaceAdapter adapter;
    private EditText searchBar;
    private List<PlaceResponse> placeList;

    // 🔹 Chips
    private Chip chipAll, chipPopular, chipMonument, chipMosquee, chipParc, chipRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_places);

        // 🔙 BACK
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // 📋 RECYCLER
        recyclerView = findViewById(R.id.recyclerViewPlaces);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        placeList = new ArrayList<>();
        adapter = new PopularPlaceAdapter(placeList);
        recyclerView.setAdapter(adapter);

        // 🔍 SEARCH
        searchBar = findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    fetchAllPlaces();
                    chipAll.setChecked(true);
                } else {
                    searchPlaces(s.toString());
                }
            }
        });

        // 🎯 INIT CHIPS
        initChips();

        // 🎯 FILTRE PAR INTENT
        String placeType = getIntent().getStringExtra("PLACE_TYPE");
        applyInitialFilter(placeType);

        // 🎯 FILTER CHIPS CLICK
        setupFilterChips();
    }

    // ================= INIT CHIPS =================

    private void initChips() {
        chipAll = findViewById(R.id.chipAll);
        chipPopular = findViewById(R.id.chipPopular);
        chipMonument = findViewById(R.id.chipMonument);
        chipMosquee = findViewById(R.id.chipMosquee);
        chipParc = findViewById(R.id.chipParc);
        chipRestaurant = findViewById(R.id.chipRestaurant);
    }

    private void applyInitialFilter(String type) {
        if (type == null) {
            chipAll.setChecked(true);
            fetchAllPlaces();
            return;
        }

        switch (type) {
            case "POPULAR":
                chipPopular.setChecked(true);
                fetchByType("POPULAR");
                break;

            case "Monument":
                chipMonument.setChecked(true);
                fetchByType("Monument");
                break;

            case "Mosquée":
                chipMosquee.setChecked(true);
                fetchByType("Mosquée");
                break;

            case "Parc":
                chipParc.setChecked(true);
                fetchByType("Parc");
                break;

            case "Restauration":
                chipRestaurant.setChecked(true);
                fetchByType("Restauration");
                break;

            default:
                chipAll.setChecked(true);
                fetchAllPlaces();
        }
    }

    // ================= API =================

    private void fetchAllPlaces() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getAllPlaces().enqueue(new Callback<List<PlaceResponse>>() {
            @Override
            public void onResponse(Call<List<PlaceResponse>> call,
                                   Response<List<PlaceResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<PlaceResponse>> call, Throwable t) {
                Log.e("API ERROR", t.getMessage());
            }
        });
    }

    private void fetchByType(String type) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getPlacesByType(type).enqueue(new Callback<List<PlaceResponse>>() {
            @Override
            public void onResponse(Call<List<PlaceResponse>> call,
                                   Response<List<PlaceResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<PlaceResponse>> call, Throwable t) {
                Log.e("API ERROR", t.getMessage());
            }
        });
    }

    private void searchPlaces(String keyword) {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.searchPlaces(keyword).enqueue(new Callback<List<PlaceResponse>>() {
            @Override
            public void onResponse(Call<List<PlaceResponse>> call,
                                   Response<List<PlaceResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<PlaceResponse>> call, Throwable t) {
                Log.e("API ERROR", t.getMessage());
            }
        });
    }

    // ================= FILTER CHIPS =================

    private void setupFilterChips() {
        chipAll.setOnClickListener(v -> fetchAllPlaces());
        chipPopular.setOnClickListener(v -> fetchByType("POPULAR"));
        chipMonument.setOnClickListener(v -> fetchByType("Monument"));
        chipMosquee.setOnClickListener(v -> fetchByType("Mosquée"));
        chipParc.setOnClickListener(v -> fetchByType("Parc"));
        chipRestaurant.setOnClickListener(v -> fetchByType("Restauration"));
    }
}
