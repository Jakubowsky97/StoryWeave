package com.example.storyweave.ui.create;


import android.app.Application;
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

import com.example.storyweave.GameActivity;
import com.example.storyweave.R;
import com.example.storyweave.databinding.FragmentCreateBinding;

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
                startActivity(new Intent(getActivity(), GameActivity.class));
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
    public static String generateSixDigitCode(){

        String code = "";
        char[] uppercaseLetters = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        };
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        for (int i = 0; i < 6; i++){
            Random random = new Random();
            if (Math.round(random.nextDouble()) == 0){
                code += uppercaseLetters[random.nextInt(uppercaseLetters.length)];
            } else {
                code += numbers[random.nextInt(numbers.length)];
            }
        }
        return code;
    }
}