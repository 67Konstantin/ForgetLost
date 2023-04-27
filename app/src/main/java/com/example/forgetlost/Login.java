package com.example.forgetlost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    EditText loginUserEmail, loginPassword;
    Button btLoginEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        {
            loginUserEmail = findViewById(R.id.editText_gmail_login);
            loginPassword = findViewById(R.id.et_password_login);
            btLoginEnd = findViewById(R.id.btLoginEnd);
        }

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();
    }

    public void GoToReg(View view) {
        startActivity(new Intent(Login.this, Registration.class));
    }


    public boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Поле ввода не может быть пустым");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String username = loginUserEmail.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        Toast.makeText(Login.this, "Окей", Toast.LENGTH_SHORT).show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("userName").equalTo(username);


        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    loginUserEmail.setError(null);
                    String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userPassword)) {
                        loginUserEmail.setError(null);
                        startActivity(new Intent(Login.this, List.class));
                        Toast.makeText(Login.this, "Всё получилось", Toast.LENGTH_SHORT).show();
                    } else {
                        loginPassword.setError("Неверный пароль");
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

    public void LogClick(View view) {
        if (checkEmail(loginUserEmail)) {
            if (loginPassword.getText().toString().length() >= 8) {
                if (firebaseAuth.getCurrentUser() != null) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(Login.this, user.getEmail() + ", вы уже вошли!", Toast.LENGTH_SHORT).show();
                } else {

                    firebaseAuth.signInWithEmailAndPassword(loginUserEmail.getText().toString(), loginPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // User signed in successfully
                                        Toast.makeText(Login.this, "вход выполнен", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login.this, List.class));
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Login.this, "ошибка входа", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                } else{
                    loginPassword.setError("Пароль содержит не менее 8 символов");
                }
            } else {
                loginUserEmail.setError("Неверно введена почта");

        }
    }

    boolean checkEmail(EditText email) {
        String emailSt = email.getText().toString();
        if (!emailSt.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailSt).matches()) {
            return true;
        } else {
            return false;
        }
    }
}