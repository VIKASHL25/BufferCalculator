package com.example.buffercalculator;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class FacultyDashboardActivity extends AppCompatActivity {
    TextView nameText, deptText, bufferText;
    EditText lateInput;
    Button submitLateBtn, backHomeBtn;
    LinearLayout lateRecordsContainer;
    DatabaseHelper db;
    String facultyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_dashboard);

        nameText = findViewById(R.id.facultyNameText);
        deptText = findViewById(R.id.facultyDeptText);
        bufferText = findViewById(R.id.facultyBufferText);
        lateInput = findViewById(R.id.lateMinutesInput);
        submitLateBtn = findViewById(R.id.submitLateBtn);
        backHomeBtn = findViewById(R.id.backToHomeBtn);
        lateRecordsContainer = findViewById(R.id.lateRecordsContainer);
        db = new DatabaseHelper(this);

        facultyId = getIntent().getStringExtra("FACULTY_ID");
        loadFacultyData();

        submitLateBtn.setOnClickListener(v -> {
            String late = lateInput.getText().toString();
            if (!late.isEmpty()) {
                int mins = Integer.parseInt(late);
                db.addLateTime(facultyId, mins);
                loadFacultyData();
                lateInput.setText("");
                Toast.makeText(this, "Late time updated!", Toast.LENGTH_SHORT).show();
            }
        });

        backHomeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(FacultyDashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void loadFacultyData() {
        Cursor c = db.getFaculty(facultyId);
        if (c.moveToFirst()) {
            nameText.setText("Faculty: " + c.getString(1));
            deptText.setText("Department: " + c.getString(2));
            bufferText.setText("Remaining Buffer: " + c.getInt(3) + " mins");
        }

        lateRecordsContainer.removeAllViews();
        Cursor records = db.getLateRecords(facultyId);
        while (records.moveToNext()) {
            TextView record = new TextView(this);
            record.setText(records.getString(2) + " → " + records.getInt(3) + " mins late");
            lateRecordsContainer.addView(record);
        }
    }
}

