package com.example.projet_front.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.projet_front.R;
import com.example.projet_front.utils.BottomNavBar;
import com.example.projet_front.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavBar.setupBottomNav(this);

        // Default fragment
        loadFragment(new HomeFragment());

        findViewById(R.id.nav_accueil).setOnClickListener(v -> {
            loadFragment(new HomeFragment());
        });
    }



    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}