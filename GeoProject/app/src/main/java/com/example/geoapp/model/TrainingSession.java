package com.example.geoapp.model;

public class TrainingSession {

    private String time;
    private String date;
    private long elapsedTime;
    private float distanceCovered;

    public TrainingSession(String time, String date, float distanceCovered, long elapsedTimeMillis) {
        this.time = time;
        this.date = date;
        this.elapsedTime = elapsedTimeMillis / 1000;
        this.distanceCovered = distanceCovered;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public float getDistanceCovered() {
        return distanceCovered;
    }
}
