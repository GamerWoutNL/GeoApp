package com.example.geoapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.geoapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView goToMapButton = findViewById(R.id.GoToMapButton);
        ImageView goToStatsButton = findViewById(R.id.GoToStatsButton);

        if (Resources.getSystem().getConfiguration().locale.getLanguage().equals("nl")) {
            // Dutch language
            goToMapButton.setImageResource(R.drawable.button_ga_naar_map);
            goToStatsButton.setImageResource(R.drawable.button_ga_naar_stats);
        } else {
            // English language
            goToMapButton.setImageResource(R.drawable.button_go_to_map);
            goToStatsButton.setImageResource(R.drawable.button_go_to_stats);
        }

        goToMapButton.setOnClickListener((v) -> {
            startActivity(new Intent(v.getContext(), MapsActivity.class));
        });

        goToStatsButton.setOnClickListener((v) -> {
            startActivity(new Intent(v.getContext(), StatsActivity.class));
        });
    }
}
