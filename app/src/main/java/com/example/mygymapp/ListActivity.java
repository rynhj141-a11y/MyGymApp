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

public class ListActivity extends AppCompatActivity {

    private ArrayList<String> exerciseList;
    private ArrayAdapter<String> adapter;
    private ActivityResultLauncher<Intent> addExerciseLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        TextView tvHeader = findViewById(R.id.tv_list_header);
        ListView lvExercises = findViewById(R.id.lv_exercises);
        Button btnAddCustom = findViewById(R.id.btn_add_custom_exercise);
        Button btnBackMain = findViewById(R.id.btn_back_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String category = extras.getString("CATEGORY");
            if (category != null && tvHeader != null) {
                tvHeader.setText(category.toUpperCase());
            }
        }

        exerciseList = new ArrayList<>(Arrays.asList(
            "Barbell Bench Press", "Squat", "Pull-ups", 
            "Dumbbell Shoulder Press", "Cable Bicep Curl"
        ));

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

        // Register ActivityResultLauncher for AddExerciseActivity
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

        btnAddCustom.setOnClickListener(v -> {
            Intent intent = new Intent(ListActivity.this, AddExerciseActivity.class);
            addExerciseLauncher.launch(intent);
        });

        if (btnBackMain != null) {
            btnBackMain.setOnClickListener(v -> finish());
        }

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
