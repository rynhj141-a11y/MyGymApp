package com.example.mygymapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for adding a new exercise to the workout library list.
 */
public class AddExerciseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        // Initialize UI view components
        EditText etNewName = findViewById(R.id.et_new_exercise_name);
        Button btnSave = findViewById(R.id.btn_save_exercise);
        Button btnCancel = findViewById(R.id.btn_cancel_exercise);

        // Save button listener: validates input and passes new exercise name back to calling Activity
        btnSave.setOnClickListener(v -> {
            String name = etNewName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter an exercise name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Prepare intent with data and set result as OK
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_EXERCISE", name);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Cancel button listener: cancels action and closes Activity without saving
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}
