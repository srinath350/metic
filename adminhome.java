package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class adminhome extends AppCompatActivity {
    ImageButton user,history,logout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhome);
        user=findViewById(R.id.userdata);
        history=findViewById(R.id.hist);
        logout=findViewById(R.id.exit);
        preferences=getSharedPreferences("MyPref",0);
        editor=preferences.edit();
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uintent=new Intent(adminhome.this,admindata.class);
                startActivity(uintent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hintent=new Intent(adminhome.this,admindata.class);
                startActivity(hintent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                boolean hasLoggedIn=preferences.getBoolean("hasLoggedIn",false);
                Intent  logoutintent=new Intent(adminhome.this,Homepage1.class);
                startActivity(logoutintent);
                finish();
            }
        });


    }
}