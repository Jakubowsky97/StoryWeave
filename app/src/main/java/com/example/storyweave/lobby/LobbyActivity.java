package com.example.storyweave.lobby;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.storyweave.R;
import com.example.storyweave.story.StoryActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;

// ...

public class LobbyActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView textTitle, textType, textInviteCode;
    private Button buttonCopy, buttonContinue;
    private LobbyInformation lobbyInfo;
    private String storyId, inviteCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        // Inicjalizacja Firestore
        db = FirebaseFirestore.getInstance();

        // Przypisanie widoków
        textTitle = findViewById(R.id.textTitle);
        textType = findViewById(R.id.textType);
        textInviteCode = findViewById(R.id.textInviteCode);
        buttonCopy = findViewById(R.id.buttonCopy);
        buttonContinue = findViewById(R.id.buttonContinue);

        // Pobranie danych z Intentu
        storyId = getIntent().getStringExtra("storyId");
        inviteCode = getIntent().getStringExtra("inviteCode");

        if (storyId != null && !storyId.isEmpty()) {
            fetchLobbyInfo(storyId);
        } else {
            setupFallbackUI();
        }
    }

    private void fetchLobbyInfo(String storyId) {
        db.collection("stories").document(storyId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null && doc.exists()) {
                        // Mapuj dane do LobbyInformation (załóżmy, że masz konstruktor lub builder)
                        lobbyInfo = new LobbyInformation(
                                storyId,
                                doc.getString("title"),
                                doc.getBoolean("isPrivate") != null ? doc.getBoolean("isPrivate") : false,
                                doc.getString("inviteCode"),
                                doc.getString("creatorId")
                        );

                        updateUIWithLobbyInfo();
                    } else {
                        setupFallbackUI();
                    }
                } else {
                    setupFallbackUI();
                }
            }
        });
    }

    private void updateUIWithLobbyInfo() {
        textTitle.setText(lobbyInfo.getTitle());
        textType.setText(lobbyInfo.isPrivate() ? "Private" : "Public");
        textInviteCode.setText(lobbyInfo.getInviteCode());

        buttonCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Invite Code", lobbyInfo.getInviteCode());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        buttonContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, StoryActivity.class);
            intent.putExtra("storyId", lobbyInfo.getStoryId());
            startActivity(intent);
        });
    }

    private void setupFallbackUI() {
        textInviteCode.setText(inviteCode != null ? inviteCode : "N/A");
        textType.setText("Public");
        textTitle.setText("Untitled");

        buttonCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Invite Code", inviteCode != null ? inviteCode : "");
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        buttonContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, StoryActivity.class);
            intent.putExtra("storyId", storyId);
            startActivity(intent);
        });
    }
}
