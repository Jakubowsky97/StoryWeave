package com.example.storyweave.lobby;

import static com.example.storyweave.game.GameCodeGenerator.generateSixDigitCode;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.storyweave.R;
import com.example.storyweave.game.GameActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class LobbyActivity extends AppCompatActivity {
    Button buttonAddGameData;

    TextView test;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        buttonAddGameData = findViewById(R.id.buttonAddGameData);
        test = findViewById(R.id.test);
        if (buttonAddGameData == null) {
            Toast.makeText(LobbyActivity.this, "Nie znaleziono przycisku w layoucie!", Toast.LENGTH_LONG).show();
        }
        buttonAddGameData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test.setTextColor(getResources().getColor(R.color.purple_200));
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                LobbyInformation gm = new LobbyInformation("polish", generateSixDigitCode());
                db.collection("games").document("a")
                        .set(gm)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(LobbyActivity.this, "Succesfully created game", Toast.LENGTH_LONG).show();
//                            setContentView(R.layout.GameActivity);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(LobbyActivity.this, "Failed to create game: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                            setContentView(R.layout.LobbyActivity);
                        });
            }
        });
    }
}
