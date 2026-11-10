package com.example.mygymapp;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Repository class managing persistence of completed workout session history logs in SharedPreferences.
 */
public class WorkoutRepository {

    private static final String PREF_NAME = "gym_workout_prefs";
    private static final String KEY_WORKOUTS = "completed_workouts_set";
    private static final String KEY_CLEARED = "has_cleared_history";

    /**
     * Appends a new timestamped workout session log to storage.
     */
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

    /**
     * Retrieves saved workout logs, or sample records if no logs exist and history has not been cleared.
     */
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

        // Populate sample workouts if list is empty and user hasn't cleared history
        if (list.isEmpty() && !hasCleared) {
            list.add("Chest & Triceps (45 mins - Sample)");
            list.add("Leg Day (60 mins - Sample)");
        }

        return list;
    }

    /**
     * Removes all saved workout logs from storage.
     */
    public static void clearWorkouts(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
            .remove(KEY_WORKOUTS)
            .putBoolean(KEY_CLEARED, true)
            .apply();
    }
}
