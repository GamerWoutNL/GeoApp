package com.example.geoapp.control;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.geoapp.model.TrainingSession;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefs {

    private static Gson gson = new Gson();

    public static void deleteObject(String token, String key) {
        SharedPreferences sharedPrefs = MyApplication.getAppContext().getSharedPreferences(token, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(key).apply();
    }

    public static void addObject(String token, String key, List<TrainingSession> value) {
        SharedPreferences sharedPrefs = MyApplication.getAppContext().getSharedPreferences(token, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, gson.toJson(value)).apply();
    }

    public static List<TrainingSession> getObject(String token, String key) {
        SharedPreferences sharedPrefs = MyApplication.getAppContext().getSharedPreferences(token, Context.MODE_PRIVATE);
        return gson.fromJson(sharedPrefs.getString(key, null), new TypeToken<ArrayList<TrainingSession>>(){}.getType());
    }
}