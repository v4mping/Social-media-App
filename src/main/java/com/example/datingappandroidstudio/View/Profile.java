package com.example.datingappandroidstudio.View;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.datingappandroidstudio.Controller.Controller;
import com.example.datingappandroidstudio.R;
import com.google.firebase.firestore.Blob;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Controller controller = Controller.getInstance();

    private ImageView profileImageView;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;



    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            profileImageView.setImageURI(selectedImageUri);

                            //TODO: Save image uri to firebase storage
                        }
                    }
                });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileImageView = view.findViewById(R.id.profileImage);

        Blob profileBlob = controller.getUser().getProfileImageBlob();
        if (profileBlob != null) {
            byte[] imageBytes = profileBlob.toBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profileImageView.setImageBitmap(bitmap);
        }


        profileImageView.setOnClickListener(v -> openImagePicker());

        //Getting the references so we can put text
        TextView nameText = view.findViewById(R.id.nameText);
        TextView genderText = view.findViewById(R.id.genderText);
        TextView locationText = view.findViewById(R.id.locationText);
        TextView ageText = view.findViewById(R.id.ageText);
        TextView heightText = view.findViewById(R.id.heightText);
        TextView prompt1Text = view.findViewById(R.id.Prompt1Text);
        TextView prompt1Answer = view.findViewById(R.id.Prompt1Answer);
        TextView prompt2Text = view.findViewById(R.id.Prompt2Text);
        TextView prompt2Answer = view.findViewById(R.id.Prompt2Answer);
        Button editProfileButton = view.findViewById(R.id.editProfileButton);


        //Setting the text
        nameText.setText("Name: " + controller.getUser().getName());
        genderText.setText("Gender: " + controller.getUser().getGender());
        locationText.setText("Location: " + controller.getUser().getLocation());
        ageText.setText("Age: " + controller.getUser().getAge());
        heightText.setText("Height: " + controller.getUser().getHeight());
        prompt1Text.setText("Prompt 1: " + controller.getUser().getPrompt1().getQuestionText());
        prompt1Answer.setText("Prompt 1 Answer: " + controller.getUser().getPrompt1().getAnswer());
        prompt2Text.setText("Prompt 2: " + controller.getUser().getPrompt2().getQuestionText());
        prompt2Answer.setText("Prompt 2 Answer: " + controller.getUser().getPrompt2().getAnswer());

        editProfileButton.setOnClickListener(v -> {
            Fragment editProfileFragment = new EditProfileFragment();

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, editProfileFragment)
                    .commit();
        });
        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    //This should probably go in controller idk im just trying stuff rn
    public Uri getSelectedImageUri() {
        return selectedImageUri;
    }

}