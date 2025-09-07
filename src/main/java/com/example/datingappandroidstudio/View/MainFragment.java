package com.example.datingappandroidstudio.View;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.datingappandroidstudio.Controller.Controller;
import com.example.datingappandroidstudio.Model.UserProfile;
import com.example.datingappandroidstudio.Model.Prompt;
import com.example.datingappandroidstudio.R;
import com.google.firebase.firestore.Blob;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */

public class MainFragment extends Fragment {


        private Controller controller;
        private TextView nameText, detailsText, prompt1Text, prompt2Text, answer1Text, answer2Text;
        private Button rejectButton, matchButton;

        private ImageView profileImage;

        private ArrayList<UserProfile> potentialMatches = new ArrayList<>();
        private int currentIndex = 0;

    public MainFragment() {
}

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        nameText = view.findViewById(R.id.profileName);
        detailsText = view.findViewById(R.id.profileDetails);
        prompt1Text = view.findViewById(R.id.prompt1);
        answer1Text = view.findViewById(R.id.answer1);
        answer2Text = view.findViewById(R.id.answer2);
        prompt2Text = view.findViewById(R.id.prompt2);
        rejectButton = view.findViewById(R.id.rejectButton);
        matchButton = view.findViewById(R.id.matchButton);
        profileImage = view.findViewById(R.id.profileImage);

        controller = Controller.getInstance();
        controller.fetchUserProfile(
                profiles -> {
                    potentialMatches.clear();
                    potentialMatches.addAll(profiles);
                    updateUi();
                },
                error -> {
                    nameText.setText("Error fetching profiles");
                }
        );

        rejectButton.setOnClickListener(v -> {

            currentIndex++;
            updateUi();
        });

        matchButton.setOnClickListener(v -> {
            if (currentIndex < potentialMatches.size()) {
                UserProfile matchedUser = potentialMatches.get(currentIndex);
                controller.getUser().match(matchedUser);
                controller.incrementRating(matchedUser.getUid());
                currentIndex++;
                updateUi();
            }
        });
        controller = Controller.getInstance();
        return view;
    }
    private void updateUi() {
    if (currentIndex >= potentialMatches.size()) {
        nameText.setText("No more matches");
        detailsText.setText("");
        prompt1Text.setText("");
        prompt2Text.setText("");
        rejectButton.setEnabled(false);
        matchButton.setEnabled(false);
        return;
    }
    UserProfile Profile = potentialMatches.get(currentIndex);
    nameText.setText(Profile.getName());
    detailsText.setText(Profile.getGender() + ", " + Profile.getAge() + ", " + Profile.getLocation() + ", " + Profile.getHeight());
    prompt1Text.setText(Profile.getPrompt1().getQuestionText());
    answer1Text.setText(Profile.getPrompt1().getAnswer());
    prompt2Text.setText(Profile.getPrompt2().getQuestionText());
    answer2Text.setText(Profile.getPrompt2().getAnswer());

        Blob imageBlob = Profile.getProfileImageBlob();
        if (imageBlob != null) {
            Bitmap bitmap = controller.getBitmapFromBlob(imageBlob);
            profileImage.setImageBitmap(bitmap);
        } else {
            profileImage.setImageResource(R.drawable.default_profile); // fallback image
        }

    }
}