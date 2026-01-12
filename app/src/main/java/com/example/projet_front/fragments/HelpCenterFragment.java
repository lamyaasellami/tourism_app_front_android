package com.example.projet_front.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projet_front.R;
import android.widget.ImageView;


public class HelpCenterFragment extends Fragment {

    public HelpCenterFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_help_center, container, false);

        initListeners(view);
        initBackButton(view);

        return view;
    }

    private void initListeners(View view) {

        LinearLayout llPolice = view.findViewById(R.id.ll_police);
        LinearLayout llAmbulance = view.findViewById(R.id.ll_ambulance);
        LinearLayout llPompiers = view.findViewById(R.id.ll_pompiers);
        LinearLayout llGendarmerie = view.findViewById(R.id.ll_gendarmerie);

        llPolice.setOnClickListener(v -> dialNumber("19"));
        llAmbulance.setOnClickListener(v -> dialNumber("15"));
        llPompiers.setOnClickListener(v -> dialNumber("15"));
        llGendarmerie.setOnClickListener(v -> dialNumber("177"));
    }

    private void dialNumber(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }
    private void initBackButton(View view) {
        ImageView btnBack = view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });
    }
}
