package com.example.tasktracdemo2.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import com.example.tasktracdemo2.R;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener{
    //global variables
    GridLayout mainGrid;
    private CardView card_all, card_exams, card_coursework, card_test, card_others;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        card_all = findViewById(R.id.card_all);
        card_exams = findViewById(R.id.card_exams);
        card_coursework = findViewById(R.id.card_coursework);
        card_test = findViewById(R.id.card_test);
        card_others = findViewById(R.id.card_others);

        card_all.setOnClickListener(this);
        card_exams.setOnClickListener(this);
        card_coursework.setOnClickListener(this);
        card_test.setOnClickListener(this);
        card_others.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.card_all:
                intent = new Intent(this,BaseActivity.class);
                startActivity(intent);
                break;
            case R.id.card_exams:
                intent = new Intent(this, ExamsActivity.class);
                startActivity(intent);
                break;
            case R.id.card_coursework:
                intent = new Intent(this, CourseworkActivity.class);
                startActivity(intent);
                break;
            case R.id.card_test:
                intent = new Intent(this, TestActivity.class);
                startActivity(intent);
                break;
            case R.id.card_others:
                intent = new Intent(this, OthersActivity.class);
                startActivity(intent);
                break;
        }
    }
}