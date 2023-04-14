package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class mainpage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton hist,walletbut,mapbut;
    Toolbar toolbar;
    TextView text;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    DocumentReference documentReference;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.greenshade));
        setContentView(R.layout.activity_mainpage);
        ImageButton ticbut=findViewById(R.id.imageButton1);
        String str;
        //str=getIntent().getStringExtra("EXTRA MSG");
        text=findViewById(R.id.textview);
        preferences=getSharedPreferences("MyPref",0);
        editor=preferences.edit();
        hist=findViewById(R.id.imageButton2);
        walletbut=findViewById(R.id.imageButton3);
        mapbut=findViewById(R.id.imageButton4);
        /*----------Hooks-----------*/
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navmenu);
        toolbar = findViewById(R.id.toolbar);

        /*----------Tool Bar--------*/
        setSupportActionBar(toolbar);

        /*----------Navigation Driver Menu--------*/

        //Hide or show items
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.menu_logout).setVisible(true);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            str=user.getPhoneNumber();
            documentReference=db.collection("users").document(str);
        }
        else{
            Toast.makeText(this, "user not found", Toast.LENGTH_SHORT).show();
        }


        ticbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tintent=new Intent(mainpage.this,qrgenerator.class);
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    String phone = user.getUid();
                    tintent.putExtra("source",phone);
                }else
                {
                    Toast.makeText(mainpage.this, "user not found", Toast.LENGTH_SHORT).show();
                }
                startActivity(tintent);
            }
        });
        hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hintent=new Intent(mainpage.this,history.class);
                startActivity(hintent);
            }
        });
        walletbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wintent=new Intent(mainpage.this,wallet.class);
                startActivity(wintent);
            }
        });
        mapbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mintent=new Intent(mainpage.this,map.class);
                startActivity(mintent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.menu_profile:
                documentReference.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.getResult().exists()){
                                    Intent pintent = new Intent(getApplicationContext(),showprofile.class);
                                    startActivity(pintent);

                                }else {
                                    Intent intent = new Intent(getApplicationContext(),profile.class);
                                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                                    String phone=user.getPhoneNumber();
                                    intent.putExtra("phone",phone);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        });
                break;

            case R.id.menu_ticket:
                Intent ticket = new Intent(mainpage.this,qrgenerator.class);
                SimpleDateFormat sdf = new SimpleDateFormat("dd:mm:yy,hh:mm:aa");
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    String phone = user.getUid();
                    String currentDateandTime = sdf.format(new Date());
                    ticket.putExtra("source",phone);
                }else
                {
                    Toast.makeText(this, "user not found", Toast.LENGTH_SHORT).show();
                }
                startActivity(ticket);
                break;
            case R.id.menu_history:
                Intent history = new Intent(mainpage.this,history.class);
                startActivity(history);
                break;
            case R.id.menu_m_wallet:
                Intent wallet = new Intent(mainpage.this,wallet.class);
                startActivity(wallet);
                break;
            case R.id.menu_map:
                Intent map = new Intent(mainpage.this,map.class);
                startActivity(map);
                break;
            case R.id.menu_info:
                Intent info = new Intent(mainpage.this,introdetails.class);
                startActivity(info);
                break;
            case R.id.menu_logout:
                editor.clear();
                editor.commit();
                boolean hasLoggedIn=preferences.getBoolean("hasLoggedIn",false);
                Intent  logoutintent=new Intent(mainpage.this,Homepage1.class);
                startActivity(logoutintent);
                finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            String nameresult=task.getResult().getString("Name");
                            text.setText(nameresult);
                        }else{
                            Toast.makeText(mainpage.this, "no profile found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                        }
}




