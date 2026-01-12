package com.example.projet_front.fragments;

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
import com.example.projet_front.models.UserRegisterRequest;
import com.example.projet_front.utils.BottomNavBar;

public class ProfileFragment extends Fragment {

    // UI Components
    private TextView tvUserName, tvUserEmail;
    private View statCardMemos, statCardReservations, statCardPoints;
    private Button btnModifyProfile, btnLogout;
    private View cardMesContenus, cardParametres, cardSupport;

    // User data
    private UserRegisterRequest currentUser;

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
        currentUser = new UserRegisterRequest();
        currentUser.name = "Aicha Benali";
        currentUser.email = "aicha.benali@gmail.com";
        updateUI();
    }

    private void updateUI() {
        if (currentUser != null) {
            tvUserName.setText(currentUser.name);
            tvUserEmail.setText(currentUser.email);
        }
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
        updateStatistics(cardMesContenus,
                "Mes Contenus",
                R.drawable.ic_event_note,
                "Mes Mémos",
                R.drawable.ic_reservation,
                "Mes Réservations");

        updateStatistics(cardParametres,
                "Paramètres",
                R.drawable.ic_user,
                "Informations personnelles",
                R.drawable.ic_notif,
                "Notifications");

        updateStatistics(cardSupport,
                "Support",
                R.drawable.ic_help,
                "Centre d'aide",
                R.drawable.ic_help,
                "Contacter le support");
    }

    private void updateStatistics(
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

        llItem1.setOnClickListener(v -> handleMenuItemClick(title, label1));
        llItem2.setOnClickListener(v -> handleMenuItemClick(title, label2));
    }

    private void handleMenuItemClick(String section, String item) {
        Toast.makeText(requireContext(),
                section + " - " + item,
                Toast.LENGTH_SHORT).show();
    }

    /* -------------------- LISTENERS -------------------- */

    private void setupListeners(View view) {

        btnModifyProfile.setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        "Modifier le profil",
                        Toast.LENGTH_SHORT).show()
        );

        btnLogout.setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        "Déconnexion...",
                        Toast.LENGTH_SHORT).show()
        );

        ImageView btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );
    }

    /* -------------------- EXTERNAL UPDATES -------------------- */

    public void updateUserData(UserRegisterRequest updatedUser) {
        currentUser = updatedUser;
        updateUI();
    }

    public void updateStatisticsCounts(int memos, int reservations, int points) {
        ((TextView) statCardMemos.findViewById(R.id.tvStatCount))
                .setText(String.valueOf(memos));

        ((TextView) statCardReservations.findViewById(R.id.tvStatCount))
                .setText(String.valueOf(reservations));

        ((TextView) statCardPoints.findViewById(R.id.tvStatCount))
                .setText(String.valueOf(points));
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
