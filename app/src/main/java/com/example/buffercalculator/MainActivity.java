package com.example.buffercalculator;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText idInput, nameInput;
    Button loginBtn, registerBtn;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idInput = findViewById(R.id.facultyIdInput);
        nameInput = findViewById(R.id.facultyNameInput);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        db = new DatabaseHelper(this);

        loginBtn.setOnClickListener(v -> {
            try {
                String id = idInput.getText().toString().trim();
                String name = nameInput.getText().toString().trim();

                if (id.isEmpty() || name.isEmpty()) {
                    Toast.makeText(this, "Enter both ID and Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (db.validateFaculty(id, name)) {
                    db.resetMonthlyBufferIfNeeded(id);
                    Intent intent = new Intent(MainActivity.this, FacultyDashboardActivity.class);
                    intent.putExtra("FACULTY_ID", id);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
