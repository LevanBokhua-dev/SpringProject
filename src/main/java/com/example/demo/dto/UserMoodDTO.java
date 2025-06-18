package com.example.demo.dto;

public class UserMoodDTO {
    private String username;
    private String moodOfTheDay;

    public UserMoodDTO(String username, String moodOfTheDay) {
        this.username = username;
        this.moodOfTheDay = moodOfTheDay;
    }

    public String getUsername() {
        return username;
    }

    public String getMoodOfTheDay() {
        return moodOfTheDay;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMoodOfTheDay(String moodOfTheDay) {
        this.moodOfTheDay = moodOfTheDay;
    }
}
