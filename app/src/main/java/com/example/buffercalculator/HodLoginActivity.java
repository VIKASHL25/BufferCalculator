package com.example.buffercalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.content.Intent;

public class HodLoginActivity extends AppCompatActivity {

    EditText name,pass;
    Button loginBtn,btnHome;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_login);

        db=new Database(this);
        name=findViewById(R.id.hodUser);
        pass=findViewById(R.id.hodPass);
        btnHome=findViewById(R.id.btnHome);
        loginBtn=findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v ->{
            if(db.validateHOD(name.getText().toString(), pass.getText().toString())){
                startActivity(new Intent(this, HodDashboardActivity.class));
            } else {
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        });

        btnHome.setOnClickListener(v ->{
            Intent i = new Intent(HodLoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}
