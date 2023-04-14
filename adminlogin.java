package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class adminlogin extends AppCompatActivity {
    DocumentReference documentReference;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    public static String Prefs_Name="MyPref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.greenshade));
        setContentView(R.layout.activity_adminlogin);
        AppCompatButton go=findViewById(R.id.buttongo);
        EditText user,pass;
        user=findViewById(R.id.user);
        pass=findViewById(R.id.pass);
        SharedPreferences sharedPreferences=getSharedPreferences(verifyotp.Prefs_Name,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        documentReference=db.collection("Admin").document("admin");

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                  if(task.getResult().exists()){
                                    String username=task.getResult().getString("username");
                                    String password=task.getResult().getString("password");
                                    if(user.getText().toString().equals(username) && pass.getText().toString().equals(password)){
                                        Intent gintent=new Intent(adminlogin.this,adminhome.class);
                                        startActivity(gintent);
                                        editor.putBoolean("hasLoggedIn",true);
                                        editor.commit();
                                    }
                                    else{
                                        Toast.makeText(adminlogin.this, "Check Username or Password ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            }
        });
    }
}