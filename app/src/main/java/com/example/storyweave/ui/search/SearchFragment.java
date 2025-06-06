package com.example.storyweave.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.storyweave.databinding.FragmentSearchBinding;
import com.example.storyweave.model.Story;
import com.example.storyweave.story.StoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.buttonSearch.setOnClickListener(v -> {
            String code = binding.inputCode.getText().toString().trim();
            if (code.isEmpty()) {
                Toast.makeText(getContext(), "Wpisz kod rozgrywki", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.searchByCode(code);
            }
        });

        viewModel.getFoundStory().observe(getViewLifecycleOwner(), story -> {
            if (story != null) {
              FirebaseFirestore.getInstance().collection("users").document(story.getOwnerId()).get().addOnSuccessListener(documentSnapshot -> {
                  System.out.println(documentSnapshot);
                  if(documentSnapshot.exists()){
                        String email = documentSnapshot.getString("email");
                        binding.textOwner.setText("Autor: " + email.split("@")[0]);
                    } else {
                        binding.textOwner.setText("Autor: Null");
                    }
                });
                binding.textTitle.setText("Tytuł: " + story.getTitle());
                binding.textDescription.setText("Opis: " + story.getDescription());
                binding.resultLayout.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), "Nie znaleziono gry o podanym kodzie", Toast.LENGTH_SHORT).show();
                binding.resultLayout.setVisibility(View.GONE);
            }
            binding.resultLayout.setAlpha(0f);
            binding.resultLayout.setVisibility(View.VISIBLE);
            binding.resultLayout.animate().alpha(1f).setDuration(400).start();

        });

        binding.buttonJoin.setOnClickListener(v -> {
            Story found = viewModel.foundStoryLive.getValue();
            if (found != null) {
                // Przykład: przekierowanie do StoryActivity
                Intent intent = new Intent(getContext(), StoryActivity.class);
                intent.putExtra("storyId", found.getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
