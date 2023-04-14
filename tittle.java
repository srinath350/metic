package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class tittle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.greenshade));
        setContentView(R.layout.activity_tittle);
        ImageButton Admin=findViewById(R.id.admin);
        ImageButton User=findViewById(R.id.user);
        Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences=getSharedPreferences(verifyotp.Prefs_Name,0);
                SharedPreferences.Editor editor;
                boolean hasloggedin= sharedPreferences.getBoolean("hasLoggedIn",false);
                if(hasloggedin) {
                    Intent aintent=new Intent(tittle.this,adminhome.class);
                    startActivity(aintent);
                    finish();
                }
                else{
                    Intent intent=new Intent(tittle.this,adminlogin.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences(verifyotp.Prefs_Name,0);
                SharedPreferences.Editor editor;
                boolean hasloggedin= sharedPreferences.getBoolean("hasLoggedIn",false);
                if(hasloggedin) {
                    Intent homeIntent = new Intent(tittle.this, mainpage.class);
                    startActivity(homeIntent);
                    finish();
                }
                else{
                    Intent intent=new Intent(tittle.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}