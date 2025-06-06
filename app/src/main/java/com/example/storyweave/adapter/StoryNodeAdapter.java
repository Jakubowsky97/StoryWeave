package com.example.storyweave.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storyweave.R;
import com.example.storyweave.model.StoryNode;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StoryNodeAdapter extends ListAdapter<StoryNode, StoryNodeAdapter.StoryNodeViewHolder> {

    public interface OnNodeActionListener {
        void onVoteClicked(StoryNode node);
        void onAddBranchClicked(StoryNode node);
        void onShowBranchesClicked(StoryNode node);
    }

    private OnNodeActionListener listener;

    public StoryNodeAdapter(OnNodeActionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story_node, parent, false);
        return new StoryNodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryNodeViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class StoryNodeViewHolder extends RecyclerView.ViewHolder {
        private final TextView textNodeContent;
        private final TextView textNodeMeta;
        private final TextView textVotes;
        private final Button btnVote;
        private final Button btnAddBranch;
        private final Button btnShowBranches;
        private final FirebaseFirestore db = FirebaseFirestore.getInstance();

        public StoryNodeViewHolder(@NonNull View itemView) {
            super(itemView);
            textNodeContent = itemView.findViewById(R.id.textNodeContent);
            textNodeMeta = itemView.findViewById(R.id.textNodeMeta);
            textVotes = itemView.findViewById(R.id.textVotes);
            btnVote = itemView.findViewById(R.id.btnVote);
            btnAddBranch = itemView.findViewById(R.id.btnAddBranch);
            btnShowBranches = itemView.findViewById(R.id.btnShowBranches);
        }

        void bind(StoryNode node, OnNodeActionListener listener) {
            textNodeContent.setText(node.getContent());
            textVotes.setText("Votes: " + node.getVotes());
            textNodeMeta.setText("Author: loading... • " + formatDate(node.getTimestamp()));

            // Pobierz nazwę użytkownika z Firestore
            db.collection("users").document(node.getAuthorId())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String uname = documentSnapshot.getString("email");
                            if (uname == null) uname = "Unknown";
                            textNodeMeta.setText("Author: " + uname.split("@")[0] + " • " + formatDate(node.getTimestamp()));
                        }
                    })
                    .addOnFailureListener(e -> {
                        textNodeMeta.setText("Author: Unknown • " + formatDate(node.getTimestamp()));
                    });

            btnVote.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVoteClicked(node);
                }
            });

            btnAddBranch.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddBranchClicked(node);
                }
            });

            btnShowBranches.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onShowBranchesClicked(node);
                }
            });
        }

        private String formatDate(long timestamp) {
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
            return sdf.format(date);
        }
    }
}