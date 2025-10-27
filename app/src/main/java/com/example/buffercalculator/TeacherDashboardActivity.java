package com.example.buffercalculator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.database.Cursor;
import android.content.Intent;

public class TeacherDashboardActivity extends AppCompatActivity {

    TextView bufferDisplay;
    EditText inputDeduct;
    Button deductBtn, refreshBtn, btnHome;
    Database db;
    String teacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        db = new Database(this);

        // UI Elements
        bufferDisplay = findViewById(R.id.bufferDisplay);
        inputDeduct = findViewById(R.id.inputDeduct);
        deductBtn = findViewById(R.id.deductBtn);
        refreshBtn = findViewById(R.id.refreshBtn);
        btnHome = findViewById(R.id.btnHome);

        // Receive teacher ID from previous screen
        teacherId = getIntent().getStringExtra("teacher_id");

        // Initial buffer display
        updateBufferDisplay();

        // Deduct time button
        deductBtn.setOnClickListener(v -> {
            String input = inputDeduct.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(this, "Enter minutes to deduct", Toast.LENGTH_SHORT).show();
                return;
            }

            int minutesToDeduct = Integer.parseInt(input);
            Cursor c = db.getTeacher(teacherId);
            if (c.moveToFirst()) {
                int currentBuffer = c.getInt(2);
                if (minutesToDeduct > currentBuffer) {
                    Toast.makeText(this, "Not enough buffer time left", Toast.LENGTH_SHORT).show();
                } else {
                    int newBuffer = currentBuffer - minutesToDeduct;
                    db.updateTeacherBuffer(teacherId, newBuffer);
                    Toast.makeText(this, "Deducted " + minutesToDeduct + " mins", Toast.LENGTH_SHORT).show();
                    updateBufferDisplay();
                    inputDeduct.setText("");
                }
            }
            c.close();
        });

        // Refresh button to update displayed buffer
        refreshBtn.setOnClickListener(v -> updateBufferDisplay());

        // Home button
        btnHome.setOnClickListener(v -> {
            Intent i = new Intent(TeacherDashboardActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void updateBufferDisplay() {
        Cursor c = db.getTeacher(teacherId);
        if (c.moveToFirst()) {
            String name = c.getString(1);
            int buffer = c.getInt(2);
            bufferDisplay.setText("Teacher: " + name + "\nRemaining Buffer: " + buffer + " mins");
        } else {
            bufferDisplay.setText("Teacher not found!");
        }
        c.close();
    }
}

