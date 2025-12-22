package com.example.projet_front.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_front.R;
import com.example.projet_front.models.UserRegisterRequest;

public class ProfileActivity extends AppCompatActivity {

    // UI Components
    private TextView tvUserName, tvUserEmail;
    private TextView tvMemoriesCount, tvReservationsCount, tvPointsCount;
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

        // Setup cards with content
        setupCards();

        // Set up click listeners
        setupListeners();
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvMemoriesCount = findViewById(R.id.tvMemoriesCount);
        tvReservationsCount = findViewById(R.id.tvReservationsCount);
        tvPointsCount = findViewById(R.id.tvPointsCount);
        btnModifyProfile = findViewById(R.id.btnModifyProfile);
        btnLogout = findViewById(R.id.btn_logout);

        // Find card views
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
        loadUserStats();
    }

    private void updateUI() {
        if (currentUser != null) {
            tvUserName.setText(currentUser.name);
            tvUserEmail.setText(currentUser.email);
        }
    }

    private void loadUserStats() {
        // In a real app, these would come from API calls
        tvMemoriesCount.setText("24");
        tvReservationsCount.setText("8");
        tvPointsCount.setText("156");
    }

    private void setupCards() {
        // Setup Card 1: Mes Contenus
        setupCard(cardMesContenus, "Mes Contenus",
                R.drawable.ic_event_note, "Mes Mémos",
                R.drawable.ic_reservation, "Mes Réservations");

        // Setup Card 2: Paramètres
        setupCard(cardParametres, "Paramètres",
                R.drawable.ic_user, "Informations personnelles",
                R.drawable.ic_notif, "Notifications");

        // Setup Card 3: Support
        setupCard(cardSupport, "Support",
                R.drawable.ic_help, "Centre d'aide",
                R.drawable.ic_help, "Contacter le support");
    }

    private void setupCard(View card, String title,
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

    // Method to update stats
    public void updateStats(int memories, int reservations, int points) {
        tvMemoriesCount.setText(String.valueOf(memories));
        tvReservationsCount.setText(String.valueOf(reservations));
        tvPointsCount.setText(String.valueOf(points));
    }
}