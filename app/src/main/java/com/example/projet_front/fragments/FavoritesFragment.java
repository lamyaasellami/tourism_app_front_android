package com.example.projet_front.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;
import com.example.projet_front.adapters.FavoriteAdapter;
import com.example.projet_front.adapters.PopularPlaceAdapter;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.AccommodationProvider;
import com.example.projet_front.models.FavoritePlaceResponse;
import com.example.projet_front.models.FavoriteProvider;
import com.example.projet_front.models.PlaceResponse;
import com.example.projet_front.models.TransportProvider;
import com.example.projet_front.utils.BottomNavBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private ApiService api;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recyclerFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        api = ApiClient.getClient().create(ApiService.class);

        loadFavorites();

        return view;
    }

    private void loadFavorites() {

        api.getFavoritesByUser(3).enqueue(new Callback<List<FavoriteProvider>>() {
            @Override
            public void onResponse(
                    Call<List<FavoriteProvider>> call,
                    Response<List<FavoriteProvider>> response
            ) {

                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("FAVORITES", "Invalid response");
                    return;
                }

                List<FavoriteProvider> favorites = response.body();
                Log.d("FAVORITES", "Count = " + favorites.size());

                // 🔁 MAP FavoriteProvider -> PlaceResponse
                List<PlaceResponse> places = new ArrayList<>();
                List<AccommodationProvider> accs = new ArrayList<>();

                for (FavoriteProvider fav : favorites) {
                    if ("PLACE".equals(fav.getEntityType())) {
                        PlaceResponse place = fav.getPlace();
                        Log.d("FAVVA", "Place: " + place.getName());

                        /*place.setName(fav.getPlace().getName());
                        place.setDescription(fav.getPlace().getDescription());*/
                        place.setFavorite(true);
                        places.add(place);
                    } else if ("ACCOMMODATION".equals(fav.getEntityType())) {
                        AccommodationProvider acc = fav.getAccommodation();
                        acc.setFavorite(true);
                        accs.add(acc);
                    } /*else if ("TRANSPORT".equals(fav.getEntityType())) {
                        TransportProvider transport = fav.getTransport();
                    }*/
                }

                // 🧠 Adapter setup
                /*adapter = new PopularPlaceAdapter(requireContext(), places);
                recyclerView.setAdapter(adapter);*/
                adapter = new FavoriteAdapter(requireContext(), favorites);
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onFailure(Call<List<FavoriteProvider>> call, Throwable t) {
                Log.e("FAVORITES", "API error", t);
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
