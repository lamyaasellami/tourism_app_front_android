package com.example.projet_front.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
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

public class HotelFragment extends Fragment {

    private RecyclerView recyclerView;
    private AccommodationAdapter adapter;

    public HotelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.activity_hotel, container, false);

        setupFilterChips(view);
        setupRecyclerView(view);
        setupListeners(view);

        fetchAllAccommodations();

        return view;
    }

    // ================= FILTER CHIPS =================
    private void setupFilterChips(View view) {

        ChipGroup chipGroup = view.findViewById(R.id.chipGroupFilters);

        if (chipGroup != null) {
            chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {

                if (checkedIds.isEmpty()) return;

                int chipId = checkedIds.get(0);
                Chip selectedChip = view.findViewById(chipId);

                if (selectedChip != null) {
                    int position = group.indexOfChild(selectedChip);
                    filterHotels(selectedChip.getText().toString(), position);
                }
            });
        }
    }

    private void filterHotels(String category, int position) {

        switch (position) {
            case 0:
                fetchAllAccommodations();
                break;

            case 1:
                fetchAccommodationsByType("riad");
                break;

            case 2:
                fetchAccommodationsByType("hotel");
                break;

            case 3:
                fetchAccommodationsByType("platform");
                break;

            case 4:
                fetchAccommodationsByType("luxury");
                break;
        }
    }

    // ================= RECYCLER VIEW =================
    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewAccommodations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    // ================= API CALLS =================
    private void fetchAllAccommodations() {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getAllAccommodations().enqueue(new Callback<List<AccommodationProvider>>() {
            @Override
            public void onResponse(
                    Call<List<AccommodationProvider>> call,
                    Response<List<AccommodationProvider>> response
            ) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    adapter = new AccommodationAdapter(
                            requireContext(),
                            response.body()
                    );
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationProvider>> call, Throwable t) {
                if (isAdded()) {
                    Toast.makeText(
                            requireContext(),
                            "Failed to load accommodations",
                            Toast.LENGTH_LONG
                    ).show();
                }
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
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    adapter = new AccommodationAdapter(
                            requireContext(),
                            response.body()
                    );
                } else {
                    Toast.makeText(
                            requireContext(),
                            "No accommodations found",
                            Toast.LENGTH_SHORT
                    ).show();

                    adapter = new AccommodationAdapter(
                            requireContext(),
                            new ArrayList<>()
                    );
                }

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<AccommodationProvider>> call, Throwable t) {
                if (isAdded()) {
                    Toast.makeText(
                            requireContext(),
                            "Failed to load filtered accommodations",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }

    // ================= LISTENERS =================
    private void setupListeners(View view) {

        ImageView btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }

    // ================= BOTTOM NAV REFRESH =================
    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            BottomNavBar.setupBottomNav(getActivity());
        }
    }
}
