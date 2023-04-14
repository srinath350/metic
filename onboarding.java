package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class onboarding extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout dots;
    slideradapter slideradapter;
    TextView[] dot;
    AppCompatButton letsstart,skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.greenshade));
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.slider);
        skip=findViewById(R.id.skip_btn);
        letsstart=findViewById(R.id.get_started_btn);
        dots=findViewById(R.id.dots);
        slideradapter =new slideradapter(this);
        viewPager.setAdapter(slideradapter);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sintent=new Intent(onboarding.this,MainActivity.class);
                startActivity(sintent);
            }
        });
        letsstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lintent=new Intent(onboarding.this,MainActivity.class);
                startActivity(lintent);
            }
        });

    }
    private void addDots(int position) {
        dot = new TextView[4];
        dots.removeAllViews();
        for (int i = 0; i < dot.length; i++) {
            dot[i] = new TextView(this);
            dot[i].setText(Html.fromHtml("&#8226"));
            dot[i].setTextSize(35);
            dots.addView(dot[i]);
        }
        if (dot.length > 0) {
            dot[position].setTextColor(getResources().getColor(R.color.blue));
        }
    }
    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            if(position == 0){
                letsstart.setVisibility(View.INVISIBLE);
                skip.setVisibility(View.VISIBLE);
            }else if(position == 1){
                letsstart.setVisibility(View.INVISIBLE);
                skip.setVisibility(View.VISIBLE);

            } else if (position == 2) {
                letsstart.setVisibility(View.INVISIBLE);
                skip.setVisibility(View.VISIBLE);

            }else{
                letsstart.setVisibility(View.VISIBLE);
                skip.setVisibility(View.INVISIBLE);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}