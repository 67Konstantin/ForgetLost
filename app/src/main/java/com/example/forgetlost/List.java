package com.example.forgetlost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        startActivity(new Intent(List.this, Registration.class));
        FirebaseAuth.getInstance().signOut();
    }
}