package com.example.projet_front.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;
import com.example.projet_front.adapters.PopularPlaceAdapter;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.PlaceResponse;
import com.example.projet_front.utils.BottomNavBar;
import com.google.android.gms.maps.*;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private RecyclerView recyclerView;
    private PopularPlaceAdapter adapter;
    private MapView mapView;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 🗺️ MAP
        mapView = findViewById(R.id.mapView);
        Bundle mapViewBundle = savedInstanceState != null
                ? savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
                : null;
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        // 🧭 Bottom nav
        BottomNavBar.setupBottomNav(this);

        // 📋 RecyclerView POPULAR
        recyclerView = findViewById(R.id.recycler_popular);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PopularPlaceAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // 👉 Voir tout (ALL)
        TextView voirTout = findViewById(R.id.tv_voir_tout);
        voirTout.setOnClickListener(v -> openAllPlaces("ALL"));

        // 👉 Icône MONUMENTS / PLACES
        ImageView iconPlaces = findViewById(R.id.category_icon_places);
        iconPlaces.setOnClickListener(v -> openAllPlaces("ALL"));

        // 👉 Icône RESTAURANTS
        ImageView iconRestaurants = findViewById(R.id.category_icon_restaurants);
        iconRestaurants.setOnClickListener(v -> openAllPlaces("Restauration"));

        fetchPopularPlaces();
    }

    private void openAllPlaces(String type) {
        Intent intent = new Intent(this, PlaceActivity.class);
        intent.putExtra("PLACE_TYPE", type);
        startActivity(intent);
    }

    // ================= API =================
    private void fetchPopularPlaces() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getPlacesByType("POPULAR").enqueue(new Callback<List<PlaceResponse>>() {
            @Override
            public void onResponse(Call<List<PlaceResponse>> call, Response<List<PlaceResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<PlaceResponse>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // ================= MAP =================
    @Override
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new com.google.android.gms.maps.model.LatLng(31.7917, -7.0926), 5f
        ));
    }

    @Override protected void onResume() { super.onResume(); mapView.onResume(); }
    @Override protected void onPause() { super.onPause(); mapView.onPause(); }
    @Override protected void onDestroy() { super.onDestroy(); mapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
}
