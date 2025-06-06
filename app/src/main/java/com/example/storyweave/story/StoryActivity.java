package com.example.storyweave.story;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.storyweave.adapter.StoryNodeAdapter;
import com.example.storyweave.databinding.ActivityStoryBinding;
import com.example.storyweave.model.StoryNode;

import java.util.Stack;

public class StoryActivity extends AppCompatActivity implements AddNodeDialog.OnNodeAddedListener, StoryNodeAdapter.OnNodeActionListener {

    private ActivityStoryBinding binding;
    private StoryViewModel viewModel;
    private StoryNodeAdapter adapter;
    private String storyId;
    private String currentParentId = null; // null dla root węzłów
    private Stack<String> parentHistory = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storyId = getIntent().getStringExtra("storyId");
        if (storyId == null) {
            Toast.makeText(this, "No story ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(StoryViewModel.class);
        adapter = new StoryNodeAdapter(this);

        binding.recyclerNodes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerNodes.setAdapter(adapter);

        loadStoryNodes();

        binding.btnAddNode.setOnClickListener(v -> {
            AddNodeDialog dialog = AddNodeDialog.newInstance(currentParentId, getLastNodeContent());
            dialog.show(getSupportFragmentManager(), "AddNodeDialog");
        });

        // Obserwuj wyniki
        viewModel.getAddNodeSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Frase added successfully", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }
    @Override
    public void onBackPressed() {
        if (!parentHistory.isEmpty()) {
            currentParentId = parentHistory.pop();
            loadStoryNodes();
        } else {
            super.onBackPressed(); // wróć do lobby, jeśli nie ma historii
        }
    }


    private void loadStoryNodes() {
        viewModel.getNodesByParent(storyId, currentParentId).observe(this, nodes -> {
            if (nodes != null) {
                adapter.submitList(nodes);
                binding.progressBar.setVisibility(View.GONE);

                // Aktualizuj currentParentId na najlepiej oceniony węzeł
                if (!nodes.isEmpty() && currentParentId == null) {
                    // Dla głównej linii fabularnej, wybierz węzeł z największą liczbą głosów
                    StoryNode bestNode = nodes.get(0); // Lista jest już posortowana według głosów
                    currentParentId = bestNode.getId();
                }
            } else {
                Toast.makeText(this, "Failed to load story", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getLastNodeContent() {
        if (adapter.getCurrentList().isEmpty()) {
            return "";
        }
        return adapter.getCurrentList().get(adapter.getCurrentList().size() - 1).getContent();
    }

    @Override
    public void onNodeAdded(String content, String parentId) {
        viewModel.addStoryNode(storyId, parentId, content);
    }

    @Override
    public void onVoteClicked(StoryNode node) {
        viewModel.voteForNode(storyId, node.getId());
        Toast.makeText(this, "Vote added!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddBranchClicked(StoryNode node) {
        AddNodeDialog dialog = AddNodeDialog.newInstance(node.getId(), node.getContent());
        dialog.show(getSupportFragmentManager(), "AddNodeDialog");
    }

    @Override
    public void onShowBranchesClicked(StoryNode node) {
        if (currentParentId != null) {
            parentHistory.push(currentParentId);
        }
        currentParentId = node.getId();
        loadStoryNodes();
        Toast.makeText(this, "Showing branches for: " + node.getContent().substring(0, Math.min(30, node.getContent().length())) + "...", Toast.LENGTH_SHORT).show();
    }
}