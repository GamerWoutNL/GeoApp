package com.example.geoapp.control;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPrefs {

    private static Gson gson = new Gson();

    public static void deleteObject(String token, String key) {
        SharedPreferences sharedPrefs = MyApplication.getAppContext().getSharedPreferences(token, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(key).apply();
    }

    public static <T> void addObject(String token, String key, T value) {
        SharedPreferences sharedPrefs = MyApplication.getAppContext().getSharedPreferences(token, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, gson.toJson(value)).apply();
    }

    public static <T> T getObject(String token, String key) {
        SharedPreferences sharedPrefs = MyApplication.getAppContext().getSharedPreferences(token, Context.MODE_PRIVATE);
        return (T)gson.fromJson(sharedPrefs.getString(key, null), Object.class);
    }
}