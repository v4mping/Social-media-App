package com.example.datingappandroidstudio.View;

import static com.example.datingappandroidstudio.Model.Prompt.getPromptOptions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.datingappandroidstudio.Controller.Controller;
import com.example.datingappandroidstudio.Model.UserProfile;
import com.example.datingappandroidstudio.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.Blob;

import java.util.*;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Controller controller = Controller.getInstance();

    private Button nameButton, ageButton, heightButton, genderButton, locationButton, prompt1Button, prompt2Button;
    private TextInputEditText nameField, genderField, ageField, heightField, locationField, prompt1Field, prompt2Field;

    private ImageView profileImageView;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;
    private Button saveButton;
    private Blob currentImageBlob;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
/*
This method is just the standard onCreate, but I added some stuff to help with the image storing.
It is kinda a hassle going from blob to bitmap but this helps.
 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            profileImageView.setImageURI(selectedImageUri);

                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                                currentImageBlob = controller.getBlobFromBitmap(bitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

    }
/*
This creates the actual view. Everything from buttons to spinners this does it. It then also edits
the things in the UI at the actual xml code.
 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        profileImageView = view.findViewById(R.id.profileImage);

        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> onSaveClicked());
        profileImageView.setOnClickListener(v -> openImagePicker());

        Spinner dropdown = view.findViewById(R.id.prompt1view);
        Spinner dropdown2 = view.findViewById(R.id.prompt2view);
        ageButton = view.findViewById(R.id.AgeButton);
        genderButton = view.findViewById(R.id.Genderbutton);
        heightButton = view.findViewById(R.id.HeightButton);
        locationButton = view.findViewById(R.id.Locationbutton);
        nameButton = view.findViewById(R.id.Namebutton);
        prompt1Button = view.findViewById(R.id.Prompt1Button);
        prompt2Button = view.findViewById(R.id.Prompt2Button);
        nameField = view.findViewById(R.id.NameEditText);
        genderField = view.findViewById(R.id.GenderEditText);
        locationField = view.findViewById(R.id.LocationEditText);
        ageField = view.findViewById(R.id.AgeEditText);
        heightField = view.findViewById(R.id.HeightEditText);
        prompt1Field = view.findViewById(R.id.Prompt1EditText);
        prompt2Field = view.findViewById(R.id.Prompt2EditText);


        // Spinner data
        List<String> promptOptions = getPromptOptions();
        String extraItem = "Select a prompt";

        String[] items = new String[promptOptions.size() + 1];

        items[0] = extraItem;

        for (int i = 0; i < promptOptions.size(); i++) {
            items[i + 1] = promptOptions.get(i);
        }

        // Adapter to populate spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown2.setAdapter(adapter);

        ageButton.setOnClickListener(v -> {
            String ageInput = ageField.getText().toString().trim();

            if (ageInput.isEmpty()) {
                ageField.setError("Please enter your age");
                return;
            }

            try {
                int age = Integer.parseInt(ageInput);
                if (age < 0) {
                    ageField.setError("Age cannot be negative");
                } else {
                    controller.getUser().setAge(age);
                }
            } catch (NumberFormatException e) {
                ageField.setError("Please enter a valid number");
            }
        });

        heightButton.setOnClickListener(v -> {
            String heightInput = heightField.getText().toString().trim();

            if (heightInput.isEmpty()) {
                heightField.setError("Please enter your height");
                return;
            }

            try {
                int height = Integer.parseInt(heightInput);
                if (height < 30 || height > 90) {
                    heightField.setError("Please enter a realistic height (30–90 inches)");
                } else {
                    controller.getUser().setHeight("" + height);
                }
            } catch (NumberFormatException e) {
                heightField.setError("Please enter a valid number");
            }
        });

        genderButton.setOnClickListener(v -> {
            controller.getUser().setGender(genderField.getText().toString().trim());
        });

        nameButton.setOnClickListener(v -> {
            controller.getUser().setName(nameField.getText().toString().trim());
        });

        locationButton.setOnClickListener(v -> {
            controller.getUser().setLocation(locationField.getText().toString().trim());
        });
        prompt1Button.setOnClickListener(v -> {
            controller.getUser().getPrompt1().changeAnswer(prompt1Field.getText().toString().trim());
        });
        prompt2Button.setOnClickListener(v -> {
            controller.getUser().getPrompt2().changeAnswer(prompt2Field.getText().toString().trim());
        });


        boolean[] isUserInteracting1 = {false};
        boolean[] isUserInteracting2 = {false};

        dropdown.setOnTouchListener((v, event) -> {
            isUserInteracting1[0] = true;
            return false;
        });

        dropdown2.setOnTouchListener((v, event) -> {
            isUserInteracting2[0] = true;
            return false;
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (isUserInteracting1[0]) {
                    String selectedItem = parentView.getItemAtPosition(position).toString();
                    char firstChar = selectedItem.charAt(0);
                    int charAsInt = Character.getNumericValue(firstChar) - 1;
                    controller.getUser().getPrompt1().setPrompt(charAsInt);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        dropdown2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (isUserInteracting2[0]) {
                    String selectedItem = parentView.getItemAtPosition(position).toString();
                    char firstChar = selectedItem.charAt(0);
                    int charAsInt = Character.getNumericValue(firstChar) - 1;
                    controller.getUser().getPrompt2().setPrompt(charAsInt);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        // Return the fully set-up view
        return view;
    }

    /*
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadAndPopulateProfile();
    }

    /*
    This just adds functionality to the save buttons so that they actually save the information
    to user profile which is where we store the users details.
     */
    private void onSaveClicked() {
        UserProfile user = controller.getUser();

        user.setName(nameField.getText().toString().trim());
        user.setGender(genderField.getText().toString().trim());
        user.setLocation(locationField.getText().toString().trim());
        user.setAge(Integer.parseInt(ageField.getText().toString().trim()));
        user.setHeight(heightField.getText().toString().trim());
        user.getPrompt1().setAnswer(prompt1Field.getText().toString().trim());
        user.getPrompt2().setAnswer(prompt2Field.getText().toString().trim());

        if (nameField.getText() == null) {
            nameField.setError("Please enter your name");
            return;
        }
        if (genderField.getText() == null) {
            genderField.setError("Please enter your gender");
            return;
        }
        if (locationField.getText() == null) {
            locationField.setError("Please enter your location");
            return;
        }
        if (prompt1Field.getText() == null) {
            prompt1Field.setError("Please enter your first prompt answer");
            return;
        }
        if (prompt2Field.getText() == null) {
            prompt2Field.setError("Please enter your second prompt answer");
            return;
        }
        if (selectedImageUri == null) {
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageField.getText().toString().trim());
            if (age < 0) {
                ageField.setError("Age cannot be negative");
                return;
            }
        } catch (NumberFormatException e) {
            ageField.setError("Please enter a valid number");
            return;
        }

        int height;
        try {
            height = Integer.parseInt(heightField.getText().toString().trim());
            if (height < 0) {
                heightField.setError("Height cannot be negative");
                return;
            }
        } catch (NumberFormatException e) {
            heightField.setError("Please enter a valid number");
            }

        Blob imageBlob = currentImageBlob;
        if (selectedImageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
                imageBlob = controller.getBlobFromBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        controller.saveProfileToFirestore(user, imageBlob,
                () -> {
                    Toast.makeText(requireContext(), "Profile saved", Toast.LENGTH_SHORT).show();

                    MainActivity activity = (MainActivity) requireActivity();
                    activity.navigateToFragment(new MainFragment());
                    activity.setupBottomNavListeners();
                },
                e -> Toast.makeText(requireContext(), "Error saving profile", Toast.LENGTH_SHORT).show());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    /*
    This method gets the profile and loads it. This is so that the user has their progress saved
    whenever they open up the edit profile tab. That helps because they do not have to type
    everything again.
     */
    private void loadAndPopulateProfile() {
        controller.loadUserProfileFromFirestore(
                user -> {
                    nameField.setText(user.getName());
                    ageField.setText(String.valueOf(user.getAge()));
                    heightField.setText(String.valueOf(user.getHeight()));
                    genderField.setText(user.getGender());
                    locationField.setText(user.getLocation());
                    prompt1Field.setText(user.getPrompt1().getAnswer());
                    prompt2Field.setText(user.getPrompt2().getAnswer());

                    Blob blop = user.getProfileImageBlob();
                    if (blop != null) {
                        Bitmap bmp = controller.getBitmapFromBlob(blop);
                        profileImageView.setImageBitmap(bmp);
                    }
                },
                error -> {
                    Toast.makeText(requireContext(), "Error loading profile" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );
    }
}

