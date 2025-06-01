package com.example.storyweave.ui.create;


import static com.example.storyweave.game.GameCodeGenerator.generateSixDigitCode;

import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.storyweave.game.GameActivity;
import com.example.storyweave.R;
import com.example.storyweave.databinding.FragmentCreateBinding;
import com.example.storyweave.lobby.LobbyActivity;

import java.util.Random;

public class CreateFragment extends Fragment {

    private FragmentCreateBinding binding;


    private TextView txtcopy;
    private Button btngame, btncopy;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CreateViewModel createViewModel =
                new ViewModelProvider(this).get(CreateViewModel.class);
        binding = FragmentCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCreate;
        createViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //game button test********************************
        btngame = root.findViewById(R.id.button_game);

        btngame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LobbyActivity.class);
                startActivity(intent);
            }
        });
        //game button test********************************


        //random code*************************
        txtcopy = root.findViewById(R.id.textcopy);

        txtcopy.setText(generateSixDigitCode());

        btncopy = root.findViewById(R.id.button_copy);

        btncopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Code", txtcopy.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity().getApplicationContext(), "String copied to Clipboard", Toast.LENGTH_LONG).show();
            }

        });
        //random code*************************

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}