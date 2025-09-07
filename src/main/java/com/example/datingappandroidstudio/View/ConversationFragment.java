package com.example.datingappandroidstudio.View;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datingappandroidstudio.Model.Message;
import com.example.datingappandroidstudio.R;

import java.util.ArrayList;
import java.util.List;

public class ConversationFragment extends Fragment {
    private static final String ARG_USER_ID = "userId";

    private String otherUserId;
    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;
    private List<Message> messages = new ArrayList<>();
    private ChatAdapter adapter;

    public static ConversationFragment newInstance(String userId) {
        ConversationFragment fragment = new ConversationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);

        if (getArguments() != null) {
            otherUserId = getArguments().getString(ARG_USER_ID);
        }

        recyclerView = view.findViewById(R.id.conversationRecyclerView);
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);

        String currentUserId = "me";

        adapter = new ChatAdapter(messages, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        loadMessages(); // Loads dummy messages

        sendButton.setOnClickListener(v -> {
            String text = messageInput.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text, currentUserId);
            }
        });

        return view;
    }

    private void loadMessages() {
        messages.add(new Message("me", "Hey there!", System.currentTimeMillis()));
        messages.add(new Message("them", "Hi! How's it going?", System.currentTimeMillis()));
        adapter.notifyDataSetChanged();
    }

    private void sendMessage(String text, String currentUserId) {
        Message msg = new Message(currentUserId, text, System.currentTimeMillis());
        messages.add(msg);
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);
        messageInput.setText("");


    }
}
