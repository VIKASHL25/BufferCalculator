package com.example.buffercalculator;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
    EditText teacherIdInput;
    Button submitBtn, hodLoginBtn;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new Database(this);
        teacherIdInput = findViewById(R.id.teacherIdInput);
        submitBtn = findViewById(R.id.submitBtn);
        hodLoginBtn = findViewById(R.id.hodLoginBtn);

        submitBtn.setOnClickListener(v -> {
            String id = teacherIdInput.getText().toString().trim();
            if (id.isEmpty()) {
                Toast.makeText(this, "Enter Teacher ID", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor c = db.getTeacher(id);
            if (c.moveToFirst()) {
                Intent i = new Intent(MainActivity.this, TeacherDashboardActivity.class);
                i.putExtra("teacher_id", id);
                startActivity(i);
            } else {
                Toast.makeText(this, "Teacher ID not found", Toast.LENGTH_SHORT).show();
            }
            c.close();
        });

        hodLoginBtn.setOnClickListener(v ->
                startActivity(new Intent(this, HodLoginActivity.class)));
    }
}

