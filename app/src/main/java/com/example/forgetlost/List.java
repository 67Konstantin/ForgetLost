package com.example.forgetlost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_activty);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void Click(View view) {
        onBackPressed();
    }
}