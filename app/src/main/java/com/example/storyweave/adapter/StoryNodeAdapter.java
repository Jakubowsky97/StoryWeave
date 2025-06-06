package com.example.storyweave.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storyweave.databinding.ItemStoryNodeBinding;
import com.example.storyweave.model.StoryNode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StoryNodeAdapter extends ListAdapter<StoryNode, StoryNodeAdapter.StoryNodeViewHolder> {

    public StoryNodeAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<StoryNode> DIFF_CALLBACK = new DiffUtil.ItemCallback<StoryNode>() {
        @Override
        public boolean areItemsTheSame(@NonNull StoryNode oldItem, @NonNull StoryNode newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull StoryNode oldItem, @NonNull StoryNode newItem) {
            return oldItem.getContent().equals(newItem.getContent())
                    && oldItem.getAuthorId().equals(newItem.getAuthorId())
                    && oldItem.getTimestamp() == newItem.getTimestamp()
                    && oldItem.getVotes() == newItem.getVotes()
                    && ((oldItem.getParentId() == null && newItem.getParentId() == null)
                    || (oldItem.getParentId() != null && oldItem.getParentId().equals(newItem.getParentId())));
        }
    };

    @NonNull
    @Override
    public StoryNodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStoryNodeBinding binding = ItemStoryNodeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StoryNodeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryNodeViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class StoryNodeViewHolder extends RecyclerView.ViewHolder {
        private final ItemStoryNodeBinding binding;

        public StoryNodeViewHolder(ItemStoryNodeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(StoryNode node) {
            binding.textNodeContent.setText(node.getContent());
            binding.textNodeMeta.setText("Author: " + node.getAuthorId() + " • " + formatDate(node.getTimestamp()));
            binding.textVotes.setText("Votes: " + node.getVotes());
        }

        private String formatDate(long timestamp) {
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
            return sdf.format(date);
        }
    }
}
