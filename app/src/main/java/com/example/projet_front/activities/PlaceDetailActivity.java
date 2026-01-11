package com.example.projet_front.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private ProgressBar progressBar;
    private View contentLayout;
    private View mapOverlay;
    private Button btnOpenMaps;
    private ImageView btnBack;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private PlaceResponse place;
    private boolean isMapReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        // ========================
        // LIENS VERS LES VUES
        // ========================
        initViews();

        // ========================
        // BACK BUTTON
        // ========================
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // ========================
        // RÉCUPÉRATION DE L'ID
        // ========================
        int placeId = getIntent().getIntExtra("place_id", -1);
        if (placeId == -1) {
            Toast.makeText(this, "Erreur : ID de lieu invalide", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ========================
        // CHARGEMENT DES DONNÉES (PRIORITAIRE)
        // ========================
        loadPlaceData(placeId);

        // ========================
        // MAP - CHARGEMENT DIFFÉRÉ (500ms après)
        // ========================
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            initMap(savedInstanceState);
        }, 500);
    }

    private void initViews() {
        tvName = findViewById(R.id.tv_place_name);
        tvType = findViewById(R.id.tv_place_type);
        tvDescription = findViewById(R.id.tv_description);
        tvOpening = findViewById(R.id.tv_opening_hours);
        tvPrice = findViewById(R.id.tv_price);
        tvPhone = findViewById(R.id.tv_phone);
        tvWebsite = findViewById(R.id.tv_website);

        progressBar = findViewById(R.id.progress_bar);
        contentLayout = findViewById(R.id.content_layout);
        mapView = findViewById(R.id.mapView);
        mapOverlay = findViewById(R.id.map_overlay);
        btnOpenMaps = findViewById(R.id.btn_open_maps);
        btnBack = findViewById(R.id.btn_back);
    }

    private void initMap(Bundle savedInstanceState) {
        if (mapView != null) {
            try {
                Bundle mapViewBundle = null;
                if (savedInstanceState != null) {
                    mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
                }
                mapView.onCreate(mapViewBundle);
                mapView.getMapAsync(this);

                // Gestion du overlay pour éviter les conflits de scroll
                if (mapOverlay != null) {
                    mapOverlay.setOnClickListener(v -> {
                        // Désactiver l'overlay au premier clic
                        mapOverlay.setVisibility(View.GONE);
                        Toast.makeText(this, "Vous pouvez maintenant interagir avec la carte", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur lors du chargement de la carte", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ========================
    // CHARGEMENT DES DONNÉES
    // ========================
    private void loadPlaceData(int placeId) {
        showLoading(true);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Timeout de 10 secondes
        Call<PlaceResponse> call = apiService.getPlaceById(placeId);

        call.enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    place = response.body();
                    fillUI();
                } else {
                    Toast.makeText(PlaceDetailActivity.this,
                            "Erreur : impossible de charger les détails (code " + response.code() + ")",
                            Toast.LENGTH_SHORT).show();

                    // Retour après 2 secondes
                    new Handler(Looper.getMainLooper()).postDelayed(() -> finish(), 2000);
                }
            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                showLoading(false);
                t.printStackTrace();

                String errorMsg = "Erreur réseau";
                if (t.getMessage() != null) {
                    errorMsg += ": " + t.getMessage();
                }

                Toast.makeText(PlaceDetailActivity.this, errorMsg, Toast.LENGTH_LONG).show();

                // Retour après 2 secondes
                new Handler(Looper.getMainLooper()).postDelayed(() -> finish(), 2000);
            }
        });
    }

    // ========================
    // GESTION DU LOADER
    // ========================
    private void showLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        if (contentLayout != null) {
            contentLayout.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        }
    }

    // ========================
    // REMPLIR L'UI
    // ========================
    private void fillUI() {
        if (place == null) return;

        tvName.setText(place.getName());
        tvType.setText(place.getPlaceType() != null ? place.getPlaceType() : "");
        tvDescription.setText(place.getDescription() != null ? place.getDescription() : "Aucune description disponible");
        tvOpening.setText(place.getOpeningHours() != null ? place.getOpeningHours() : "Horaires non renseignés");

        String priceText = "Min: " + place.getMinPrice() + "€ | Max: " + place.getMaxPrice() + "€";
        tvPrice.setText(priceText);

        // Téléphone
        if (place.getPhone() != null && !place.getPhone().isEmpty()) {
            tvPhone.setText(place.getPhone());
            tvPhone.setOnClickListener(v -> callPhone(place.getPhone()));
        } else {
            tvPhone.setText("Non renseigné");
            tvPhone.setClickable(false);
        }

        // Website
        if (place.getWebsiteUrl() != null && !place.getWebsiteUrl().isEmpty()) {
            tvWebsite.setText(place.getWebsiteUrl());
            tvWebsite.setOnClickListener(v -> openWebsite(place.getWebsiteUrl()));
        } else {
            tvWebsite.setText("Non renseigné");
            tvWebsite.setClickable(false);
        }

        // Bouton Google Maps
        if (btnOpenMaps != null) {
            btnOpenMaps.setOnClickListener(v -> openInGoogleMaps());
        }

        // Mettre à jour la carte si prête
        if (isMapReady) {
            updateMapMarker();
        }
    }

    // ========================
    // ACTIONS
    // ========================
    private void callPhone(String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Impossible d'ouvrir le téléphone", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWebsite(String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Impossible d'ouvrir le site web", Toast.LENGTH_SHORT).show();
        }
    }

    private void openInGoogleMaps() {
        if (place == null) return;

        try {
            Uri gmmIntentUri = Uri.parse("geo:" + place.getLatitude() + "," + place.getLongitude() + "?q=" + place.getLatitude() + "," + place.getLongitude() + "(" + place.getName() + ")");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Ouvrir dans le navigateur si Google Maps n'est pas installé
                String url = "https://www.google.com/maps/search/?api=1&query=" + place.getLatitude() + "," + place.getLongitude();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Impossible d'ouvrir Google Maps", Toast.LENGTH_SHORT).show();
        }
    }

    // ========================
    // MAP MARKER
    // ========================
    private void updateMapMarker() {
        if (googleMap != null && place != null) {
            try {
                LatLng pos = new LatLng(place.getLatitude(), place.getLongitude());
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(place.getName()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ========================
    // MAP READY
    // ========================
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        isMapReady = true;

        try {
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setScrollGesturesEnabled(true);

            // Ajouter marker si les données sont déjà chargées
            if (place != null) {
                updateMapMarker();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========================
    // MAP LIFECYCLE - AVEC PROTECTION
    // ========================
    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (mapView != null) mapView.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mapView != null) mapView.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mapView != null) mapView.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            if (mapView != null) mapView.onLowMemory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            if (mapView != null) {
                Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
                if (mapViewBundle == null) {
                    mapViewBundle = new Bundle();
                    outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
                }
                mapView.onSaveInstanceState(mapViewBundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}