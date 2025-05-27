package com.example.storyweave.game;

import static com.example.storyweave.game.GameCodeGenerator.generateSixDigitCode;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.storyweave.R;
import com.example.storyweave.lobby.LobbyInformation;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class GameActivity extends AppCompatActivity {

    Button buttonAddGameData;

    TextView test;

    //DropBoxManager language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FirebaseApp.initializeApp(this);

//        KODZIK DO OGARNIANIA GIERKI (osoby, wiadomości itd.)

        FirebaseFirestore db = FirebaseFirestore.getInstance();


    }
}
