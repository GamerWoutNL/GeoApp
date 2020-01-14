package com.example.geoapp.view;

public class SliderFragmentCustom extends SliderFragment {

    public long distanceCovered;
    public long time;
    public String date;

    public SliderFragmentCustom(long distanceCovered,long time, String date) {
        this.distanceCovered = distanceCovered;
        this.time = time;
        this.date = date;
    }

    public long getDistanceCovered() {
        return distanceCovered;
    }

    public long getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
