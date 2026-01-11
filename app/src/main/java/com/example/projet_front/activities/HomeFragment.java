package com.example.projet_front.activities;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;

import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.PlaceResponse;
import com.example.projet_front.adapters.PopularPlaceAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;

import com.example.projet_front.utils.BottomNavBar;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private RecyclerView recyclerView;

    private Spinner priceSpinner;
    private Spinner timeSpinner;
    private Spinner typeSpinner;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_home, container, false);

        // 🗺️ MAP
        mapView = view.findViewById(R.id.mapView);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        // 📋 RECYCLER VIEW
        recyclerView = view.findViewById(R.id.recycler_popular);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 🔹 SPINNERS
        typeSpinner  = view.findViewById(R.id.spinner_type);
        priceSpinner = view.findViewById(R.id.spinner_price);
        timeSpinner  = view.findViewById(R.id.spinner_time);

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

        typeSpinner.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                placeTypes
        ));
        ((ArrayAdapter<?>) typeSpinner.getAdapter())
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        priceSpinner.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                prices
        ));
        ((ArrayAdapter<?>) priceSpinner.getAdapter())
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timeSpinner.setAdapter(new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                openingTimes
        ));
        ((ArrayAdapter<?>) timeSpinner.getAdapter())
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 🔹 TYPE FILTER
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = typeSpinner.getSelectedItem().toString();
                if (selectedType.equals("Tous")) {
                    fetchAllPlaces();
                } else {
                    fetchPlacesByType(selectedType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        fetchAllPlaces();
        setupCategoryClicks(view);

        return view;
    }


    private void setupCategoryClicks(View root) {
        /*View catEventsInclude = root.findViewById(R.id.cat_monuments);
        LinearLayout categoryEvents =
                catEventsInclude.findViewById(R.id.category_events);

        categoryEvents.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EventsFragment.class);
            startActivity(intent);
        });*/
        View catEventsInclude = root.findViewById(R.id.cat_monuments);
        LinearLayout categoryEvents =
                catEventsInclude.findViewById(R.id.category_events);

        categoryEvents.setOnClickListener(v -> {

            if (getActivity() == null) return;

            FragmentManager fm =
                    ((AppCompatActivity) getActivity()).getSupportFragmentManager();

            fm.beginTransaction()
                    .replace(R.id.fragment_container, new EventsFragment())
                    .addToBackStack(null) // ✅ allows back button
                    .commit();
        });
    }

    // ======================= FETCH ALL PLACES =======================
    private void fetchAllPlaces() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<PlaceResponse>> call = apiService.getAllPlaces();

        call.enqueue(new Callback<List<PlaceResponse>>() {
            @Override
            public void onResponse(Call<List<PlaceResponse>> call,
                                   Response<List<PlaceResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerView.setAdapter(
                            new PopularPlaceAdapter(response.body())
                    );
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
            public void onResponse(Call<List<PlaceResponse>> call,
                                   Response<List<PlaceResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerView.setAdapter(
                            new PopularPlaceAdapter(response.body())
                    );
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

    // ======================= MAP LIFECYCLE =======================
    @Override public void onResume() {
        super.onResume();
        mapView.onResume();

        if (getActivity() != null) {
            BottomNavBar.setupBottomNav(getActivity());
        }
    }

    @Override public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
}

