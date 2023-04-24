package com.example.forgetlost;

import android.content.Intent;
import android.net.IpSecAlgorithm;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText loginUserEmail, loginPasswod;
    Button btLoginEnd;
    TextView tvPasswordERROR_login, tvEmailERROR_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        {
            loginUserEmail = findViewById(R.id.editText_gmail_login);
            loginPasswod = findViewById(R.id.et_password_login);
            btLoginEnd = findViewById(R.id.btLoginEnd);
            tvPasswordERROR_login = findViewById(R.id.tvPasswordERROR_login);
            tvEmailERROR_login = findViewById(R.id.tvEmailERROR_login);
            btLoginEnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        tvEmailERROR_login.setText("");
                    String val2 = loginPasswod.getText().toString();
                    String val1 = loginUserEmail.getText().toString();
                    if (val1.length()==0) {
                        loginUserEmail.setError(null);
                        if (val2.isEmpty()) {
                            loginPasswod.setError("Поле ввода не может быть пустым");
                        } else {
                            loginPasswod.setError(null);
                            checkUser();
                        }
                    } else {
                        loginUserEmail.setError("Неправильно введена почта");
                    }

                }
            });
        }
    }

    public void GoToReg(View view) {
        startActivity(new Intent(Login.this, Registration.class));
    }


    public boolean validateEmail() {
        String val = loginUserEmail.getText().toString();
        if (val.isEmpty()) {
            loginPasswod.setError(null);
            return true;
        } else {
            loginPasswod.setError("Неверно введена почта");
            return false;
        }
    }
    public boolean validatePassword() {
        String val = loginPasswod.getText().toString();
        if (val.isEmpty()) {
            loginPasswod.setError("Поле ввода не может быть пустым");
            return false;
        } else {
            loginPasswod.setError(null);
            return true;
        }
    }
    public void checkUser(){
        String userName = loginUserEmail.getText().toString().trim();
        String userPassword = loginPasswod.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(userName);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    loginUserEmail.setError(null);
                    String passwordFromDB = snapshot.child(userName).child("password").getValue(String.class);

                    if(!Objects.equals(passwordFromDB, userPassword)){
                        loginUserEmail.setError(null);
                        startActivity(new Intent(Login.this,List.class));
                        Toast.makeText(Login.this, "Всё получилось", Toast.LENGTH_SHORT).show();
                    } else {
                        loginPasswod.setError("Неверный пароль");
                        loginUserEmail.requestFocus();
                    }
                } else {
                    loginUserEmail.setError("Пользователя с такой почтой не существует");
                    loginUserEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}