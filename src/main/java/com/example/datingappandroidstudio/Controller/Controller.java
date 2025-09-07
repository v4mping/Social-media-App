package com.example.datingappandroidstudio.Controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.datingappandroidstudio.Model.Model;
import com.example.datingappandroidstudio.Model.UserProfile;
import com.example.datingappandroidstudio.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Transaction;


public class Controller {
    private static Controller instance;
    private final Model model = Model.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();


    private Controller() {
    }

    /*
    This method loads a user profile from Firestore. The way that I am doing is I am  getting
    the authentication and the database associated with that login. Then I am getting the current
    user and grabbing its stuff. I have some error checking just in case the user does not exist.
     */
    public void loadUserProfileFromFirestore(
            OnSuccessListener<UserProfile> onSuccess,
            OnFailureListener onFailure
    ) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) {
                        onFailure.onFailure(new Exception("Profile not found"));
                        return;
                    }

                    // 1) Create a fresh UserProfile
                    UserProfile user = new UserProfile();

                    // 2) Simple fields
                    user.setName(    snapshot.getString("name"));
                    Long ageLong = snapshot.getLong("age");
                    if (ageLong != null) user.setAge(ageLong.intValue());
                    user.setGender(  snapshot.getString("gender"));
                    user.setLocation(snapshot.getString("location"));
                    user.setHeight(  snapshot.getString("height"));

                    // 3) PROMPT 1
                    Object p1 = snapshot.get("prompt1");
                    if (p1 instanceof String) {
                        // old‐style
                        user.getPrompt1().setAnswer((String) p1);
                    } else if (p1 instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String,Object> m = (Map<String,Object>) p1;
                        Number idx = (Number) m.get("promptIndex");
                        if (idx != null) user.getPrompt1().setPrompt(idx.intValue());
                        String ans = (String) m.get("answer");
                        if (ans != null) user.getPrompt1().setAnswer(ans);
                    }

                    // 4) PROMPT 2
                    Object p2 = snapshot.get("prompt2");
                    if (p2 instanceof String) {
                        user.getPrompt2().setAnswer((String) p2);
                    } else if (p2 instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String,Object> m = (Map<String,Object>) p2;
                        Number idx = (Number) m.get("promptIndex");
                        if (idx != null) user.getPrompt2().setPrompt(idx.intValue());
                        String ans = (String) m.get("answer");
                        if (ans != null) user.getPrompt2().setAnswer(ans);
                    }

                    // 5) Image Blob
                    Blob blob = snapshot.getBlob("profileImage");
                    if (blob != null) {
                        user.setProfileImageBlob(blob);
                    }

                    // 6) Store in model & notify
                    model.setUserModel(user);
                    onSuccess.onSuccess(user);
                })
                .addOnFailureListener(onFailure);
    }



/*
Here I am saving the profile to firestore. I might be doing it a little slow because I am doing
every field one by one but I just mapping it to firestore so that it can be saved.
 */
    public void saveProfileToFirestore(UserProfile user,
                                       @Nullable Blob newImageBlob,
                                       Runnable onSuccess,
                                       OnFailureListener onFailure) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> data = new HashMap<>();
        data.put("name",     user.getName());
        data.put("gender",   user.getGender());
        data.put("location", user.getLocation());
        data.put("age",      user.getAge());
        data.put("height",   user.getHeight());

        Map<String,Object> p1 = new HashMap<>();
        p1.put("promptIndex", user.getPrompt1().getPromptIndex());
        p1.put("answer",      user.getPrompt1().getAnswer());
        data.put("prompt1", p1);

        Map<String,Object> p2 = new HashMap<>();
        p2.put("promptIndex", user.getPrompt2().getPromptIndex());
        p2.put("answer",      user.getPrompt2().getAnswer());
        data.put("prompt2", p2);


        // Preserve old blob if no new one was picked
        Blob blobToSave = newImageBlob != null
                ? newImageBlob
                : user.getProfileImageBlob();

        if (blobToSave != null) {
            data.put("profileImage", blobToSave);
        }

        // Now write, merging so nothing else is accidentally dropped
        db.collection("users")
                .document(userId)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(a -> onSuccess.run())
                .addOnFailureListener(onFailure);
    }


/*
Next two methods Rui gave them to me. They are for storing images and retrieving them. This was
a challenge because we have limited storage to work with so we need to store them as blobs.
 */
    public Blob getBlobFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
        return Blob.fromBytes(stream.toByteArray());
    }

    public Bitmap getBitmapFromBlob(Blob imageBlob) {
        byte[] bytes = imageBlob.toBytes();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public UserProfile getUser() {
        return model.getUserModel();
    }

    /*
    This will get a userProfile for when it might be needed. The difference between this method
    and the loadUserProfile method is that this method is smaller for when we do not need to lead
    a whole profile with all of its things.
     */
    public void fetchUserProfile(Consumer<List<UserProfile>> onSuccess, Consumer<Exception> onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Add null check for current user
        if (auth.getCurrentUser() == null) {
            onFailure.accept(new Exception("No authenticated user"));
            return;
        }
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<UserProfile> profiles = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        if (!doc.getId().equals(currentUserId)) {
                            UserProfile profile = doc.toObject(UserProfile.class);
                            if (profile != null) {
                                profile.setUid(doc.getId());//  Set UID from document ID
                                Blob imageBlob = doc.get("profileImage", Blob.class);
                                profile.setProfileImageBlob(imageBlob);
                                profiles.add(profile);
                            }
                        }
                    }
                    onSuccess.accept(profiles);
                })
                .addOnFailureListener(onFailure::accept);
    }

    /*
    Increment the rating whenever we match with someone. This is for the leaderboard fragment.
    Pretty simple code.
     */
    public void incrementRating (String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(uid);

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot snapshot = transaction.get(userRef);;
            long currentRating = snapshot.contains("rating") ? snapshot.getLong("rating") : 0;
            transaction.update(userRef, "rating", currentRating + 1);
            return null;
        }).addOnSuccessListener(aVoid -> {
            Log.d("Rating", "Rating incremented successfully");
        }).addOnFailureListener(e -> {
            Log.e("Rating", "Error incrementing rating", e);
        });
    }

    /*
    Another for the leaderboard fragment. This actually goes through the database and, thankfully,
    there exists a way to order the database in descending order so that we can then just fetch
    the top users without us manually having to sort them here.
     */
    public void fetchTopUsers(int limit, OnSuccessListener<List<UserProfile>> onSuccess, OnFailureListener onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .orderBy("rating", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<UserProfile> topUsers = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        UserProfile user = doc.toObject(UserProfile.class);
                        if (user != null) {
                            Blob imageBlob = doc.get("profileImage", Blob.class);
                            user.setProfileImageBlob(imageBlob);
                            topUsers.add(user);
                        }
                    }
                    onSuccess.onSuccess(topUsers);
    })
                .addOnFailureListener(onFailure);
    }
}
