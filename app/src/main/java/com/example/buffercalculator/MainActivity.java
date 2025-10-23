package com.example.buffercalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
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
        hodLoginBtn.setOnClickListener(v ->
                startActivity(new Intent(this, HodLoginActivity.class)));


    }
}
