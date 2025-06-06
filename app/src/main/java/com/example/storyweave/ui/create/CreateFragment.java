package com.example.storyweave.ui.create;

import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.storyweave.databinding.FragmentCreateBinding;
import com.example.storyweave.lobby.LobbyActivity;

public class CreateFragment extends Fragment {

    private FragmentCreateBinding binding;
    private CreateViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(CreateViewModel.class);

        binding.btnCreate.setOnClickListener(v -> {
            String title = binding.inputTitle.getText().toString().trim();
            String description = binding.inputDescription.getText().toString().trim();

            if (title.isEmpty()) {
                binding.inputTitle.setError("Title is required");
                return;
            }

            viewModel.createStory(title, description);

            viewModel.getCreateSuccess().observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    Toast.makeText(getContext(), "Story created successfully", Toast.LENGTH_SHORT).show();
                }
            });

            viewModel.getCreatedStoryId().observe(getViewLifecycleOwner(), storyId -> {
                if (storyId != null) {
                    String inviteCode = viewModel.getGeneratedInviteCode().getValue();

                    Intent intent = new Intent(requireContext(), LobbyActivity.class);
                    intent.putExtra("storyId", storyId);
                    intent.putExtra("inviteCode", inviteCode);
                    startActivity(intent);
                    requireActivity().finish();
                }
            });
        });

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
