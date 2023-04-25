package com.example.forgetlost;

import android.content.Intent;
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

public class Login extends AppCompatActivity {
    EditText loginUserName, loginPasswod;
    Button btLoginEnd;
    TextView tvPasswordERROR_login, tvEmailERROR_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        {
            loginUserName = findViewById(R.id.editText_userName_login);
            loginPasswod = findViewById(R.id.et_password_login);
            btLoginEnd = findViewById(R.id.btLoginEnd);
            tvPasswordERROR_login = findViewById(R.id.tvPasswordERROR_login);
            tvEmailERROR_login = findViewById(R.id.tvEmailERROR_login);
        }
    }

    public void GoToReg(View view) {
        startActivity(new Intent(Login.this, Registration.class));
    }


    public boolean validateEmail() {
        String val = loginUserName.getText().toString();
        if (val.isEmpty()) {
            loginPasswod.setError("Неверно введена почта");
            return true;
        } else {
            loginPasswod.setError(null);
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

    public void checkUser() {
        String username = loginUserName.getText().toString().trim();
        String userPassword = loginPasswod.getText().toString().trim();

        Toast.makeText(Login.this, "Окей", Toast.LENGTH_SHORT).show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("userName").equalTo(username);


        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    loginUserName.setError(null);
                    String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)) {
                        loginUserName.setError(null);
                        startActivity(new Intent(Login.this, List.class));
                        Toast.makeText(Login.this, "Всё получилось", Toast.LENGTH_SHORT).show();
                    } else {
                        loginPasswod.setError("Неверный пароль");
                        loginUserName.requestFocus();
                    }
                } else {
                    loginUserName.setError("Пользователя с таким Никнеймом не существует");
                    loginUserName.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void RegClick(View view) {
        if (!(validateEmail() && validatePassword())) {
            checkUser();
        }
    }
}