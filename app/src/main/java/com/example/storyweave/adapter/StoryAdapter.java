package com.example.storyweave.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storyweave.databinding.ItemStoryBinding;
import com.example.storyweave.model.Story;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StoryAdapter extends ListAdapter<Story, StoryAdapter.StoryViewHolder> {
    public StoryAdapter() {
        super(DIFF_CALLBACK);
    }

    static final DiffUtil.ItemCallback<Story> DIFF_CALLBACK = new DiffUtil.ItemCallback<Story>() {
        @Override
        public boolean areItemsTheSame(@NonNull Story oldItem, @NonNull Story newItem) {
            return oldItem.getInviteCode().equals(newItem.getInviteCode());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Story oldItem, @NonNull Story newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStoryBinding binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemStoryBinding binding;

        public StoryViewHolder(ItemStoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Story story) {
            binding.textTitle.setText(story.getTitle());
            binding.textDescription.setText(story.getDescription());
            binding.textDate.setText(formatDate(story.getCreatedAt()));
        }

        private String formatDate(long timestamp) {
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
            return sdf.format(date);
        }
    }
}
