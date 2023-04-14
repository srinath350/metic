package com.example.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class qrgenerator extends AppCompatActivity {
    private ImageView qrCodeIV;
    private TextView source,time;
    private Button generateQrBtn;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.greenshade));
        setContentView(R.layout.activity_qrgenerator);
        TextView text1=findViewById(R.id.source);
       // TextView text2=findViewById(R.id.time);
        String str1,str2;
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        str1=user.getUid();
        //getIntent().getStringExtra("source");
       // text1.setText(str1);
        // text2.setText(String.format(
        //"+91-%s",getIntent().getStringExtra("mobile")));
        qrCodeIV = findViewById(R.id.idIVQrcode);
        // source = findViewById(R.id.source);
        // time=findViewById(R.id.time);
        generateQrBtn = findViewById(R.id.idBtnGenerateQR);
        // initializing onclick listener for button.
        generateQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(str1)) {
                    // if the edittext inputs are empty then execute
                    // this method showing a toast message.
                    Toast.makeText(qrgenerator.this, "Enter some text to generate QR Code", Toast.LENGTH_SHORT).show();
                } else {
                    // below line is for getting
                    // the windowmanager service.
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

                    // initializing a variable for default display.
                    Display display = manager.getDefaultDisplay();

                    // creating a variable for point which
                    // is to be displayed in QR Code.
                    Point point = new Point();
                    display.getSize(point);

                    // getting width and
                    // height of a point
                    int width = point.x;
                    int height = point.y;

                    // generating dimension from width and height.
                    int dimen = width < height ? width : height;
                    dimen = dimen * 4 / 4;

                    // setting this dimensions inside our qr code
                    // encoder to generate our qr code.
                    qrgEncoder = new QRGEncoder(str1,null, QRGContents.Type.TEXT, dimen);
                    Toast.makeText(qrgenerator.this, "Scan Near Entry Gate", Toast.LENGTH_SHORT).show();
                    try {
                        // getting our qrcode in the form of bitmap.
                        bitmap= qrgEncoder.encodeAsBitmap();
                        // the bitmap is set inside our image
                        // view using .setimagebitmap method.
                        qrCodeIV.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        // this method is called for
                        // exception handling.
                        Log.e("Tag", e.toString());
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Exit? \n (Do not close your app until you reach your Destination)");
        builder.setCancelable(true);
        builder.setPositiveButton("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                qrgenerator.super.onBackPressed();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}