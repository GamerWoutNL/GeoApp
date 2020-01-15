package com.example.geoapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geoapp.R;
import com.example.geoapp.control.SharedPrefs;

public class MainActivity extends AppCompatActivity {

    private TextView runnrTextView;
    private final String appName = "Runnr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPrefs.deleteObject("MY_PREFS", "sessions");
        // RUN THIS TO DELETE THE SESSIONS

        ImageView goToMapButton = findViewById(R.id.GoToMapButton);
        ImageView goToStatsButton = findViewById(R.id.GoToStatsButton);
        runnrTextView = findViewById(R.id.RunnrTextView);
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

        startRunnrAnimation();
    }



    public void startRunnrAnimation(){
        Thread t = new Thread(() -> {
            try{
                while(true){
                    runnrTextView.post(() -> {
                        runnrTextView.setText("R");
                    });
                    Thread.sleep(350);
                    runnrTextView.post(() -> {
                        runnrTextView.setText("RU");
                    });
                    Thread.sleep(350);
                    runnrTextView.post(() -> {
                        runnrTextView.setText("RUN");
                    });
                    Thread.sleep(350);
                    runnrTextView.post(() -> {
                        runnrTextView.setText("RUNN");
                    });
                    Thread.sleep(350);
                    runnrTextView.post(() -> {
                        runnrTextView.setText("RUNNR");
                    });
                    Thread.sleep(350);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        });
        t.start();
    }
}
