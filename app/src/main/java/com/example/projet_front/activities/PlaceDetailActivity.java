package com.example.projet_front.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_front.R;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.PlaceResponse;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tvName, tvType, tvDescription, tvOpening, tvPrice, tvPhone, tvWebsite;
    private MapView mapView;
    private GoogleMap googleMap;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private PlaceResponse place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        // ========================
        // LIENS VERS LES VUES
        // ========================
        tvName = findViewById(R.id.tv_place_name);
        tvType = findViewById(R.id.tv_place_type);
        tvDescription = findViewById(R.id.tv_description);
        tvOpening = findViewById(R.id.tv_opening_hours);
        tvPrice = findViewById(R.id.tv_price);
        tvPhone = findViewById(R.id.tv_phone);
        tvWebsite = findViewById(R.id.tv_website);
        mapView = findViewById(R.id.mapView);

        // ========================
        // MAP
        // ========================
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        // ========================
        // Bottom Nav
        // ========================
//        setupBottomNav();

        // ========================
        // RÉCUPÉRATION DE L'ID DE LA PLACE
        // ========================
        int placeId = getIntent().getIntExtra("place_id", -1);
        if (placeId == -1) {
            finish(); // Pas d'ID, on ferme l'activité
            return;
        }

        // ========================
        // APPEL API POUR RÉCUPÉRER LES INFOS
        // ========================
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getPlaceById(placeId).enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    place = response.body();
                    fillUI();
                }
            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // ========================
    // REMPLIR L'UI APRÈS RÉCUPÉRATION
    // ========================
    private void fillUI() {
        if (place == null) return;

        tvName.setText(place.getName());
        tvType.setText(place.getPlaceType());
        tvDescription.setText(place.getDescription() != null ? place.getDescription() : "Aucune description");
        tvOpening.setText(place.getOpeningHours());
        tvPrice.setText("Min: " + place.getMinPrice() + "€ | Max: " + place.getMaxPrice() + "€");
        tvPhone.setText(place.getPhone());
        tvWebsite.setText(place.getWebsiteUrl());

        tvPhone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + place.getPhone()));
            startActivity(intent);
        });

        tvWebsite.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(place.getWebsiteUrl()));
            startActivity(intent);
        });

        // Ajouter le marker sur la map si la carte est prête
        if (googleMap != null) {
            LatLng pos = new LatLng(place.getLatitude(), place.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(pos).title(place.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f));
        }
    }

    // ========================
    // BOTTOM NAV
    // ========================
    private void setupBottomNav() {
        setNavItem(findViewById(R.id.nav_accueil), R.drawable.ic_accueil, "Accueil", false);
        setNavItem(findViewById(R.id.nav_hotels), R.drawable.ic_hotel, "Hôtels", false);
        setNavItem(findViewById(R.id.nav_transport), R.drawable.ic_transport, "Transport", false);
        setNavItem(findViewById(R.id.nav_favoris), R.drawable.ic_favorite, "Favoris", false);
        setNavItem(findViewById(R.id.nav_profil), R.drawable.ic_profile, "Profil", true);
    }

    private void setNavItem(View item, int icon, String text, boolean active) {
        ImageView iconView = item.findViewById(R.id.nav_icon);
        TextView textView = item.findViewById(R.id.nav_text);

        iconView.setImageResource(icon);
        int color = getResources().getColor(active ? R.color.nav_active : R.color.nav_inactive);
        iconView.setColorFilter(color);
        textView.setText(text);
        textView.setTextColor(color);
    }

    // ========================
    // MAP READY
    // ========================
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Ajouter marker si les données sont déjà chargées
        if (place != null) {
            LatLng pos = new LatLng(place.getLatitude(), place.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(pos).title(place.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f));
        }
    }

    // ========================
    // MAP LIFECYCLE
    // ========================
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
