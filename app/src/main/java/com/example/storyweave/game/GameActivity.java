package com.example.storyweave.game;

import static com.example.storyweave.game.GameCodeGenerator.generateSixDigitCode;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.storyweave.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity {

    Button buttonAddGameData;

    TextView test;

    //DropBoxManager language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FirebaseApp.initializeApp(this);

        buttonAddGameData = findViewById(R.id.buttonAddGameData);
        test = findViewById(R.id.test);
        if (buttonAddGameData == null) {
            Toast.makeText(GameActivity.this, "Nie znaleziono przycisku w layoucie!", Toast.LENGTH_LONG).show();
        }
        buttonAddGameData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test.setTextColor(getResources().getColor(R.color.purple_200));
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                GameValues gm = new GameValues("polish", generateSixDigitCode());
                db.collection("games").document("a")
                        .set(gm)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(GameActivity.this, "Succesfully created game", Toast.LENGTH_LONG).show();
//                            setContentView(R.layout.GameActivity);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(GameActivity.this, "Failed to create game: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                            setContentView(R.layout.LobbyActivity);
                        });
            }
        });

    }
}
