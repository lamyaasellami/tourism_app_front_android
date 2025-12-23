package com.example.projet_front.activities;

import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.PlaceResponse;
import com.example.projet_front.adapters.PopularPlaceAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;
import com.example.projet_front.adapters.PopularPlaceAdapter;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.PlaceResponse;

import com.example.projet_front.utils.BottomNavBar;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private RecyclerView recyclerView;

    private Spinner priceSpinner;
    private Spinner timeSpinner;
    private Spinner typeSpinner;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 🗺️ MAP
        mapView = findViewById(R.id.mapView);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        // 🧭 BOTTOM NAV
        BottomNavBar.setupBottomNav(this);

        // 📋 RECYCLER VIEW
        recyclerView = findViewById(R.id.recycler_popular);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 🔹 SPINNERS
        typeSpinner  = findViewById(R.id.spinner_type);
        priceSpinner = findViewById(R.id.spinner_price);
        timeSpinner  = findViewById(R.id.spinner_time);

        // 🔹 SPINNER DATA
        String[] placeTypes = {
                "Tous",
                "POPULAR",
                "Monument",
                "Mosquée",
                "Restauration",
                "Shopping",
                "Divertissement",
                "Jardin",
                "Musée",
                "Marché",
                "Parc"
        };
        String[] prices = {"Tous les prix","Gratuit","0 - 50 DH","50 - 150 DH","150+ DH"};
        String[] openingTimes = {"Tous","Matin","Après-midi","Soir","Ouvert maintenant"};

        typeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, placeTypes));
        ((ArrayAdapter<?>) typeSpinner.getAdapter()).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        priceSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, prices));
        ((ArrayAdapter<?>) priceSpinner.getAdapter()).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, openingTimes));
        ((ArrayAdapter<?>) timeSpinner.getAdapter()).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 🔹 Listener pour le spinner de type
        typeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedType = typeSpinner.getSelectedItem().toString();
                if (selectedType.equals("Tous")) {
                    fetchAllPlaces();
                } else {
                    fetchPlacesByType(selectedType);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        // 🔹 Charger toutes les places au démarrage
        fetchAllPlaces();
    }

    // ======================= FETCH ALL PLACES =======================
    private void fetchAllPlaces() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<PlaceResponse>> call = apiService.getAllPlaces();

        call.enqueue(new Callback<List<PlaceResponse>>() {
            @Override
            public void onResponse(Call<List<PlaceResponse>> call, Response<List<PlaceResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerView.setAdapter(new PopularPlaceAdapter(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<PlaceResponse>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // ======================= FETCH PLACES BY TYPE =======================
    private void fetchPlacesByType(String type) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<PlaceResponse>> call = apiService.getPlacesByType(type);

        call.enqueue(new Callback<List<PlaceResponse>>() {
            @Override
            public void onResponse(Call<List<PlaceResponse>> call, Response<List<PlaceResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerView.setAdapter(new PopularPlaceAdapter(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<PlaceResponse>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // ======================= MAP =======================
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        LatLng maroc = new LatLng(31.7917, -7.0926);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maroc, 5f));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override protected void onResume() { super.onResume(); mapView.onResume(); }
    @Override protected void onPause() { super.onPause(); mapView.onPause(); }
    @Override protected void onDestroy() { super.onDestroy(); mapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
}
