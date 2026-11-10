package com.example.mygymapp;

import com.example.mygymapp.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Activity for displaying exercise categories and custom exercises in a list view.
 * Enables selection of exercises to start detail sessions and adding custom exercises.
 */
public class ListActivity extends AppCompatActivity {

    private ArrayList<String> exerciseList;
    private ArrayAdapter<String> adapter;
    private ActivityResultLauncher<Intent> addExerciseLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Bind layout views
        TextView tvHeader = findViewById(R.id.tv_list_header);
        ListView lvExercises = findViewById(R.id.lv_exercises);
        Button btnAddCustom = findViewById(R.id.btn_add_custom_exercise);
        Button btnBackMain = findViewById(R.id.btn_back_main);

        // Display category passed via intent extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String category = extras.getString("CATEGORY");
            if (category != null && tvHeader != null) {
                tvHeader.setText(category.toUpperCase());
            }
        }

        // Initial default exercise list
        exerciseList = new ArrayList<>(Arrays.asList(
            "Barbell Bench Press", "Squat", "Pull-ups", 
            "Dumbbell Shoulder Press", "Cable Bicep Curl"
        ));

        // Custom array adapter with dark background item formatting
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, exerciseList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextColor(android.content.res.ColorStateList.valueOf(Color.WHITE));
                view.setTextSize(16);
                view.setPadding(32, 24, 32, 24);
                view.setBackgroundColor(Color.parseColor("#161616"));
                return view;
            }
        };
        lvExercises.setAdapter(adapter);

        // Register result launcher to handle newly added custom exercise from AddExerciseActivity
        addExerciseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String newExercise = result.getData().getStringExtra("NEW_EXERCISE");
                    if (newExercise != null && !newExercise.isEmpty()) {
                        exerciseList.add(newExercise);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Added: " + newExercise, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );

        // Add Custom Exercise button listener: launches AddExerciseActivity
        btnAddCustom.setOnClickListener(v -> {
            Intent intent = new Intent(ListActivity.this, AddExerciseActivity.class);
            addExerciseLauncher.launch(intent);
        });

        // Navigation back button listener
        if (btnBackMain != null) {
            btnBackMain.setOnClickListener(v -> finish());
        }

        // Exercise item selection listener: opens DetailActivity for selected exercise
        lvExercises.setOnItemClickListener((parent, view, position, id) -> {
            String exerciseName = exerciseList.get(position);
            Intent intent = new Intent(ListActivity.this, DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("EXERCISE_NAME", exerciseName);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}
