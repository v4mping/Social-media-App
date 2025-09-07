package com.example.datingappandroidstudio.Model;
public class Conversation {
    private String userId;
    private String name;
    private String lastMessage;

    // Constructor
    public Conversation(String userId, String name, String lastMessage) {
        this.userId = userId;
        this.name = name;
        this.lastMessage = lastMessage;
    }

    // Getter for userId
    public String getUserId() {
        return userId;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for lastMessage
    public String getLastMessage() {
        return lastMessage;
    }
}
