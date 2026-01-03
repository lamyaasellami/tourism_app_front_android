package com.example.projet_front.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;
import com.example.projet_front.adapters.EventAdapter;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.EventProvider;
import com.example.projet_front.utils.BottomNavBar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        // This makes the icons appear and work!
        //BottomNavBar.setupBottomNav(this);

        setupListeners();

        setupRecyclerView();

        fetchAllEvents();
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
        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private void fetchAllEvents() {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getAllEvents().enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<List<EventProvider>> call, @NonNull Response<List<EventProvider>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new EventAdapter(
                            EventsActivity.this,
                            response.body()
                    );
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<EventProvider>> call, @NonNull Throwable throwable) {
                Toast.makeText(EventsActivity.this,
                        "Failed to load events",
                        Toast.LENGTH_LONG
                ).show();

            }
        });
    }
}