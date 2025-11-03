package com.example.buffercalculator;



import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText idInput, nameInput, deptInput;
    Button submitBtn, backHomeBtn;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        idInput = findViewById(R.id.regId);
        nameInput = findViewById(R.id.regName);
        deptInput = findViewById(R.id.regDept);
        submitBtn = findViewById(R.id.regSubmitBtn);
        backHomeBtn = findViewById(R.id.backToHomeBtn);
        db = new DatabaseHelper(this);

        submitBtn.setOnClickListener(v -> {
            String id = idInput.getText().toString();
            String name = nameInput.getText().toString();
            String dept = deptInput.getText().toString();

            if (id.isEmpty() || name.isEmpty() || dept.isEmpty()) {
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.registerFaculty(id, name, dept)) {
                Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Faculty already exists", Toast.LENGTH_SHORT).show();
            }
        });

        backHomeBtn.setOnClickListener(v -> {
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });
    }
}
