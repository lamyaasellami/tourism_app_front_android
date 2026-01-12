package com.example.projet_front.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.projet_front.R;
import com.example.projet_front.activities.EditProfileActivity;
import com.example.projet_front.activities.LoginActivity;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.utils.BottomNavBar;
import com.example.projet_front.utils.TokenManager;

public class ProfileFragment extends Fragment {

    // UI
    private TextView tvUserName, tvUserEmail;
    private Button btnModifyProfile, btnLogout;

    private View statCardMemos, statCardReservations, statCardPoints;
    private View cardMesContenus, cardParametres, cardSupport;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        initViews(view);
        loadUserData();
        setupStatisticsCards();
        setupMenuCards();
        setupListeners(view);

        return view;
    }

    /* -------------------- INIT -------------------- */

    private void initViews(View view) {
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);

        btnModifyProfile = view.findViewById(R.id.btnModifyProfile);
        btnLogout = view.findViewById(R.id.btn_logout);

        statCardMemos = view.findViewById(R.id.stat_card_memos);
        statCardReservations = view.findViewById(R.id.stat_card_reservations);
        statCardPoints = view.findViewById(R.id.stat_card_points);

        cardMesContenus = view.findViewById(R.id.card_mes_contenus);
        cardParametres = view.findViewById(R.id.card_parametres);
        cardSupport = view.findViewById(R.id.card_support);
    }

    /* -------------------- USER DATA -------------------- */

    private void loadUserData() {
        TokenManager tokenManager = new TokenManager(requireContext());

        if (!tokenManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        tvUserName.setText(tokenManager.getUserName());
        tvUserEmail.setText(tokenManager.getUserEmail());
    }

    /* -------------------- STATS -------------------- */

    private void setupStatisticsCards() {
        setupStatCard(statCardMemos,
                R.drawable.ic_event_note,
                "Mémos",
                24,
                "#E8F5E9");

        setupStatCard(statCardReservations,
                R.drawable.ic_reservation,
                "Réservations",
                8,
                "#FFF3E0");

        setupStatCard(statCardPoints,
                R.drawable.ic_star,
                "Points",
                156,
                "#FFF9C4");
    }

    private void setupStatCard(
            View card,
            int iconRes,
            String title,
            int count,
            String backgroundColor
    ) {
        CardView cvIconBackground = card.findViewById(R.id.cvIconBackground);
        ImageView ivStatIcon = card.findViewById(R.id.ivStatIcon);
        TextView tvStatTitle = card.findViewById(R.id.tvStatTitle);
        TextView tvStatCount = card.findViewById(R.id.tvStatCount);

        ivStatIcon.setImageResource(iconRes);
        tvStatTitle.setText(title);
        tvStatCount.setText(String.valueOf(count));
        cvIconBackground.setCardBackgroundColor(Color.parseColor(backgroundColor));

        card.setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        "Voir tous les " + title,
                        Toast.LENGTH_SHORT).show()
        );
    }

    /* -------------------- MENU CARDS -------------------- */

    private void setupMenuCards() {
        setupMenu(cardMesContenus,
                "Mes Contenus",
                R.drawable.ic_event_note,
                "Mes Mémos",
                R.drawable.ic_reservation,
                "Mes Réservations");

        setupMenu(cardParametres,
                "Paramètres",
                R.drawable.ic_user,
                "Informations personnelles",
                R.drawable.ic_notif,
                "Notifications");

        setupMenu(cardSupport,
                "Support",
                R.drawable.ic_help,
                "Centre d'aide",
                R.drawable.ic_help,
                "Contacter le support");
    }

    private void setupMenu(
            View card,
            String title,
            int icon1,
            String label1,
            int icon2,
            String label2
    ) {
        TextView tvTitle = card.findViewById(R.id.tv_section_title);
        ImageView ivIcon1 = card.findViewById(R.id.iv_icon_1);
        TextView tvLabel1 = card.findViewById(R.id.tv_label_1);
        LinearLayout llItem1 = card.findViewById(R.id.ll_item_1);

        ImageView ivIcon2 = card.findViewById(R.id.iv_icon_2);
        TextView tvLabel2 = card.findViewById(R.id.tv_label_2);
        LinearLayout llItem2 = card.findViewById(R.id.ll_item_2);

        tvTitle.setText(title);
        ivIcon1.setImageResource(icon1);
        tvLabel1.setText(label1);

        ivIcon2.setImageResource(icon2);
        tvLabel2.setText(label2);

        llItem1.setOnClickListener(v -> {
            if ("Centre d'aide".equals(label1)) {
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HelpCenterFragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(requireContext(), label1, Toast.LENGTH_SHORT).show();
            }
        });


        llItem2.setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        label2,
                        Toast.LENGTH_SHORT).show()
        );
    }

    /* -------------------- LISTENERS -------------------- */

    private void setupListeners(View view) {

        btnModifyProfile.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), EditProfileActivity.class))
        );

        btnLogout.setOnClickListener(v -> {
            TokenManager tokenManager = new TokenManager(requireContext());
            tokenManager.clearAll();

            // Réinitialiser ApiClient pour supprimer le token des headers
            ApiClient.resetClient();

            Toast.makeText(requireContext(),
                    "Déconnexion réussie",
                    Toast.LENGTH_SHORT).show();

            redirectToLogin();
        });

        ImageView btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );
    }

    /* -------------------- REDIRECTION -------------------- */

    private void redirectToLogin() {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /* -------------------- BOTTOM NAV -------------------- */

    @Override
    public void onResume() {
        super.onResume();

        // Recharger les données utilisateur au retour
        loadUserData();

        if (getActivity() != null) {
            BottomNavBar.setupBottomNav(getActivity());
        }
    }
}