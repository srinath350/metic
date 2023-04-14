
package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText enternumber;
    Button getotpbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.greenshade));
        setContentView(R.layout.activity_main);

        enternumber = findViewById(R.id.input_number);
        getotpbutton = findViewById(R.id.button_otp);
        final ProgressBar progressBar=findViewById(R.id.progressbar_sendingotp);


           getotpbutton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (!enternumber.getText().toString().trim().isEmpty()) {
                       if ((enternumber.getText().toString().trim()).length() == 10) {
                           progressBar.setVisibility(View.VISIBLE);
                           getotpbutton.setVisibility(View.INVISIBLE);
                           PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                   "+91" + enternumber.getText().toString(),
                                   60,
                                   TimeUnit.SECONDS,
                                   MainActivity.this,
                                   new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                       @Override
                                       public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                           progressBar.setVisibility(View.GONE);
                                           getotpbutton.setVisibility(View.VISIBLE);

                                       }

                                       @Override
                                       public void onVerificationFailed(@NonNull FirebaseException e) {
                                           progressBar.setVisibility(View.GONE);
                                           getotpbutton.setVisibility(View.VISIBLE);
                                           Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                       }

                                       @Override
                                       public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                           progressBar.setVisibility(View.GONE);
                                           getotpbutton.setVisibility(View.VISIBLE);
                                           Intent intent = new Intent(getApplicationContext(), verifyotp.class);
                                           intent.putExtra("mobile", enternumber.getText().toString());
                                           intent.putExtra("backendotp", backendotp);
                                           startActivity(intent);
                                       }
                                   }

                           );


                       } else {
                           Toast.makeText(MainActivity.this, "Please enter correct number", Toast.LENGTH_SHORT).show();
                       }
                   } else {
                       Toast.makeText(MainActivity.this, "Enter Mobile number", Toast.LENGTH_SHORT).show();
                   }
               }
        });

    }
}