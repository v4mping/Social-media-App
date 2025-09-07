package com.example.datingappandroidstudio.Model;

import com.google.firebase.firestore.Blob;

import java.util.*;

public class UserProfile {
    private String uid;
    private String name;
    private String gender;
    private String location;
    private int age;
    private String height;
    private Prompt prompt1;
    private Prompt prompt2;
    private int rating;
    private List<String> matches = new ArrayList<>();

    private List<String> likes = new ArrayList<>();

    private List<String> rejects = new ArrayList<>();
    private List<java.util.Map.Entry<String,Integer>> reviews = new java.util.ArrayList<>();

    private Blob profileImageBlob;
    // Default constructor
    public UserProfile() {
        prompt1 = new Prompt(); // Initialize prompt1
        prompt2 = new Prompt(); // Initialize prompt2
        rating = 0;
        uid = "";
        name = "";
        gender = "";
        location = "";
        age = 0;
        height = "";
    }

    public UserProfile (String name, String gender, String location, int age, String height, Prompt prompt1, Prompt prompt2) {
        this.name = name;
        this.gender = gender;
        this.location = location;
        this.age = age;
        this.height = height;
        this.prompt1 = prompt1;
        this.prompt2 = prompt2;
        this.rating = 0;
    }

    public String getUid() {
        return uid;
    }

    public int getRating(){
        return rating;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    //Things keep crashing because values are null and we are trying to set things into firebase
    //This is a method I can apply to the setters to avoid that

    private String safeLower(String s) {
        return s != null ? s.toLowerCase() : "";
    }


    // Getter and setter methods for profile fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        // Add trimming and null check
        this.name = name != null ? name.trim() : "";
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = safeLower(gender);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = safeLower(location);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        // Add validation for negative age
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Prompt getPrompt1() {
        return prompt1;
    }

    public void setPrompt1(Prompt prompt1) {
        this.prompt1 = prompt1;
    }

    public Prompt getPrompt2() {
        return prompt2;
    }

    public void setPrompt2(Prompt prompt2) {
        this.prompt2 = prompt2;
    }

    public String getReviews() {
        return reviews.toString();
    }

    public void match(UserProfile model){
        matches.add(uid);
    }

    public List<String> getMatches(){
        return matches;
    }

    public void likes(UserProfile model){
        likes.add(uid);
    }

    public List<String> getLikes(){
        return likes;
    }

    public void rejects(UserProfile model){
        rejects.add(model.uid);
    }

    public List<String> getRejects(){
        return rejects;
    }

    public Blob getProfileImageBlob() {
        return profileImageBlob;
    }

    public void setProfileImageBlob(Blob profileImage) {
        this.profileImageBlob = profileImage;
    }
}

