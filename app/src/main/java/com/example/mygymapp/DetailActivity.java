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

/**
 * Activity for managing individual exercise or routine sessions.
 * Provides a rest countdown timer, rep tracking, and logs completed workout sessions.
 */
public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivityLifecycle";

    private CountDownTimer countDownTimer;
    private TextView tvTimerDisplay;
    private EditText etRepsCompleted;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 30000; // Default 30 seconds rest timer
    private String exerciseName = "Workout Session";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate called");

        // Bind view controls
        TextView tvExerciseTitle = findViewById(R.id.tv_exercise_title);
        tvTimerDisplay = findViewById(R.id.tv_timer_display);
        etRepsCompleted = findViewById(R.id.et_reps_completed);
        Button btnStartTimer = findViewById(R.id.btn_start_timer);
        Button btnResetTimer = findViewById(R.id.btn_reset_timer);
        Button btnFinish = findViewById(R.id.btn_finish);

        // Retrieve exercise or routine title passed from previous screen
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

        // Timer action buttons
        btnStartTimer.setOnClickListener(v -> {
            if (!isTimerRunning) {
                startTimer();
            }
        });

        btnResetTimer.setOnClickListener(v -> resetTimer());

        // Finish button: records completed workout reps and saves log to repository
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
        // Pause timer when activity loses focus
        if (isTimerRunning) {
            pauseTimer();
        }
    }

    /**
     * Starts the countdown timer for rest interval.
     */
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

    /**
     * Pauses the active countdown timer.
     */
    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
    }

    /**
     * Resets countdown timer to default duration.
     */
    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 30000;
        updateCountDownText();
        isTimerRunning = false;
    }

    /**
     * Formats and updates timer display text view (MM:SS).
     */
    private void updateCountDownText() {
        long secondsLeft = timeLeftInMillis / 1000;
        tvTimerDisplay.setText(String.format("00:%02d", secondsLeft));
    }
}
