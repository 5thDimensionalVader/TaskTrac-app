package com.example.tasktracdemo2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.tasktracdemo2.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        //hide the ActionBar
        getSupportActionBar().hide();
        //create the intent to go to the BaseActivity after a few seconds (2.0 secs)
        final Intent intent = new Intent(SplashActivity.this, CategoryActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}