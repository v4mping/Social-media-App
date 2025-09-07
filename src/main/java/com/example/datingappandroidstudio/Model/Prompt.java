package com.example.datingappandroidstudio.Model;

import java.util.*;

public class Prompt {
    private int promptIndex;  // Store the selected prompt index
    private String answer;
    private static final List<String> PROMPT_OPTIONS = Arrays.asList(
            "1. Two truths and a lie",
            "2. Funny joke"
    );

    public Prompt() {
        this.promptIndex = 0;
        this.answer = "";
    }


    public Prompt(int promptIndex, String answer) {
        this.answer = answer;
        this.promptIndex = promptIndex;
    }

    public int getPromptIndex() {
        return promptIndex;
    }

    public String getQuestionText() {
        return PROMPT_OPTIONS.get(promptIndex);
    }


    // Static method to get prompt options (no instance needed)
    public static List<String> getPromptOptions() {
        return new ArrayList<>(PROMPT_OPTIONS); // Return a copy
    }
    

    public void changeAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setPrompt(int index) {
        if (index >= 0 && index < PROMPT_OPTIONS.size()) {
            this.promptIndex = index;
        }
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}