package com.example.geoapp.view;

import com.example.geoapp.control.MyApplication;
import com.example.geoapp.model.TrainingSession;

public class SliderFragmentCustom extends SliderFragment {

    private TrainingSession session;

    public SliderFragmentCustom(TrainingSession session) {
        this.session = session;
    }

    public float getDistanceCovered() {
        return session.getDistanceCovered();
    }

    public String getTime() {
        return session.getTime();
    }

    public String getDate() {
        return session.getDate();
    }
}
