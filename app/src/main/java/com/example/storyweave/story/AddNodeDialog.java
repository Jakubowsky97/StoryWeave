package com.example.storyweave.story;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.storyweave.R;

public class AddNodeDialog extends DialogFragment {

    public interface OnNodeAddedListener {
        void onNodeAdded(String content, String parentId);
    }

    private OnNodeAddedListener listener;
    private String parentId;
    private String parentContent;
    private EditText editContent;
    private TextView textParent;

    public static AddNodeDialog newInstance(String parentId, String parentContent) {
        AddNodeDialog dialog = new AddNodeDialog();
        Bundle args = new Bundle();
        args.putString("parentId", parentId);
        args.putString("parentContent", parentContent);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentId = getArguments().getString("parentId");
            parentContent = getArguments().getString("parentContent");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_node, container, false);

        textParent = view.findViewById(R.id.textParentContent);
        editContent = view.findViewById(R.id.editNewContent);
        Button btnAdd = view.findViewById(R.id.btnAddNode);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        if (parentContent != null && !parentContent.isEmpty()) {
            textParent.setText("Kontynuując: \"" + parentContent + "\"");
            textParent.setVisibility(View.VISIBLE);
        } else {
            textParent.setVisibility(View.GONE);
        }

        btnAdd.setOnClickListener(v -> {
            String content = editContent.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(getContext(), "Wpisz treść fragmentu", Toast.LENGTH_SHORT).show();
                return;
            }

            if (listener != null) {
                listener.onNodeAdded(content, parentId);
            }
            dismiss();
        });

        btnCancel.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnNodeAddedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnNodeAddedListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}