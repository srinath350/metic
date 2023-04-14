package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class showprofile extends AppCompatActivity {
    DocumentReference documentReference;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    TextView name,age,phone,gender;
    ImageView imageView;
    UploadTask uploadTask;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FloatingActionButton ftbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.greenshade));
        setContentView(R.layout.activity_showprofile);
        imageView=findViewById(R.id.profile);
        name=findViewById(R.id.fname);
        age=findViewById(R.id.age);
        phone=findViewById(R.id.phone);
        gender=findViewById(R.id.gender1);
        ftbutton=findViewById(R.id.edit);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String str=user.getPhoneNumber();
        documentReference=db.collection("users").document(str);
        storageReference=firebaseStorage.getInstance().getReference(str);

        ftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eintent=new Intent(showprofile.this,profile.class);
                startActivity(eintent);
            }
        });
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
                          String genderesult=task.getResult().getString("Gender");
                          String ageresult=task.getResult().getString("Age");
                          String noresult=task.getResult().getString("MobileNumber");
                          String Url=task.getResult().getString("url");
                          Picasso.get().load(Url).into(imageView);
                          name.setText(nameresult);
                          age.setText(ageresult);
                          phone.setText(noresult);
                          gender.setText(genderesult);

                      }else{
                          Toast.makeText(showprofile.this, "no profile found", Toast.LENGTH_SHORT).show();
                      }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}