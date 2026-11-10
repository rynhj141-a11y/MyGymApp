package com.example.mygymapp;

import com.example.mygymapp.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinnerRoutines = findViewById(R.id.spinner_routines);
        Button btnStartQuick = findViewById(R.id.btn_start_quick);
        Button btnBrowseRoutines = findViewById(R.id.btn_browse_routines);
        Button btnHistory = findViewById(R.id.btn_history);
        Button btnProfile = findViewById(R.id.btn_profile);

        String[] routines = {"Chest & Triceps", "Back & Biceps", "Leg Day", "Full Body"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, routines) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextColor(Color.WHITE);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                view.setTextColor(Color.WHITE);
                view.setBackgroundColor(Color.parseColor("#161616"));
                return view;
            }
        };
        spinnerRoutines.setAdapter(adapter);

        btnStartQuick.setOnClickListener(v -> {
            String selectedRoutine = spinnerRoutines.getSelectedItem().toString();
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("ROUTINE_NAME", selectedRoutine);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnBrowseRoutines.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("CATEGORY", "Exercise Library");
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("USER_ID", "USER_123");
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("USER_NAME", "John Doe");
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}
