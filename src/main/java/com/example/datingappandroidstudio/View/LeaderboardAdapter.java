package com.example.datingappandroidstudio.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.datingappandroidstudio.Model.UserProfile;

import java.util.List;

import com.example.datingappandroidstudio.R;


public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    private List<UserProfile> userList;

    public LeaderboardAdapter(List<UserProfile> userList) {
        this.userList = userList;
    }
/*
This whole class is pretty much to help show the profiles in the leaderboard correctly. This handles
things like positioning.
 */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rankText;
        public TextView nameText;
        public TextView pointsText;

        public ViewHolder(View view) {
            super(view);
            rankText = view.findViewById(R.id.rankText);
            nameText = view.findViewById(R.id.nameText);
            pointsText = view.findViewById(R.id.pointsText);
        }
    }

    @Override
    public LeaderboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserProfile user = userList.get(position);
        holder.rankText.setText((position + 1) + ".");
        holder.nameText.setText(user.getName());
        holder.pointsText.setText(user.getRating() + " pts");
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }
}
