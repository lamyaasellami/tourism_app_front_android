package com.example.projet_front.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.projet_front.R;
import com.example.projet_front.activities.HomeActivity;
import com.example.projet_front.activities.HotelActivity;
import com.example.projet_front.activities.ProfileActivity;
import com.example.projet_front.activities.TransportActivity;

public class BottomNavBar {

    public static void setupBottomNav(Activity activity) {
        // 1. Find the parent views
        View navAccueil = activity.findViewById(R.id.nav_accueil);
        View navHotels = activity.findViewById(R.id.nav_hotels);
        View navTransport = activity.findViewById(R.id.nav_transport);
        View navFavoris = activity.findViewById(R.id.nav_favoris);
        View navProfil = activity.findViewById(R.id.nav_profil);

        // 2. Set the items (Logic based on which Activity is calling this)
        setNavItem(activity, navAccueil, R.drawable.ic_accueil, "Accueil", activity instanceof HomeActivity);
        setNavItem(activity, navHotels, R.drawable.ic_hotel, "Hôtels", activity instanceof HotelActivity);
        setNavItem(activity, navTransport, R.drawable.ic_transport, "Transport", activity instanceof TransportActivity); // FIX: Change to appropriate Activity
        setNavItem(activity, navFavoris, R.drawable.ic_favorite, "Favoris", false);
        setNavItem(activity, navProfil, R.drawable.ic_profile, "Profil", activity instanceof ProfileActivity);

        // 3. Click Listeners
        navAccueil.setOnClickListener(v -> {
            if (!(activity instanceof HomeActivity)) {
                Intent intent = new Intent(activity, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
            }
        });

        navHotels.setOnClickListener(v -> {
            if (!(activity instanceof HotelActivity)) {
                Intent intent = new Intent(activity, HotelActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
            }
        });

        navTransport.setOnClickListener(v -> {
            if (!(activity instanceof TransportActivity)) {
                Intent intent = new Intent(activity, TransportActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
            }
        });

        navProfil.setOnClickListener(v -> {
            if (!(activity instanceof ProfileActivity)) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
            }
        });

        // Add listeners for Hotels, Transport, etc. similarly
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
