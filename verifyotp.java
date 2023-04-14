package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class verifyotp extends AppCompatActivity {

    EditText inputnumber1,inputnumber2,inputnumber3,inputnumber4,inputnumber5,inputnumber6;
    String getotpbackend;
    public static String Prefs_Name="MyPref";
    DocumentReference documentReference;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.greenshade));
        setContentView(R.layout.activity_verifyotp);
        final Button verifybuttonclick = findViewById(R.id.button_otp);
        inputnumber1 = findViewById(R.id.inputotp1);
        inputnumber2 = findViewById(R.id.inputotp2);
        inputnumber3 = findViewById(R.id.inputotp3);
        inputnumber4 = findViewById(R.id.inputotp4);
        inputnumber5 = findViewById(R.id.inputotp5);
        inputnumber6 = findViewById(R.id.inputotp6);

        TextView textView =findViewById(R.id.textshowmobileno);
        textView.setText(String.format(
               "+91%s",getIntent().getStringExtra("mobile")
        ));
      getotpbackend = getIntent().getStringExtra("backendotp");
        final ProgressBar progressBarverifyotp=findViewById(R.id.progressbar_verify_otp);


        String str=textView.getText().toString();
        textView.setText(str);
        documentReference=db.collection("users").document(str);

        verifybuttonclick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=getSharedPreferences(verifyotp.Prefs_Name,0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                if(!inputnumber1.getText().toString().trim().isEmpty() && !inputnumber2.getText().toString().trim().isEmpty()&&!inputnumber3.getText().toString().trim().isEmpty() && !inputnumber4.getText().toString().trim().isEmpty() && !inputnumber5.getText().toString().trim().isEmpty() && !inputnumber6.getText().toString().trim().isEmpty()){
                    String entercodeotp = inputnumber1.getText().toString()+
                            inputnumber2.getText().toString()+
                            inputnumber3.getText().toString()+
                            inputnumber4.getText().toString()+
                            inputnumber5.getText().toString()+
                            inputnumber6.getText().toString();

                    if(getotpbackend!=null){
                        progressBarverifyotp.setVisibility(View.VISIBLE);
                        verifybuttonclick.setVisibility(View.INVISIBLE);

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(getotpbackend, entercodeotp);
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBarverifyotp.setVisibility(View.GONE);
                                        verifybuttonclick.setVisibility(View.VISIBLE);

                                        if (task.isSuccessful()){
                                            editor.putBoolean("hasLoggedIn",true);
                                            editor.commit();
                                            documentReference.get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if(task.getResult().exists()){
                                                                Intent pintent = new Intent(getApplicationContext(),mainpage.class);
                                                                pintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(pintent);

                                                            }else {
                                                                Intent intent = new Intent(getApplicationContext(),profile.class);
                                                                String phone=textView.getText().toString();
                                                                intent.putExtra("phone",phone);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent);
                                                                finish();

                                                            }
                                                        }
                                                    });
                                            }
                                        else {
                                            Toast.makeText(verifyotp.this, "Enter correct Otp", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else{
                        Toast.makeText(verifyotp.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(verifyotp.this,"Recheck your OTP",Toast.LENGTH_SHORT).show();
                    }
                else {
                    Toast.makeText(verifyotp.this,"Please enter all number",Toast.LENGTH_SHORT).show();
                }
            }
        });
        numberotpmove();

                TextView resendlabel = findViewById(R.id.textresend);
                resendlabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + getIntent().getStringExtra("mobile"),
                                30,
                                TimeUnit.SECONDS,
                                verifyotp.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    }
                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Toast.makeText(verifyotp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onCodeSent(@NonNull String newbackendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                       getotpbackend = newbackendotp;
                                        Toast.makeText(verifyotp.this, "Otp Sent successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                    }
                });
    }
        private void numberotpmove(){

        inputnumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputnumber2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputnumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputnumber3.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputnumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputnumber4.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputnumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputnumber5.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputnumber5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    inputnumber6.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}