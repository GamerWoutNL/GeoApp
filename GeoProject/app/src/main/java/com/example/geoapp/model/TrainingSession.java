package com.example.geoapp.model;

public class TrainingSession {

    private String time;
    private String date;
    private String elapsedTime;
    private double distanceCovered;

    public TrainingSession(String time, String date, float distanceCovered, String elapsedTime) {
        this.time = time;
        this.date = date;
        this.elapsedTime = elapsedTime;
        this.distanceCovered = distanceCovered;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public double getDistanceCovered() {
        return distanceCovered;
    }
}
