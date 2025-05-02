package com.example.storyweave.ui.create;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.storyweave.GameActivity;
import com.example.storyweave.R;
import com.example.storyweave.databinding.FragmentCreateBinding;

public class CreateFragment extends Fragment {

    private FragmentCreateBinding binding;

    private Button btngame;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CreateViewModel createViewModel =
                new ViewModelProvider(this).get(CreateViewModel.class);
        binding = FragmentCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCreate;
        createViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        btngame = root.findViewById(R.id.button_game);

        btngame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GameActivity.class));
            }
        });



        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}