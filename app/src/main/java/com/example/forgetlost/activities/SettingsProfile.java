package com.example.forgetlost.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.forgetlost.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsProfile extends AppCompatActivity {
    FrameLayout flImageProfileRedact;
    ImageView ivImageProfileRedact;
    EditText etProfileNameRedact;
    TextView tvProfileEmailRedact, tvChangePasswordRedact, etPasswordRedact;
    String uid;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);
        {
            user = firebaseAuth.getCurrentUser();
            flImageProfileRedact = findViewById(R.id.flImageProfileRedact);
            ivImageProfileRedact = findViewById(R.id.ivImageProfileRedact);
            etProfileNameRedact = findViewById(R.id.etProfileNameRedact);
            tvProfileEmailRedact = findViewById(R.id.tvProfileEmailRedact);
            etPasswordRedact = findViewById(R.id.etPasswordRedact);
            tvChangePasswordRedact = findViewById(R.id.tvChangePasswordRedact);
        }
        uid = user.getUid();

    }
}