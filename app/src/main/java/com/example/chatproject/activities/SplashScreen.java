package com.example.chatproject.activities;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import com.example.chatproject.R;

public class SplashScreen extends AppCompatActivity {

    private static final int DELAY = 3300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        android.os.Handler handler =  new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                android.content.Intent intent =new android.content.Intent(SplashScreen.this,SignInActivity.class);
                startActivity(intent);
            }
        },DELAY);
    }
}