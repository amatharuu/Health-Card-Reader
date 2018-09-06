package com.example.surfermate.visionapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewActivity extends AppCompatActivity {

    FindKeywords findKeywords;
    TextView reviewText;
    TextView reviewDOB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        reviewText = findViewById(R.id.reviewText);
        reviewDOB = findViewById(R.id.reviewDOB);
        String info = getIntent().getStringExtra("EXTRA_INFO");

        findKeywords = new FindKeywords(info);
        findKeywords.findName(info, reviewText);
        findKeywords.findDOB(info, reviewDOB);

    }

}
