package com.example.mygymapp;

import android.content.Context;
import android.content.SharedPreferences;

public class ProfileRepository {

    private static final String PREF_NAME = "gym_profile_prefs";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_GOAL = "user_goal";
    private static final String KEY_CURRENT_WEIGHT = "user_current_weight";
    private static final String KEY_GOAL_WEIGHT = "user_goal_weight";

    public static void saveProfile(Context context, String name, String goal, String currentWeight, String goalWeight) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
            .putString(KEY_NAME, name)
            .putString(KEY_GOAL, goal)
            .putString(KEY_CURRENT_WEIGHT, currentWeight)
            .putString(KEY_GOAL_WEIGHT, goalWeight)
            .apply();
    }

    public static String getName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_NAME, "John Doe");
    }

    public static String getGoal(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_GOAL, "Intermediate Athlete");
    }

    public static String getCurrentWeight(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_CURRENT_WEIGHT, "78 kg");
    }

    public static String getGoalWeight(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_GOAL_WEIGHT, "75 kg");
    }
}
