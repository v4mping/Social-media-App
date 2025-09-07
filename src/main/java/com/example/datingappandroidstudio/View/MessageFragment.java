package com.example.datingappandroidstudio.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datingappandroidstudio.Model.Conversation;
import com.example.datingappandroidstudio.R;
import com.example.datingappandroidstudio.View.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Conversation> conversationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Example data
        conversationList = new ArrayList<>();
        conversationList.add(new Conversation("Rui","Rui", "Hey there!"));
        conversationList.add(new Conversation("Pete", "Pete", "Hey there!"));

        messageAdapter = new MessageAdapter(conversationList);
        recyclerView.setAdapter(messageAdapter);

        return view;
    }
}
