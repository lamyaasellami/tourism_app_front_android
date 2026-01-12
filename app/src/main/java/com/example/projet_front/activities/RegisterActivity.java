package com.example.projet_front.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_front.R;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.UserRegisterRequest;
import com.example.projet_front.models.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText nameEt, emailEt, passwordEt, countryEt, languageEt, currencyEt;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEt = findViewById(R.id.etName);
        emailEt = findViewById(R.id.etEmail);
        passwordEt = findViewById(R.id.etPassword);
        countryEt = findViewById(R.id.etCountry);
        languageEt = findViewById(R.id.etLanguage);
        currencyEt = findViewById(R.id.etCurrency);
        registerBtn = findViewById(R.id.btnRegister);

        registerBtn.setOnClickListener(v -> {
            if (validateInputs()) {
                registerUser();
            }
        });
    }

    private boolean validateInputs() {
        String name = nameEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        if (name.isEmpty()) {
            nameEt.setError("Nom requis");
            return false;
        }

        if (email.isEmpty()) {
            emailEt.setError("Email requis");
            return false;
        }

        if (!email.contains("@")) {
            emailEt.setError("Email invalide");
            return false;
        }

        if (password.isEmpty()) {
            passwordEt.setError("Mot de passe requis");
            return false;
        }

        if (password.length() < 6) {
            passwordEt.setError("Minimum 6 caractères");
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            passwordEt.setError("Doit contenir une majuscule");
            return false;
        }

        return true;
    }

    private void registerUser() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.name = nameEt.getText().toString().trim();
        request.email = emailEt.getText().toString().trim();
        request.password = passwordEt.getText().toString().trim();
        request.country = countryEt.getText().toString().trim();
        request.language = languageEt.getText().toString().trim();
        request.currency = currencyEt.getText().toString().trim();

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.register(request).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Erreur inscription: Email déjà utilisé", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Erreur serveur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}