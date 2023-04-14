package com.example.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.sql.Ref;
import java.util.HashMap;
import java.util.Map;

public class profile extends AppCompatActivity {
    private EditText name,dob,phone;
    private ImageView image;
    private ProgressBar progressBar;
    private RadioGroup radioGroupgender;
    private RadioButton genderselected;
    private Uri imageuri;
    private static final int PICK_IMAGE=1;
    UploadTask uploadTask;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DocumentReference documentReference;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.greenshade));
        setContentView(R.layout.activity_profile);
        image=findViewById(R.id.profileimage);
        name=findViewById(R.id.fullname);
        dob=findViewById(R.id.dob);
        phone=findViewById(R.id.mobile);
        progressBar=findViewById(R.id.progress);
        String number=getIntent().getStringExtra("phone");
        phone.setText(number);
        radioGroupgender=findViewById(R.id.gender);
        radioGroupgender.clearCheck();
        AppCompatButton save=findViewById(R.id.save);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
           String str=user.getPhoneNumber();
        documentReference=db.collection("users").document(str);
        storageReference=firebaseStorage.getInstance().getReference(str);
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
                            Picasso.get().load(Url).into(image);
                            name.setText(nameresult);
                            dob.setText(ageresult);
                            phone.setText(noresult);


                        }else{
                            Toast.makeText(profile.this, "no profile found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploaddata();
            }
        });
    }

    private void uploaddata() {
        int selctedgender = radioGroupgender.getCheckedRadioButtonId();
        genderselected = findViewById(selctedgender);
        String fullname, Dob, mobile;
        fullname = name.getText().toString();
        Dob = dob.getText().toString();
        mobile = phone.getText().toString();
        if (TextUtils.isEmpty(fullname)) {
            Toast.makeText(profile.this, "Please enter name", Toast.LENGTH_SHORT).show();
            name.setError("Name is required");
            name.requestFocus();
        } else if (TextUtils.isEmpty(Dob)) {
            Toast.makeText(profile.this, "Please enter date of birth", Toast.LENGTH_SHORT).show();
            dob.setError("Dob is required");
            dob.requestFocus();
        } else if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(profile.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
            phone.setError("mobile number is required");
            phone.requestFocus();
        } else if (mobile.length() >= 14 && mobile.length() <= 10) {
            Toast.makeText(profile.this, "Please re-enter mobile number", Toast.LENGTH_SHORT).show();
            phone.setError("mobile number shoulbe be 10 digits");
            phone.requestFocus();
        } else if (radioGroupgender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(profile.this, "Please select your gender", Toast.LENGTH_SHORT).show();
            genderselected.setError("gender is required");
            genderselected.requestFocus();
        }else if(imageuri == null){
            Toast.makeText(this, "select profile image", Toast.LENGTH_SHORT).show();
        }else{
            String gender = genderselected.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference reference=storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageuri));
            uploadTask=reference.putFile(imageuri);
            Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloaduri=task.getResult();
                        Map<String, String> profil = new HashMap<>();
                        profil.put("Name", fullname);
                        profil.put("Gender", gender);
                        profil.put("Age", Dob);
                        profil.put("MobileNumber", mobile);
                        profil.put("url",downloaduri.toString());
                        documentReference.set(profil).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(profile.this, "profile created succesfully", Toast.LENGTH_SHORT).show();
                                Intent pintent = new Intent(profile.this, mainpage.class);
                                startActivity(pintent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(profile.this, "failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }


    public void ChooseImage(View view) {
        Intent pintent=new Intent();
        pintent.setType("image/*");
        pintent.setAction(pintent.ACTION_GET_CONTENT);
        startActivityForResult(pintent,PICK_IMAGE);
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE || resultCode == RESULT_OK || data != null || data.getData() != null){
            imageuri=data.getData();
            Picasso.get().load(imageuri).into(image);
        }
    }

    private  String getFileExt(Uri uri) {
    ContentResolver contentresolver = getContentResolver();
    MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
    return  mimeTypeMap.getExtensionFromMimeType(contentresolver.getType(uri));
    }

    @Override
    protected void onStart() {

        super.onStart();
    }
}


