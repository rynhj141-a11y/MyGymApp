package com.example.mygymapp;

import com.example.mygymapp.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivityLifecycle";

    private CountDownTimer countDownTimer;
    private TextView tvTimerDisplay;
    private EditText etRepsCompleted;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 30000; // 30 seconds default
    private String exerciseName = "Workout Session";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate called");

        TextView tvExerciseTitle = findViewById(R.id.tv_exercise_title);
        tvTimerDisplay = findViewById(R.id.tv_timer_display);
        etRepsCompleted = findViewById(R.id.et_reps_completed);
        Button btnStartTimer = findViewById(R.id.btn_start_timer);
        Button btnResetTimer = findViewById(R.id.btn_reset_timer);
        Button btnFinish = findViewById(R.id.btn_finish);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String ex = extras.getString("EXERCISE_NAME");
            String routine = extras.getString("ROUTINE_NAME");

            if (ex != null) {
                exerciseName = ex;
                tvExerciseTitle.setText(exerciseName);
            } else if (routine != null) {
                exerciseName = routine;
                tvExerciseTitle.setText(exerciseName);
            }
        }

        btnStartTimer.setOnClickListener(v -> {
            if (!isTimerRunning) {
                startTimer();
            }
        });

        btnResetTimer.setOnClickListener(v -> resetTimer());

        btnFinish.setOnClickListener(v -> {
            String reps = etRepsCompleted.getText().toString();
            String repsText = reps.isEmpty() ? "10" : reps;
            
            // Save to dynamic WorkoutRepository
            WorkoutRepository.saveWorkout(this, "Completed: " + exerciseName + " (" + repsText + " reps)");

            Intent resultIntent = new Intent();
            resultIntent.putExtra("COMPLETED_WORKOUT", exerciseName);
            resultIntent.putExtra("REPS_DONE", repsText);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this, "Workout Completed & Saved!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
        if (isTimerRunning) {
            pauseTimer();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                tvTimerDisplay.setText("00:00");
                isTimerRunning = false;
                timeLeftInMillis = 30000;
                Toast.makeText(DetailActivity.this, "Rest Time Up!", Toast.LENGTH_SHORT).show();
            }
        }.start();

        isTimerRunning = true;
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 30000;
        updateCountDownText();
        isTimerRunning = false;
    }

    private void updateCountDownText() {
        long secondsLeft = timeLeftInMillis / 1000;
        tvTimerDisplay.setText(String.format("00:%02d", secondsLeft));
    }
}
