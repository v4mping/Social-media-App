package com.example.datingappandroidstudio.Model;

public class Model {
    private static Model instance;
    private UserProfile userModel;

    //I think we need to add the user profile here. If so, add Uri for image
    private Model() {
        userModel = new UserProfile("Angel", "boy", "Colorado", 20, "5'2", new Prompt(0, "Hello"), new Prompt(1, "Goodbye"));
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public UserProfile getUserModel() {
        return userModel;
    }

    public void setUserModel(UserProfile userModel) {
        this.userModel = userModel;
    }
}