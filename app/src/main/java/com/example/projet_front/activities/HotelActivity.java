package com.example.projet_front.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;
import com.example.projet_front.adapters.AccommodationAdapter;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.AccommodationProvider;
import com.example.projet_front.utils.BottomNavBar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AccommodationAdapter adapter;
    private List<AccommodationProvider> accommodations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        // This makes the icons appear and work!
        BottomNavBar.setupBottomNav(this);

        setupFilterChips();

        setupListeners();

        setupRecyclerView();
        //loadAccommodations();
        //setupFilterChips();
        fetchAllAccommodations();
    }

    private void setupFilterChips() {
        // 1. Find the ChipGroup inside your activity
        // Note: Since you used <include>, the ID 'chipGroupFilters' is directly accessible
        ChipGroup chipGroup = findViewById(R.id.chipGroupFilters);

        if (chipGroup != null) {
            // 2. Set the listener for selection changes
            chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
                if (checkedIds.isEmpty()) return;

                // 3. Get the selected Chip
                int chipId = checkedIds.get(0);
                Chip selectedChip = findViewById(chipId);

                if (selectedChip != null) {
                    String category = selectedChip.getText().toString();

                    // Get position/index of the chip in the group
                    int position = group.indexOfChild(selectedChip);

                    // 4. Trigger your filtering logic
                    filterHotels(category, position);
                }
            });
        }
    }

    private void filterHotels(String category, int position) {
        // Implement your filtering logic here
        // For example: update RecyclerView adapter with filtered data
        switch (position) {
            case 0: // All
                // Show all hotels
                break;
            case 1: // 5 Star
                // Show 5 star hotels
                break;
            case 2: // 4 Star
                // Show 4 star hotels
                break;
            case 3: // Budget
                // Show budget hotels
                break;
            case 4: // Luxury
                // Show luxury hotels
                break;
        }

        // Show a loading spinner here
        // progressBar.setVisibility(View.VISIBLE);

        // This is where you will call your database/API
        // Example logic:
        /*if (category.equals("All")) {
            fetchAllHotelsFromDB();
        } else {
            // Pass the category to your database query helper
            fetchHotelsByRating(category);
        }*/
    }

    private void setupListeners() {
        // 1. Find the back button by its ID
        ImageView btnBack = findViewById(R.id.btnBack);

        // 2. Set the click listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3. Close this activity to go back
                finish();
            }
        });
    }

    //----------
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewAccommodations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    /*private void loadAccommodations() {
        // Sample data based on your SQL
        accommodations = new ArrayList<>();
        accommodations.add(new AccommodationProvider(
                1,
                "Riad La Sultana",
                "riad",
                "Luxury riad near Jamaâ El Fna Square with Moorish décor, rooftop terrace, spa, and marble bathrooms.",
                "https://www.lasultanahotels.com",
                null
        ));
        accommodations.add(new AccommodationProvider(
                2,
                "Riad Yasmine",
                "riad",
                "Instagram-famous boutique riad with a stunning courtyard pool, Moroccan tiles, and personalized service.",
                "https://www.riadyasmine.com",
                null
        ));

        adapter = new AccommodationAdapter(this, accommodations);
        recyclerView.setAdapter(adapter);
    }*/

    private void fetchAllAccommodations() {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getAllAccommodations().enqueue(new Callback<List<AccommodationProvider>>() {

            @Override
            public void onResponse(
                    Call<List<AccommodationProvider>> call,
                    Response<List<AccommodationProvider>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new AccommodationAdapter(
                            HotelActivity.this,
                            response.body()
                    );
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationProvider>> call, Throwable t) {
                Toast.makeText(
                        HotelActivity.this,
                        "Failed to load accommodations",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }


    /*private void setupFilterChips() {
        FilterChips filterFragment = (FilterChips) getSupportFragmentManager()
                .findFragmentById(R.id.filterChipsContainer);

        if (filterFragment != null) {
            filterFragment.setOnFilterSelectedListener((filterCategory, position) -> {
                adapter.filter(filterCategory);
            });
        }
    }*/
}