package com.example.mygymapp;

import com.example.mygymapp.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddExerciseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        EditText etNewName = findViewById(R.id.et_new_exercise_name);
        Button btnSave = findViewById(R.id.btn_save_exercise);
        Button btnCancel = findViewById(R.id.btn_cancel_exercise);

        btnSave.setOnClickListener(v -> {
            String name = etNewName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter an exercise name", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_EXERCISE", name);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}
