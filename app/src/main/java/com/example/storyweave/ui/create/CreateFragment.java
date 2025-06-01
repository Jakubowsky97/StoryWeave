package com.example.storyweave.ui.create;

import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.storyweave.databinding.FragmentCreateBinding;

public class CreateFragment extends Fragment {

    private FragmentCreateBinding binding;
    private CreateViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(CreateViewModel.class);

        setupListeners();
        setupObservers();

        return binding.getRoot();
    }

    private void setupListeners() {
        binding.btnCreate.setOnClickListener(v -> {
            String title = binding.inputTitle.getText().toString().trim();
            String description = binding.inputDescription.getText().toString().trim();

            if (title.isEmpty()) {
                binding.inputTitle.setError("Title is required");
                return;
            }

            viewModel.createStory(title, description);
        });

        binding.btnCopyCode.setOnClickListener(v -> {
            String code = binding.textInviteCode.getText().toString();
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Invite Code", code);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(requireContext(), "Invite code copied!", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupObservers() {
        viewModel.getGeneratedInviteCode().observe(getViewLifecycleOwner(), code -> {
            binding.textInviteCode.setText(code);
        });

        viewModel.getCreateSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                binding.inviteCodeLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
