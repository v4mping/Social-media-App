package com.example.datingappandroidstudio.View;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datingappandroidstudio.Model.Message;
import com.example.datingappandroidstudio.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Message> messages;
    private String currentUserId;

    public ChatAdapter(List<Message> messages, String currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public ViewHolder(View view) {
            super(view);
            messageText = view.findViewById(R.id.messageText);
        }
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageText.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
