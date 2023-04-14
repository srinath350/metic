package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class wallet extends AppCompatActivity implements PaymentResultListener {
    EditText amount;
    int am = 0;
    String str1;
    TextView wamount;
    DocumentReference documentReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.greenshade));
        setContentView(R.layout.activity_wallet);
        Checkout.preload(getApplicationContext());
        AppCompatButton add = findViewById(R.id.addmoneybtn);
        wamount = findViewById(R.id.text1);
        amount = findViewById(R.id.amount);
        CardView pass=findViewById(R.id.cardpass);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(wallet.this,passbook.class);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount.getText().toString().isEmpty()){
                    Toast.makeText(wallet.this, "Please enter the amount", Toast.LENGTH_SHORT).show();
                }else if (Integer.parseInt(amount.getText().toString()) >= 50) {
                    makepayment();
                }
                else{
                    Toast.makeText(wallet.this, "Minimum 50 should be added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void makepayment() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String phone = user.getPhoneNumber();
        documentReference = db.collection("wallet").document(phone);
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_1TEIpWQAuqQYpJ");
        checkout.setImage(R.drawable.app_logo);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();
            String string = amount.getText().toString();
            int str = Integer.parseInt(string) * 100;
            options.put("name", "Metic");
            options.put("description", "Reference No. #123456");
            options.put("image", "app_logo");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#acd5ad");
            options.put("currency", "INR");
            options.put("amount", str);//300 X 100
            options.put("prefill.email", "sandeepp502@gmail.com");
            options.put("prefill.contact", phone);
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            str1 = (task.getResult().getString("Amount"));
                            wamount.setText(str1);
                            am = Integer.parseInt(str1);
                            add();

                        } else {
                            am=0;
                            add();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
           }

    private void add() {
        int a = Integer.parseInt(amount.getText().toString()) + am;
        String str2 = String.valueOf(a);
        Map<String, String> profil = new HashMap<>();
        profil.put("Amount", str2);
        documentReference.set(profil).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:aa", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                //String uniqueID = UUID.randomUUID().toString();
                String wa="Money added to wallet: ₹ "+amount.getText().toString();
                CollectionReference dbCourses = db.collection(uid);

                // adding our data to our courses object class.
                walletpass walletpass = new walletpass(wa,currentDateandTime);

                // below method is use to add data to Firebase Firestore.
                dbCourses.add(walletpass).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // after the data addition is successful
                        // we are displaying a success toast message.
                        Toast.makeText(wallet.this, "Amount added to your wallet", Toast.LENGTH_SHORT).show();
                        amount.setText("");
                        display();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // this method is called when the data addition process is failed.
                        // displaying a toast message when data addition is failed.
                        Toast.makeText(wallet.this, "Fail to add amount \n" + e, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(wallet.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void display() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String phone=user.getPhoneNumber();
        documentReference=db.collection("wallet").document(phone);
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            str1=(task.getResult().getString("Amount"));
                            wamount.setText(str1);

                        }else{
                            Toast.makeText(wallet.this, "No wallet balance", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "payment failure"+s, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        display();

    }
}