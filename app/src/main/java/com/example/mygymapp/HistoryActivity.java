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

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<String> workoutHistoryList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private TextView tvTotalLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView tvTitle = findViewById(R.id.tv_history_title);
        tvTotalLogs = findViewById(R.id.tv_total_logs);
        ListView lvHistory = findViewById(R.id.lv_history);
        Button btnBackMain = findViewById(R.id.btn_back_main);
        Button btnClearHistory = findViewById(R.id.btn_clear_history);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userId = extras.getString("USER_ID");
            if (userId != null && tvTitle != null) {
                tvTitle.setText("HISTORY (" + userId + ")");
            }
        }

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

        refreshData();

        btnBackMain.setOnClickListener(v -> finish());

        btnClearHistory.setOnClickListener(v -> {
            WorkoutRepository.clearWorkouts(this);
            refreshData();
            Toast.makeText(this, "History cleared!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

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
