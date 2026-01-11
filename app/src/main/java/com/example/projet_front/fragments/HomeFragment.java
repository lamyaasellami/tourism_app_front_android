package com.example.projet_front.fragments;

import static android.app.PendingIntent.getActivity;

import com.example.projet_front.activities.EventsActivity;
import com.example.projet_front.activities.PlaceActivity;
import com.example.projet_front.activities.PlaceDetailActivity;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.PlaceResponse;
import com.example.projet_front.adapters.PopularPlaceAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    public HomeFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
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

        // 🌐 API CALL for POPULAR places
        fetchPopularPlaces();

        // 🏷️ CATEGORY CLICKS
        setupCategoryClicks(view);

        return view;
    }

    // ======================= FETCH POPULAR PLACES =======================
    private void fetchPopularPlaces() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.getPlacesByType("POPULAR")
                .enqueue(new Callback<List<PlaceResponse>>() {
                    @Override
                    public void onResponse(Call<List<PlaceResponse>> call,
                                           Response<List<PlaceResponse>> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(getContext(), "Loaded " + response.body().size() + " places", Toast.LENGTH_SHORT).show();

                            List<PlaceResponse> placeList = response.body();

                            PopularPlaceAdapter adapter =
                                    new PopularPlaceAdapter(placeList, place -> {
                                        if (getActivity() != null) {
                                            Intent intent = new Intent(
                                                    getActivity(),
                                                    PlaceDetailActivity.class
                                            );
                                            intent.putExtra("place_id", place.getPlaceId());
                                            startActivity(intent);
                                        }
                                    });

                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlaceResponse>> call, Throwable t) {
                        t.printStackTrace();
                        // Print full stack trace


                        // Log the error in Logcat with a tag
                        Log.e("API_ERROR", "Failed to load places", t);
                        if (isAdded()) {
                            Toast.makeText(getContext(),
                                    "Failed to load places" + t.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // ======================= CATEGORY CLICKS =======================
    private void setupCategoryClicks(View root) {
        View catEventsInclude = root.findViewById(R.id.cat_monuments);
        LinearLayout categoryEvents = catEventsInclude.findViewById(R.id.category_events);
        LinearLayout categoryPlaces = catEventsInclude.findViewById(R.id.category_places);
        LinearLayout categoryRestau = catEventsInclude.findViewById(R.id.category_restaurants);



        /*categoryEvents.setOnClickListener(v -> {
            if (getActivity() == null) return;

            FragmentManager fm = ((AppCompatActivity) getActivity())
                    .getSupportFragmentManager();

            fm.beginTransaction()
                    .replace(R.id.fragment_container, new EventsFragment())
                    .addToBackStack(null)
                    .commit();
        });*/

        categoryEvents.setOnClickListener(view -> {
            if (getActivity() == null) return;

            Intent intent = new Intent(getActivity(), EventsActivity.class);
            startActivity(intent);
        });
        categoryPlaces.setOnClickListener(v ->
            openAllPlaces("ALL"));

        categoryRestau.setOnClickListener(v ->
                openAllPlaces("Restauration"));
    }
    private void openAllPlaces(String type) {
        Context context = requireContext(); // throws if fragment not attached
        Intent intent = new Intent(context, PlaceActivity.class);
        intent.putExtra("PLACE_TYPE", type);
        startActivity(intent);
    }

    /*private void openAldlPlaces(String type) {
        Intent intent = new Intent(this, PlaceActivity.class);
        intent.putExtra("PLACE_TYPE", type);
        startActivity(intent);
    }*/

    // ======================= MAP =======================
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        LatLng maroc = new LatLng(31.7917, -7.0926);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maroc, 5f));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    // ======================= MAP LIFECYCLE =======================
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (getActivity() != null) {
            BottomNavBar.setupBottomNav(getActivity());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
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


