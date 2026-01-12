package com.example.projet_front.utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.projet_front.R;
import com.example.projet_front.fragments.HomeFragment;
import com.example.projet_front.fragments.HotelFragment;
import com.example.projet_front.fragments.ProfileFragment;
import com.example.projet_front.fragments.TransportFragment;

public class BottomNavBar {

    public static void setupBottomNav(Activity activity) {
        // 1. Find the parent views
        View navAccueil = activity.findViewById(R.id.nav_accueil);
        View navHotels = activity.findViewById(R.id.nav_hotels);
        View navTransport = activity.findViewById(R.id.nav_transport);
        View navFavoris = activity.findViewById(R.id.nav_favoris);
        View navProfil = activity.findViewById(R.id.nav_profil);

        // 2. Set the items (Logic based on which Activity is calling this)
        setNavItem(activity, navAccueil, R.drawable.ic_accueil, "Accueil", isCurrentFragment(activity, HomeFragment.class));
        setNavItem(activity, navHotels, R.drawable.ic_hotel, "Hôtels", isCurrentFragment(activity, HotelFragment.class));
        setNavItem(activity, navTransport, R.drawable.ic_transport, "Transport", isCurrentFragment(activity, TransportFragment.class)); // FIX: Change to appropriate Activity
        setNavItem(activity, navFavoris, R.drawable.ic_favorite, "Favoris", false);
        setNavItem(activity, navProfil, R.drawable.ic_profile, "Profil", isCurrentFragment(activity, ProfileFragment.class));

        // 3. Click Listeners
        // ✅ HOME (Fragment)
        navAccueil.setOnClickListener(v -> {
            if (!isCurrentFragment(activity, HomeFragment.class)) {

                FragmentManager fm =
                        ((AppCompatActivity) activity).getSupportFragmentManager();

                fm.beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }
        });
        navHotels.setOnClickListener(v -> {;
            if (!isCurrentFragment(activity, HotelFragment.class)) {

                FragmentManager fm =
                        ((AppCompatActivity) activity).getSupportFragmentManager();

                fm.beginTransaction()
                        .replace(R.id.fragment_container, new HotelFragment())
                        .addToBackStack(null) // 👈 VERY IMPORTANT
                        .commit();
            }
        });

        navTransport.setOnClickListener(v -> {;
            if (!isCurrentFragment(activity, TransportFragment.class)) {

                FragmentManager fm =
                        ((AppCompatActivity) activity).getSupportFragmentManager();

                fm.beginTransaction()
                        .replace(R.id.fragment_container, new TransportFragment())
                        .addToBackStack(null) // 👈 VERY IMPORTANT
                        .commit();
            }
        });

        navProfil.setOnClickListener(v -> {
            if (!isCurrentFragment(activity, ProfileFragment.class)) {

                FragmentManager fm =
                        ((AppCompatActivity) activity).getSupportFragmentManager();

                fm.beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment())
                        .addToBackStack(null) // 👈 VERY IMPORTANT
                        .commit();
            }
        });
    }


    // ================== HELPERS ==================

    private static boolean isCurrentFragment(
            Activity activity,
            Class<? extends Fragment> fragmentClass) {

        if (!(activity instanceof AppCompatActivity)) return false;

        Fragment current =
                ((AppCompatActivity) activity)
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);

        return current != null && fragmentClass.isInstance(current);
    }

    private static void setNavItem(Activity activity, View item, int icon, String text, boolean active) {
        ImageView iconView = item.findViewById(R.id.nav_icon);
        TextView textView = item.findViewById(R.id.nav_text);

        iconView.setImageResource(icon);

        // FIX: Use ContextCompat instead of getResources().getColor()
        int color = ContextCompat.getColor(activity,
                active ? R.color.nav_active : R.color.nav_inactive
        );

        iconView.setColorFilter(color);
        textView.setText(text);
        textView.setTextColor(color);
    }
}
