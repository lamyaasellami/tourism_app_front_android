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
import com.example.projet_front.adapters.PopularPlaceAdapter;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.FavoritePlaceResponse;
import com.example.projet_front.models.PlaceResponse;
import com.example.projet_front.utils.BottomNavBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private PopularPlaceAdapter adapter;
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

        api.getFavoritesByUser(3).enqueue(new Callback<List<FavoritePlaceResponse>>() {
            @Override
            public void onResponse(
                    Call<List<FavoritePlaceResponse>> call,
                    Response<List<FavoritePlaceResponse>> response
            ) {

                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("FAVORITES", "Invalid response");
                    return;
                }

                List<FavoritePlaceResponse> favorites = response.body();
                Log.d("FAVORITES", "Count = " + favorites.size());

                // 🔁 MAP FavoritePlaceResponse -> PlaceResponse
                List<PlaceResponse> places = new ArrayList<>();

                for (FavoritePlaceResponse fav : favorites) {

                    PlaceResponse place = new PlaceResponse();
                    place.setPlaceId(fav.getPlaceId());
                    place.setName(fav.getName());
                    place.setPlaceType(fav.getPlaceType());
                    place.setDescription(fav.getDescription());
                    place.setMinPrice(fav.getMinPrice());
                    place.setMaxPrice(fav.getMaxPrice());

                    // VERY IMPORTANT
                    place.setFavorite(true);

                    places.add(place);
                }

                // 🧠 Adapter setup
                adapter = new PopularPlaceAdapter(requireContext(), places);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<FavoritePlaceResponse>> call, Throwable t) {
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
