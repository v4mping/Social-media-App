package com.example.datingappandroidstudio.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datingappandroidstudio.Model.Conversation;
import com.example.datingappandroidstudio.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Conversation> conversations;

    public MessageAdapter(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, messageTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textName);
            messageTextView = itemView.findViewById(R.id.textMessage);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conversation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Conversation convo = conversations.get(position);
        holder.nameTextView.setText(convo.getName());
        holder.messageTextView.setText(convo.getLastMessage());

        holder.itemView.setOnClickListener(v -> {
            String userId = convo.getUserId(); // Now this will work
            Fragment fragment = ConversationFragment.newInstance(userId);

            ((FragmentActivity) v.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null) // This enables "back" navigation
                    .commit();
        });



    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }
}
