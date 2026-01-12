package com.example.projet_front.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_front.R;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.LoginResponse;
import com.example.projet_front.models.UserLoginRequest;
import com.example.projet_front.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailEt, passwordEt;
    Button loginBtn;
    TextView tvRegister;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokenManager = new TokenManager(this);

        // Si déjà connecté, aller directement à MainActivity
        if (tokenManager.isLoggedIn()) {
            // Initialiser ApiClient avec le token existant
            ApiClient.init(tokenManager);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        emailEt = findViewById(R.id.email);
        passwordEt = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        tvRegister = findViewById(R.id.tvRegister);

        loginBtn.setOnClickListener(v -> login());

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        UserLoginRequest request = new UserLoginRequest();
        request.email = email;
        request.password = password;

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    // Sauvegarder TOUTES les infos utilisateur
                    tokenManager.saveToken(loginResponse.token);
                    tokenManager.saveUserId(loginResponse.user.user_id);
                    tokenManager.saveUserName(loginResponse.user.name);
                    tokenManager.saveUserEmail(loginResponse.user.email);
                    tokenManager.saveUserCountry(loginResponse.user.country != null ? loginResponse.user.country : "");
                    tokenManager.saveUserLanguage(loginResponse.user.language != null ? loginResponse.user.language : "");
                    tokenManager.saveUserCurrency(loginResponse.user.currency != null ? loginResponse.user.currency : "");

                    // Initialiser ApiClient avec le nouveau token
                    ApiClient.init(tokenManager);

                    Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, "Erreur: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}