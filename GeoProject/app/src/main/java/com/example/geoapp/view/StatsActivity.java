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
import java.util.List;

public class StatsActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */


    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;


    private ArrayList<TrainingSession> trainingSessions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        this.trainingSessions = SharedPrefs.getObject("MY_PREFS", "sessions");

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.viewPager);
        pagerAdapter = new CustomPagerAdapter(trainingSessions,this);
        mPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
