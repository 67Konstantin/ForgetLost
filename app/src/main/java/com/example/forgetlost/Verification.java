package com.example.forgetlost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Verification extends AppCompatActivity {
    FirebaseAuth auth;
    Button btSend, btCheck, btLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        auth = FirebaseAuth.getInstance();
        btSend = findViewById(R.id.btSendEmail);
        btCheck = findViewById(R.id.btCheckVerification);
        btLogOut = findViewById(R.id.btLogOut);
    }

    public void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Verification.this, Registration.class));
    }

    public void Check(View view) {
        FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Verification.this, "Вы успешно подтвердили почту", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Verification.this, List.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Verification.this, "Вы не подтвердили почту", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void Send(View view) {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Verification.this, "Письмо было отправлено на вашу почту", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Verification.this, "Не удалось отправить письмо", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}