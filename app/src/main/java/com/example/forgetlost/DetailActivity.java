package com.example.forgetlost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String describing = intent.getStringExtra("describing");
        String image = intent.getStringExtra("image");
        String userId = intent.getStringExtra("userId");
        String conditions = intent.getStringExtra("conditions");
        String area = intent.getStringExtra("area");
        String data = intent.getStringExtra("data");
    }
}