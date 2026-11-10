package com.example.mygymapp;

import com.example.mygymapp.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName;
    private TextView tvUserLevel;
    private TextView tvCurrentWeight;
    private TextView tvGoalWeight;
    private ActivityResultLauncher<Intent> editProfileLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUserName = findViewById(R.id.tv_user_name);
        tvUserLevel = findViewById(R.id.tv_user_level);
        tvCurrentWeight = findViewById(R.id.tv_current_weight);
        tvGoalWeight = findViewById(R.id.tv_goal_weight);

        Button btnEditProfile = findViewById(R.id.btn_edit_profile);
        Button btnWorkoutGoals = findViewById(R.id.btn_workout_goals);
        Button btnProfileBack = findViewById(R.id.btn_profile_back);

        // Load profile data from persistent ProfileRepository
        loadProfileData();

        // Register ActivityResultLauncher for EditProfileActivity
        editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String updatedName = data.getStringExtra("UPDATED_NAME");
                    String updatedGoal = data.getStringExtra("UPDATED_GOAL");
                    String updatedCurrentWeight = data.getStringExtra("UPDATED_CURRENT_WEIGHT");
                    String updatedGoalWeight = data.getStringExtra("UPDATED_GOAL_WEIGHT");

                    String finalName = (updatedName != null && !updatedName.isEmpty()) ? updatedName : tvUserName.getText().toString();
                    String finalGoal = (updatedGoal != null && !updatedGoal.isEmpty()) ? updatedGoal : tvUserLevel.getText().toString();
                    String finalCurrentWeight = (updatedCurrentWeight != null && !updatedCurrentWeight.isEmpty()) ? updatedCurrentWeight : tvCurrentWeight.getText().toString();
                    String finalGoalWeight = (updatedGoalWeight != null && !updatedGoalWeight.isEmpty()) ? updatedGoalWeight : tvGoalWeight.getText().toString();

                    // Persist to SharedPreferences via ProfileRepository
                    ProfileRepository.saveProfile(this, finalName, finalGoal, finalCurrentWeight, finalGoalWeight);

                    // Update UI immediately
                    loadProfileData();

                    Toast.makeText(this, "Profile & Goals Updated!", Toast.LENGTH_SHORT).show();
                }
            }
        );

        btnEditProfile.setOnClickListener(v -> launchEditProfile());
        btnWorkoutGoals.setOnClickListener(v -> launchEditProfile());

        btnProfileBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileData();
    }

    private void loadProfileData() {
        if (tvUserName != null) tvUserName.setText(ProfileRepository.getName(this));
        if (tvUserLevel != null) tvUserLevel.setText(ProfileRepository.getGoal(this));
        if (tvCurrentWeight != null) tvCurrentWeight.setText(ProfileRepository.getCurrentWeight(this));
        if (tvGoalWeight != null) tvGoalWeight.setText(ProfileRepository.getGoalWeight(this));
    }

    private void launchEditProfile() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        intent.putExtra("CURRENT_NAME", tvUserName.getText().toString());
        intent.putExtra("CURRENT_GOAL", tvUserLevel.getText().toString());
        intent.putExtra("CURRENT_WEIGHT", tvCurrentWeight.getText().toString());
        intent.putExtra("GOAL_WEIGHT", tvGoalWeight.getText().toString());
        editProfileLauncher.launch(intent);
    }
}
