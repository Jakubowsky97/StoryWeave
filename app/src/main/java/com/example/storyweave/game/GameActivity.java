package com.example.storyweave.game;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import com.example.storyweave.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity {

    FirebaseFirestore db;

    Button buttonSendData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        FirebaseOptions options = FirebaseOptions.fromResource(this);

        if (options != null) {
            // Initialize the default FirebaseApp instance manually
            FirebaseApp.initializeApp(this, options);
            System.out.println("Firebase has been initialized manually.");
        } else {
            System.err.println("FirebaseOptions could not be loaded from resources.");
            buttonSendData = findViewById(R.id.buttonSendData);

            buttonSendData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db = FirebaseFirestore.getInstance();
                    GameValues gm = new GameValues("polish", "4F34DA");

                    Map<String, Object> user = new HashMap<>();
                    user.put("first", "Alan");
                    user.put("middle", "Mathison");
                    user.put("last", "Turing");
                    user.put("born", 1912);

                    db.collection("test")
                            .add(user);
                }
            });

        }

    }
}
