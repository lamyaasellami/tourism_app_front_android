package com.example.projet_front.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_front.R;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;

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

        // 👇 BOTTOM NAV (AJOUTÉ)
        setupBottomNav();
    }

    // =======================
    // 🧭 BOTTOM NAVIGATION
    // =======================
    private void setupBottomNav() {

        setNavItem(
                findViewById(R.id.nav_accueil),
                R.drawable.ic_accueil,
                "Accueil",
                true
        );

        setNavItem(findViewById(R.id.nav_hotels), R.drawable.ic_hotel, "Hôtels", false);
        setNavItem(findViewById(R.id.nav_transport), R.drawable.ic_transport, "Transport", false);
        setNavItem(findViewById(R.id.nav_favoris), R.drawable.ic_favorite, "Favoris", false);
        setNavItem(findViewById(R.id.nav_profil), R.drawable.ic_profile, "Profil", false);
    }

    private void setNavItem(View item, int icon, String text, boolean active) {

        ImageView iconView = item.findViewById(R.id.nav_icon);
        TextView textView = item.findViewById(R.id.nav_text);

        iconView.setImageResource(icon);

        int color = getResources().getColor(
                active ? R.color.nav_active : R.color.nav_inactive
        );

        iconView.setColorFilter(color);
        textView.setText(text);
        textView.setTextColor(color);
    }

    // =======================
    // 🗺️ MAP CALLBACK
    // =======================
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // 📍 Centre du Maroc
        LatLng maroc = new LatLng(31.7917, -7.0926);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maroc, 5f));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    // =======================
    // 🔄 MAP LIFECYCLE
    // =======================
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

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
