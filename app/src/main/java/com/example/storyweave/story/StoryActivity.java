package com.example.storyweave.story;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.storyweave.adapter.StoryNodeAdapter;
import com.example.storyweave.databinding.ActivityStoryBinding;
import com.example.storyweave.story.StoryViewModel;

public class StoryActivity extends ComponentActivity {

    private ActivityStoryBinding binding;
    private StoryViewModel viewModel;
    private StoryNodeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String storyId = getIntent().getStringExtra("storyId");
        if (storyId == null) {
            Toast.makeText(this, "No story ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(StoryViewModel.class);
        adapter = new StoryNodeAdapter();

        binding.recyclerNodes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerNodes.setAdapter(adapter);

        viewModel.getStoryNodes(storyId).observe(this, nodes -> {
            if (nodes != null) {
                adapter.submitList(nodes);
                binding.progressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Failed to load story", Toast.LENGTH_SHORT).show();
            }
        });

        // Możliwość dodania przycisku do dopisania fragmentu
        binding.btnAddNode.setOnClickListener(v -> {
            // Możesz tu otworzyć Dialog lub nowe okno
            Toast.makeText(this, "Add node coming soon!", Toast.LENGTH_SHORT).show();
        });
    }
}
