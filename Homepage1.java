package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;

public class Homepage1 extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.greenshade));
        setContentView(R.layout.activity_homepage1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences=getSharedPreferences(verifyotp.Prefs_Name,0);
                SharedPreferences.Editor editor;
                boolean hasloggedin= sharedPreferences.getBoolean("hasLoggedIn",false);
                if(hasloggedin) {
                    Intent homeIntent = new Intent(Homepage1.this, tittle.class);
                    startActivity(homeIntent);
                    finish();
                }
                else{
                    Intent intent=new Intent(Homepage1.this,tittle.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);
    }
}