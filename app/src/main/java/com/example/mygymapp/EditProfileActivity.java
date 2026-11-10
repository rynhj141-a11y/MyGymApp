package com.example.mygymapp;

import com.example.mygymapp.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for editing user profile details including name, goal, and weight metrics.
 */
public class EditProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Bind input fields and action buttons
        EditText etName = findViewById(R.id.et_edit_name);
        EditText etGoal = findViewById(R.id.et_edit_goal);
        EditText etCurrentWeight = findViewById(R.id.et_edit_current_weight);
        EditText etGoalWeight = findViewById(R.id.et_edit_goal_weight);
        Button btnSave = findViewById(R.id.btn_save_profile);
        Button btnCancel = findViewById(R.id.btn_cancel_profile);

        // Pre-fill fields with existing values passed via Intent extras
        String currentName = getIntent().getStringExtra("CURRENT_NAME");
        String currentGoal = getIntent().getStringExtra("CURRENT_GOAL");
        String currentWeight = getIntent().getStringExtra("CURRENT_WEIGHT");
        String goalWeight = getIntent().getStringExtra("GOAL_WEIGHT");

        if (currentName != null) etName.setText(currentName);
        if (currentGoal != null) etGoal.setText(currentGoal);
        if (currentWeight != null) etCurrentWeight.setText(currentWeight);
        if (goalWeight != null) etGoalWeight.setText(goalWeight);

        // Save button listener: collects edited fields and passes result back to ProfileActivity
        btnSave.setOnClickListener(v -> {
            String updatedName = etName.getText().toString().trim();
            String updatedGoal = etGoal.getText().toString().trim();
            String updatedCurrentWeight = etCurrentWeight.getText().toString().trim();
            String updatedGoalWeight = etGoalWeight.getText().toString().trim();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("UPDATED_NAME", updatedName);
            resultIntent.putExtra("UPDATED_GOAL", updatedGoal);
            resultIntent.putExtra("UPDATED_CURRENT_WEIGHT", updatedCurrentWeight);
            resultIntent.putExtra("UPDATED_GOAL_WEIGHT", updatedGoalWeight);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Cancel button listener: closes screen without saving changes
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}
