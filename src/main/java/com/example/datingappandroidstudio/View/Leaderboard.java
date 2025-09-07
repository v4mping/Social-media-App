package com.example.datingappandroidstudio.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.datingappandroidstudio.Controller.Controller;
import com.example.datingappandroidstudio.Model.UserProfile;
import com.example.datingappandroidstudio.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Leaderboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Leaderboard extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;

    public Leaderboard() {
        // Required empty public constructor
    }
    public static Leaderboard newInstance(String param1, String param2) {
        Leaderboard fragment = new Leaderboard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        Controller controller = Controller.getInstance();
        RecyclerView leaderboardRecyclerView = view.findViewById(R.id.leaderboardRecyclerView);
        Button refreshButton = view.findViewById(R.id.refreshButton);

        loadLeaderboard(controller, leaderboardRecyclerView);

        refreshButton.setOnClickListener(v -> {
            loadLeaderboard(controller, leaderboardRecyclerView);
        });

        return view;
    }

    private void loadLeaderboard(Controller controller, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        controller.fetchTopUsers(10, users -> {
            LeaderboardAdapter adapter = new LeaderboardAdapter(users);
            recyclerView.setAdapter(adapter);
        }, e -> {
            Log.e("Leaderboard", "Error loading leaderboard", e);
        });
    }


}
