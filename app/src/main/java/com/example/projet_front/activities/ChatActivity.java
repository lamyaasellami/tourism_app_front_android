package com.example.projet_front.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projet_front.R;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ChatRequest;
import com.example.projet_front.api.ChatResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private LinearLayout messageContainer;
    private LinearLayout introContainer;
    private EditText inputMessage;
    private ImageButton sendBtn;
    private ScrollView messageScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_convo);

        messageContainer = findViewById(R.id.messageContainer);
        introContainer = findViewById(R.id.introContainer);
        inputMessage = findViewById(R.id.inputMessage);
        sendBtn = findViewById(R.id.sendBtn);
        messageScroll = findViewById(R.id.messageScroll);

        // Listener pour le bouton d'envoi
        sendBtn.setOnClickListener(v -> sendMessage());

        // Option: envoyer avec la touche Enter
        inputMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }

    private void sendMessage() {
        String text = inputMessage.getText().toString().trim();

        if (text.isEmpty()) {
            return;
        }

        // Masquer l'intro au premier message
        if (introContainer.getVisibility() == View.VISIBLE) {
            introContainer.setVisibility(View.GONE);
        }

        // Ajouter le message de l'utilisateur
        addMessageToUI(text, true);

        // Vider le champ de saisie
        inputMessage.setText("");

        // Désactiver le bouton pendant l'envoi
        sendBtn.setEnabled(false);

        // Appel API
        ChatRequest request = new ChatRequest(text);
        ApiClient.getApiService().sendMessage(request).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                sendBtn.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    String botResponse = response.body().getAnswer();
                    addMessageToUI(botResponse, false);
                } else {
                    Toast.makeText(ChatActivity.this,
                            "Erreur: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                    addMessageToUI("Désolé, une erreur s'est produite.", false);
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                sendBtn.setEnabled(true);
                Toast.makeText(ChatActivity.this,
                        "Erreur de connexion: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                addMessageToUI("Impossible de se connecter au serveur.", false);
            }
        });
    }

    private void addMessageToUI(String text, boolean isUser) {
        // Créer une bulle de message
        View messageView = getLayoutInflater().inflate(
                isUser ? R.layout.item_message_user : R.layout.item_message_bot,
                messageContainer,
                false
        );

        TextView messageText = messageView.findViewById(R.id.messageText);
        messageText.setText(text);

        // Ajouter au conteneur
        messageContainer.addView(messageView);

        // Scroll automatique vers le bas
        messageScroll.post(() -> messageScroll.fullScroll(View.FOCUS_DOWN));
    }
}