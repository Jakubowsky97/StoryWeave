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

public class LobbyActivity extends AppCompatActivity {

    private TextView textTitle, textType, textInviteCode;
    private Button buttonCopy, buttonContinue;
    private LobbyInformation lobbyInfo;
    private String storyId, inviteCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        // Przypisanie widoków
        textTitle = findViewById(R.id.textTitle);
        textType = findViewById(R.id.textType);
        textInviteCode = findViewById(R.id.textInviteCode);
        buttonCopy = findViewById(R.id.buttonCopy);
        buttonContinue = findViewById(R.id.buttonContinue);

        // Pobranie danych z Intentu
        storyId = getIntent().getStringExtra("storyId");
        inviteCode = getIntent().getStringExtra("inviteCode");
        lobbyInfo = (LobbyInformation) getIntent().getSerializableExtra("lobbyInfo");

        if (lobbyInfo != null) {
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
        } else {
            // fallback gdy LobbyInformation nie zostało przekazane
            textInviteCode.setText(inviteCode);
            textType.setText("Public");
            textTitle.setText("Untitled");

            buttonCopy.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Invite Code", inviteCode);
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
}
