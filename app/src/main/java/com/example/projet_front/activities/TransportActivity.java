package com.example.projet_front.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;
import com.example.projet_front.adapters.TransportAdapter;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.TransportProvider;
import com.example.projet_front.utils.BottomNavBar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        // This makes the icons appear and work!
        BottomNavBar.setupBottomNav(this);

        setupFilterChips();

        setupListeners();

        setupRecyclerView();

        fetchAllTransportations();
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

                    // Get position/index of the chip in the group
                    int position = group.indexOfChild(selectedChip);

                    // 4. Trigger your filtering logic
                    filterHotels(position);
                }
            });
        }
    }

    private void filterHotels(int position) {

        switch (position) {
            case 0: // All
                fetchAllTransportations();
                break;

            case 1: // Train
                fetchTransportationsByType("train");
                break;

            case 2: // Tram
                fetchTransportationsByType("tram");
                break;

            case 3: // Uber
                fetchTransportationsByType("bus");
                break;

            case 4: // other
                fetchTransportationsByType("other");
                break;
        }
    }


    //----------
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewTransportations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private void fetchAllTransportations() {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getAllTransportations().enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<List<TransportProvider>> call, @NonNull Response<List<TransportProvider>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new TransportAdapter(
                            TransportActivity.this,
                            response.body()
                    );
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TransportProvider>> call, @NonNull Throwable throwable) {
                Toast.makeText(TransportActivity.this,
                        "Failed to load transportations",
                        Toast.LENGTH_LONG
                ).show();

            }
        });
    }


    private void fetchTransportationsByType(String type) {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getTransportationsByType(type).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<TransportProvider>> call,
                    @NonNull Response<List<TransportProvider>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new TransportAdapter(
                            TransportActivity.this,
                            response.body()
                    );
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(
                            TransportActivity.this,
                            "No means of transport found",
                            Toast.LENGTH_SHORT
                    ).show();
                    recyclerView.setAdapter(
                            new TransportAdapter(
                                    TransportActivity.this,
                                    new ArrayList<>()
                            )
                    );
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TransportProvider>> call, @NonNull Throwable t) {
                Toast.makeText(
                        TransportActivity.this,
                        "Failed to load filtered transport results",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}