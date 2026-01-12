package com.example.projet_front.api;

public class ChatResponse {
    private String question;
    private String answer;
    private String timestamp;
    private boolean hasError;

    // Constructeurs
    public ChatResponse() {
    }

    public ChatResponse(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    // Getters et Setters
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }
}