package com.example.storyweave.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.storyweave.adapter.StoryAdapter;
import com.example.storyweave.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private StoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        adapter = new StoryAdapter();
        binding.storyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.storyRecyclerView.setAdapter(adapter);

        viewModel.getStoryList().observe(getViewLifecycleOwner(), adapter::submitList);

        viewModel.fetchStories();

        return binding.getRoot();
    }
}
