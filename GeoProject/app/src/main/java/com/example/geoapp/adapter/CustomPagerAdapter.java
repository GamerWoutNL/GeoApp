package com.example.geoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.geoapp.R;
import com.example.geoapp.model.TrainingSession;

import java.util.ArrayList;
import java.util.List;

public class CustomPagerAdapter extends PagerAdapter {

    private List<TrainingSession> sessions;
    private Context context;

    public CustomPagerAdapter(List<TrainingSession> sessions, Context context) {
        this.sessions = sessions;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (sessions.size() > 10) {
            return 10;
        }
        return sessions.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        TextView distanceCovered = view.findViewById(R.id.distanceTextView);
        TextView date = view.findViewById(R.id.dateTextView);
        TextView time = view.findViewById(R.id.timeTextView);
        TextView elapsedTime = view.findViewById(R.id.elapsedTimeTextView);

        distanceCovered.setText(String.valueOf(sessions.get(position).getDistanceCovered()));
        date.setText(String.valueOf(sessions.get(position).getDate()));
        time.setText(String.valueOf(sessions.get(position).getTime()));
        elapsedTime.setText(String.valueOf(sessions.get(position).getElapsedTime()));

        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
