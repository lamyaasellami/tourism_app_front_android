package com.example.projet_front.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.projet_front.R;
import com.example.projet_front.adapters.OnboardingAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OnboardingActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);

        OnboardingAdapter adapter = new OnboardingAdapter();
        viewPager.setAdapter(adapter);

        // Connect Dots Indicator (TabLayout)
        TabLayout tabLayout = findViewById(R.id.tabIndicator);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();

        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                // Finish Onboarding and go to Login
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }
}