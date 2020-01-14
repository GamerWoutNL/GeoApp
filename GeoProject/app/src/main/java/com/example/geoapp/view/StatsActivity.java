package com.example.geoapp.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.geoapp.R;
import com.example.geoapp.adapter.CustomPagerAdapter;
import com.example.geoapp.control.SharedPrefs;
import com.example.geoapp.model.TrainingSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class StatsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        List<TrainingSession> trainingSessions = SharedPrefs.getObject("MY_PREFS", "sessions");

        if (trainingSessions == null) {
            trainingSessions = new ArrayList<>();
        }

        Collections.reverse(trainingSessions);

        // Instantiate a ViewPager and a PagerAdapter.
        ViewPager mPager = findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new CustomPagerAdapter(trainingSessions, this);
        mPager.setAdapter(pagerAdapter);
    }
}
