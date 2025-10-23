package com.example.buffercalculator;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.database.Cursor;
import android.content.Intent;

public class HodDashboardActivity extends AppCompatActivity {

    EditText checkIdInput;
    Button checkBtn,registerBtn,resetBtn,btnHome;
    TextView checkResult;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_dashboard);

        db=new Database(this);
        checkIdInput=findViewById(R.id.checkIdInput);
        checkBtn=findViewById(R.id.checkBtn);
        registerBtn=findViewById(R.id.registerBtn);
        resetBtn=findViewById(R.id.resetBtn);
        checkResult=findViewById(R.id.checkResult);
        btnHome=findViewById(R.id.btnHome);

        registerBtn.setOnClickListener(v -> startActivity(new Intent(this, RegisterTeacherActivity.class)));

        checkBtn.setOnClickListener(v -> {
            String id=checkIdInput.getText().toString().trim();
            Cursor c=db.getTeacher(id);
            if(c.moveToFirst()){
                String name=c.getString(1);
                int buffer=c.getInt(2);
                checkResult.setText("Teacher: " + name + "\nRemaining Buffer: " + buffer + " mins");
            } else {
                checkResult.setText("No teacher found!");
            }
        });

        resetBtn.setOnClickListener(v -> {
            db.resetAllTeachersBuffer();
            Toast.makeText(this, "All teachers' buffer reset to 120 mins", Toast.LENGTH_SHORT).show();
        });


        btnHome.setOnClickListener(v -> {
            Intent i = new Intent(HodDashboardActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

    }
}
