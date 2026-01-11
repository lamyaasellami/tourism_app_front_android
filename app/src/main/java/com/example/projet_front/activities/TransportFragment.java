package com.example.projet_front.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

public class TransportFragment extends Fragment {

    private RecyclerView recyclerView;
    private TransportAdapter adapter;

    public TransportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.activity_transport, container, false);

        setupFilterChips(view);
        setupRecyclerView(view);
        setupListeners(view);

        fetchAllTransportations();

        return view;
    }

    // ================= LISTENERS =================
    private void setupListeners(View view) {

        ImageView btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
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
                    filterTransportations(position);
                }
            });
        }
    }

    private void filterTransportations(int position) {

        switch (position) {
            case 0:
                fetchAllTransportations();
                break;

            case 1:
                fetchTransportationsByType("train");
                break;

            case 2:
                fetchTransportationsByType("tram");
                break;

            case 3:
                fetchTransportationsByType("bus");
                break;

            case 4:
                fetchTransportationsByType("other");
                break;
        }
    }

    // ================= RECYCLER VIEW =================
    private void setupRecyclerView(View view) {

        recyclerView = view.findViewById(R.id.recyclerViewTransportations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    // ================= API CALLS =================
    private void fetchAllTransportations() {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getAllTransportations().enqueue(new Callback<List<TransportProvider>>() {

            @Override
            public void onResponse(
                    @NonNull Call<List<TransportProvider>> call,
                    @NonNull Response<List<TransportProvider>> response
            ) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    adapter = new TransportAdapter(
                            requireContext(),
                            response.body()
                    );
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<TransportProvider>> call,
                    @NonNull Throwable throwable
            ) {
                if (isAdded()) {
                    Toast.makeText(
                            requireContext(),
                            "Failed to load transportations",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }

    private void fetchTransportationsByType(String type) {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getTransportationsByType(type).enqueue(new Callback<List<TransportProvider>>() {

            @Override
            public void onResponse(
                    @NonNull Call<List<TransportProvider>> call,
                    @NonNull Response<List<TransportProvider>> response
            ) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    adapter = new TransportAdapter(
                            requireContext(),
                            response.body()
                    );
                } else {
                    Toast.makeText(
                            requireContext(),
                            "No means of transport found",
                            Toast.LENGTH_SHORT
                    ).show();

                    adapter = new TransportAdapter(
                            requireContext(),
                            new ArrayList<>()
                    );
                }

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<TransportProvider>> call,
                    @NonNull Throwable t
            ) {
                if (isAdded()) {
                    Toast.makeText(
                            requireContext(),
                            "Failed to load filtered transport results",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
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
