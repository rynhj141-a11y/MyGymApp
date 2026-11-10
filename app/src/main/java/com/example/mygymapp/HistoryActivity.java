package com.example.mygymapp;

import com.example.mygymapp.R;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

/**
 * Activity for displaying completed workout history log entries.
 * Allows users to view past workouts and clear history records.
 */
public class HistoryActivity extends AppCompatActivity {

    private ArrayList<String> workoutHistoryList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private TextView tvTotalLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Bind layout views
        TextView tvTitle = findViewById(R.id.tv_history_title);
        tvTotalLogs = findViewById(R.id.tv_total_logs);
        ListView lvHistory = findViewById(R.id.lv_history);
        Button btnBackMain = findViewById(R.id.btn_back_main);
        Button btnClearHistory = findViewById(R.id.btn_clear_history);

        // Extract user ID passed via intent extras if present
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userId = extras.getString("USER_ID");
            if (userId != null && tvTitle != null) {
                tvTitle.setText("HISTORY (" + userId + ")");
            }
        }

        // Custom list item adapter styled for dark theme UI
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, workoutHistoryList) {
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
        lvHistory.setAdapter(adapter);

        // Initial data load
        refreshData();

        // Navigate back to main screen
        btnBackMain.setOnClickListener(v -> finish());

        // Clear history logs from repository and refresh UI
        btnClearHistory.setOnClickListener(v -> {
            WorkoutRepository.clearWorkouts(this);
            refreshData();
            Toast.makeText(this, "History cleared!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh workout history when returning to this screen
        refreshData();
    }

    /**
     * Reloads workout logs from WorkoutRepository and updates the list view and count label.
     */
    private void refreshData() {
        workoutHistoryList.clear();
        workoutHistoryList.addAll(WorkoutRepository.getWorkouts(this));
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (tvTotalLogs != null) {
            tvTotalLogs.setText(workoutHistoryList.size() + " Sessions");
        }
    }
}
