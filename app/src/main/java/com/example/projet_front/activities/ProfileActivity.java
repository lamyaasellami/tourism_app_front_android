package com.example.projet_front.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.projet_front.R;
import com.example.projet_front.models.UserRegisterRequest;

public class ProfileActivity extends AppCompatActivity {

    // UI Components
    private TextView tvUserName, tvUserEmail;
    // Statistics cards
    private View statCardMemos, statCardReservations, statCardPoints;
    private Button btnModifyProfile, btnLogout;

    // Card views
    private View cardMesContenus, cardParametres, cardSupport;

    // User data
    private UserRegisterRequest currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI components
        initViews();

        // Load user data (in a real app, this would come from SharedPreferences or API)
        loadUserData();

        // Setup statistics cards
        setupStatisticsCards();

        // Setup cards with content
        setupMenuCards();

        // Set up click listeners
        setupListeners();
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        btnModifyProfile = findViewById(R.id.btnModifyProfile);
        btnLogout = findViewById(R.id.btn_logout);

        // Find statistics card views
        statCardMemos = findViewById(R.id.stat_card_memos);
        statCardReservations = findViewById(R.id.stat_card_reservations);
        statCardPoints = findViewById(R.id.stat_card_points);


        // Find menu card views
        cardMesContenus = findViewById(R.id.card_mes_contenus);
        cardParametres = findViewById(R.id.card_parametres);
        cardSupport = findViewById(R.id.card_support);
    }

    private void loadUserData() {
        // In a real app, you would fetch this from SharedPreferences or your database
        currentUser = new UserRegisterRequest();
        currentUser.name = "Aicha Benali";
        currentUser.email = "aicha.benali@gmail.com";

        // Update UI with user data
        updateUI();

        // Load stats (these would typically come from an API)
        //loadUserStats();
    }

    private void updateUI() {
        if (currentUser != null) {
            tvUserName.setText(currentUser.name);
            tvUserEmail.setText(currentUser.email);
        }
    }

    private void setupStatisticsCards() {
        // Setup Mémos card
        setupStatCard(statCardMemos,
                R.drawable.ic_event_note,
                "Mémos",
                24,
                "#E8F5E9");  // Light green background

        // Setup Réservations card
        setupStatCard(statCardReservations,
                R.drawable.ic_reservation,
                "Réservations",
                8,
                "#FFF3E0");  // Light orange background

        // Setup Points card
        setupStatCard(statCardPoints,
                R.drawable.ic_star,
                "Points",
                156,
                "#FFF9C4");  // Light yellow background
    }

    private void setupStatCard(View card, int iconRes, String title, int count, String backgroundColor) {
        CardView cvIconBackground = card.findViewById(R.id.cvIconBackground);
        ImageView ivStatIcon = card.findViewById(R.id.ivStatIcon);
        TextView tvStatTitle = card.findViewById(R.id.tvStatTitle);
        TextView tvStatCount = card.findViewById(R.id.tvStatCount);

        // Set icon
        ivStatIcon.setImageResource(iconRes);

        // Set title
        tvStatTitle.setText(title);

        // Set count
        tvStatCount.setText(String.valueOf(count));

        // Set background color for icon circle
        cvIconBackground.setCardBackgroundColor(android.graphics.Color.parseColor(backgroundColor));

        // Optional: Add click listener for statistics cards
        card.setOnClickListener(v -> {
            Toast.makeText(this, "Voir tous les " + title, Toast.LENGTH_SHORT).show();
            // Navigate to respective activity
        });
    }

    private void setupMenuCards() {
        // Setup Card 1: Mes Contenus
        updateStatistics(cardMesContenus, "Mes Contenus",
                R.drawable.ic_event_note, "Mes Mémos",
                R.drawable.ic_reservation, "Mes Réservations");

        // Setup Card 2: Paramètres
        updateStatistics(cardParametres, "Paramètres",
                R.drawable.ic_user, "Informations personnelles",
                R.drawable.ic_notif, "Notifications");

        // Setup Card 3: Support
        updateStatistics(cardSupport, "Support",
                R.drawable.ic_help, "Centre d'aide",
                R.drawable.ic_help, "Contacter le support");
    }

    private void updateStatistics(View card, String title,
                           int icon1, String label1,
                           int icon2, String label2) {
        TextView tvTitle = card.findViewById(R.id.tv_section_title);
        ImageView ivIcon1 = card.findViewById(R.id.iv_icon_1);
        TextView tvLabel1 = card.findViewById(R.id.tv_label_1);
        LinearLayout llItem1 = card.findViewById(R.id.ll_item_1);

        ImageView ivIcon2 = card.findViewById(R.id.iv_icon_2);
        TextView tvLabel2 = card.findViewById(R.id.tv_label_2);
        LinearLayout llItem2 = card.findViewById(R.id.ll_item_2);

        // Set content
        tvTitle.setText(title);
        ivIcon1.setImageResource(icon1);
        tvLabel1.setText(label1);
        ivIcon2.setImageResource(icon2);
        tvLabel2.setText(label2);

        // Set click listeners with specific actions
        llItem1.setOnClickListener(v -> handleMenuItemClick(title, label1));
        llItem2.setOnClickListener(v -> handleMenuItemClick(title, label2));
    }

    private void handleMenuItemClick(String section, String item) {
        // Handle navigation based on the clicked item
        Toast.makeText(this, section + " - " + item, Toast.LENGTH_SHORT).show();

        // Add your navigation logic here
        switch (item) {
            case "Mes Mémos":
                // Navigate to Memos Activity
                // Intent intent = new Intent(this, MemosActivity.class);
                // startActivity(intent);
                break;
            case "Mes Réservations":
                // Navigate to Reservations Activity
                break;
            case "Informations personnelles":
                // Navigate to Personal Info Activity
                break;
            case "Notifications":
                // Navigate to Notifications Settings
                break;
            case "Centre d'aide":
                // Navigate to Help Center
                break;
            case "Contacter le support":
                // Navigate to Contact Support
                break;
        }
    }

    private void setupListeners() {
        // Modify profile button
        if (btnModifyProfile != null) {
            btnModifyProfile.setOnClickListener(v -> {
                // Handle modify profile button click
                Toast.makeText(this, "Modifier le profil", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                // startActivity(intent);
            });
        }

        // Logout button
        btnLogout.setOnClickListener(v -> {
            // Handle logout
            Toast.makeText(this, "Déconnexion...", Toast.LENGTH_SHORT).show();
            // Implement logout logic here
            // Clear SharedPreferences, navigate to LoginActivity, etc.
        });
    }

    // Method to update user data from another activity/fragment
    public void updateUserData(UserRegisterRequest updatedUser) {
        this.currentUser = updatedUser;
        updateUI();
    }


    // Method to update statistics dynamically
    public void updateStatistics(int memosCount, int reservationsCount, int pointsCount) {
        TextView tvMemos = statCardMemos.findViewById(R.id.tvStatCount);
        TextView tvReservations = statCardReservations.findViewById(R.id.tvStatCount);
        TextView tvPoints = statCardPoints.findViewById(R.id.tvStatCount);

        tvMemos.setText(String.valueOf(memosCount));
        tvReservations.setText(String.valueOf(reservationsCount));
        tvPoints.setText(String.valueOf(pointsCount));
    }
}