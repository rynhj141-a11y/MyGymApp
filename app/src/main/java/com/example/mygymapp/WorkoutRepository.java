package com.example.mygymapp;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class WorkoutRepository {

    private static final String PREF_NAME = "gym_workout_prefs";
    private static final String KEY_WORKOUTS = "completed_workouts_set";
    private static final String KEY_CLEARED = "has_cleared_history";

    public static void saveWorkout(Context context, String workoutLog) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet(KEY_WORKOUTS, new HashSet<>());
        Set<String> newSet = new HashSet<>(set);
        newSet.add(System.currentTimeMillis() + "::" + workoutLog);

        prefs.edit()
            .putStringSet(KEY_WORKOUTS, newSet)
            .putBoolean(KEY_CLEARED, false)
            .apply();
    }

    public static ArrayList<String> getWorkouts(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean hasCleared = prefs.getBoolean(KEY_CLEARED, false);
        Set<String> set = prefs.getStringSet(KEY_WORKOUTS, new HashSet<>());
        ArrayList<String> list = new ArrayList<>();

        if (set != null) {
            for (String raw : set) {
                String[] parts = raw.split("::", 2);
                if (parts.length == 2) {
                    list.add(parts[1]);
                } else {
                    list.add(raw);
                }
            }
        }

        if (list.isEmpty() && !hasCleared) {
            list.add("Chest & Triceps (45 mins - Sample)");
            list.add("Leg Day (60 mins - Sample)");
        }

        return list;
    }

    public static void clearWorkouts(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
            .remove(KEY_WORKOUTS)
            .putBoolean(KEY_CLEARED, true)
            .apply();
    }
}
