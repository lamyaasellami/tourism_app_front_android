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
import com.example.projet_front.models.UserLoginRequest;
import com.example.projet_front.models.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // Déclaration des variables
    EditText emailEt, passwordEt;
    Button loginBtn;
    TextView tvRegister; // <-- Ajouté pour le TextView "Créer un compte"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisation des vues
        emailEt = findViewById(R.id.email);       // correspond à android:id="@+id/email" dans le XML
        passwordEt = findViewById(R.id.password); // correspond à android:id="@+id/password" dans le XML
        loginBtn = findViewById(R.id.loginBtn);   // correspond à android:id="@+id/loginBtn"
        tvRegister = findViewById(R.id.tvRegister); // correspond à android:id="@+id/tvRegister"

        // Listener pour le bouton login
        loginBtn.setOnClickListener(v -> login());

        // Listener pour le TextView "Créer un compte"
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        UserLoginRequest request = new UserLoginRequest();
        request.email = emailEt.getText().toString();
        request.password = passwordEt.getText().toString();

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.login(request).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<UserResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(
                        LoginActivity.this,
                        "Erreur: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
