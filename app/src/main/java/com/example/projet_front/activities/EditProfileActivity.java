package com.example.projet_front.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_front.R;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.UserResponse;
import com.example.projet_front.models.UserUpdateRequest;
import com.example.projet_front.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail, etCountry, etLanguage, etCurrency, etCurrentPassword, etNewPassword;
    private Button btnSave;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialiser le TokenManager
        tokenManager = new TokenManager(this);

        // Initialiser ApiClient avec le token
        ApiClient.init(tokenManager);

        // Initialiser les EditTexts et le bouton
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etCountry = findViewById(R.id.etCountry);
        etLanguage = findViewById(R.id.etLanguage);
        etCurrency = findViewById(R.id.etCurrency);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnSave = findViewById(R.id.btnSave);

        // Pré-remplir les champs avec les infos existantes
        etName.setText(tokenManager.getUserName());
        etEmail.setText(tokenManager.getUserEmail());
        etCountry.setText(tokenManager.getUserCountry());
        etLanguage.setText(tokenManager.getUserLanguage());
        etCurrency.setText(tokenManager.getUserCurrency());

        // Bouton "Sauvegarder"
        btnSave.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {
        String newPassword = etNewPassword.getText().toString().trim();
        String currentPassword = etCurrentPassword.getText().toString().trim();

        // Validation : si on veut changer le mot de passe, il faut fournir l'ancien
        if (!newPassword.isEmpty() && currentPassword.isEmpty()) {
            etCurrentPassword.setError("Mot de passe actuel requis pour changer le mot de passe");
            return;
        }

        // Créer l'objet de requête
        UserUpdateRequest request = new UserUpdateRequest();
        request.name = etName.getText().toString().trim();
        request.email = etEmail.getText().toString().trim();
        request.country = etCountry.getText().toString().trim();
        request.language = etLanguage.getText().toString().trim();
        request.currency = etCurrency.getText().toString().trim();

        // Ajouter les mots de passe seulement s'ils sont remplis
        if (!currentPassword.isEmpty()) {
            request.currentPassword = currentPassword;
        }
        if (!newPassword.isEmpty()) {
            request.newPassword = newPassword;
        }

        // Appel API
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.updateProfile(request).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();

                    // Mise à jour locale du TokenManager
                    tokenManager.saveUserName(user.name);
                    tokenManager.saveUserEmail(user.email);
                    tokenManager.saveUserCountry(user.country);
                    tokenManager.saveUserLanguage(user.language);
                    tokenManager.saveUserCurrency(user.currency);

                    Toast.makeText(EditProfileActivity.this,
                            "Profil mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this,
                            "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this,
                        "Erreur serveur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}