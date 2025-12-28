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

        switch (position) {
            case 0: // All
                fetchAllAccommodations();
                break;

            case 1: // Riads
                fetchAccommodationsByType("riad");
                break;

            case 2: // Hotels
                fetchAccommodationsByType("hotel");
                break;

            case 3: // Platforms
                fetchAccommodationsByType("platform");
                break;

            case 4: // Luxury
                fetchAccommodationsByType("luxury");
                break;
        }
    }


    //----------
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewAccommodations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

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

    private void fetchAccommodationsByType(String type) {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getAccommodationsByType(type).enqueue(new Callback<List<AccommodationProvider>>() {
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
                else {
                    Toast.makeText(
                            HotelActivity.this,
                            "No accommodations found",
                            Toast.LENGTH_SHORT
                    ).show();
                    recyclerView.setAdapter(
                            new AccommodationAdapter(
                                    HotelActivity.this,
                                    new ArrayList<>()
                            )
                    );
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationProvider>> call, Throwable t) {
                Toast.makeText(
                        HotelActivity.this,
                        "Failed to load filtered accommodations",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
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
}