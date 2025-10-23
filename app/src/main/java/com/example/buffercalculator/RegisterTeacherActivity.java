package com.example.buffercalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

public class RegisterTeacherActivity extends AppCompatActivity {

    EditText facultyId, facultyName;
    Button registerBtn,btnHome;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);

        db = new Database(this);
        facultyId=findViewById(R.id.teacherId);
        facultyName=findViewById(R.id.teacherName);
        registerBtn=findViewById(R.id.registerTeacherBtn);
        btnHome=findViewById(R.id.btnHome);
        registerBtn.setOnClickListener(v->{
            String id=facultyId.getText().toString().trim();
            String name=facultyName.getText().toString().trim();

            if(id.isEmpty() || name.isEmpty()){
                Toast.makeText(this, "Enter both ID and Name", Toast.LENGTH_SHORT).show();
                return;
            }

            if(db.registerTeacher(id, name)){
                Toast.makeText(this, "Faculty Registered Successfully", Toast.LENGTH_SHORT).show();
                facultyId.setText("");
                facultyName.setText("");
            }
            else{
                Toast.makeText(this, "Faculty ID already exists", Toast.LENGTH_SHORT).show();
            }
        });

        btnHome.setOnClickListener(v -> {
            Intent i = new Intent(RegisterTeacherActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}