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

import com.example.projet_front.R;
import com.example.projet_front.utils.BottomNavBar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class HotelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        // This makes the icons appear and work!
        BottomNavBar.setupBottomNav(this);

        setupFilterChips();

        setupListeners();
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
}