package com.example.forgetlost;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
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

public class Registration extends AppCompatActivity {
    EditText etEmail, etPassword, etname, etUserName;
    Button btReg;
    CheckBox checkBox;
    Vibrator vibrator;
    int mls = 100;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        {
            etEmail = findViewById(R.id.editText_gmail);
            etUserName = findViewById(R.id.editText_nickname);
            etname = findViewById(R.id.editText_name);
            etPassword = findViewById(R.id.editText_password);
            checkBox = findViewById(R.id.checkBox);
            btReg = findViewById(R.id.btRegisterEnd);
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }
        SpannableString ss = new SpannableString("Даю согласие на обработку \n " + "  " + "персональных данных");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(Registration.this, ProgressBar.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 4, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkBox.setText(ss);
        checkBox.setMovementMethod(LinkMovementMethod.getInstance());
        checkBox.setHighlightColor(Color.TRANSPARENT);
    }

    public void SingInGoogle(View view) {
        startActivity(new Intent(Registration.this, Login.class));
    }

    public void RegClick(View view) {
        if (etUserName.getText().toString().length() != 0) {
            etUserName.setError(null);

            if (checkEmail(etEmail)) {
                etEmail.setError(null);
                if (etname.getText().toString().length() != 0) {
                    etname.setError(null);
                    if (etPassword.getText().toString().length() >= 8) {
                        etPassword.setError(null);
                        if (!etPassword.getText().toString().matches("(.*) (.*)")) {
                            etPassword.setError(null);
                            if (checkBox.isChecked()) {
                                checkUserName();


                            } else {
                                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                                checkBox.setAnimation(shake);
                                checkBox.setDrawingCacheBackgroundColor(Color.RED);
                                vibrator.vibrate(mls);
                            }
                        } else {
                            etPassword.setError("Пароль не должен содержать пробелы");
                            vibrator.vibrate(mls);
                        }
                    } else {
                        etPassword.setError("Пароль должен содержать не менее 8 символов");
                        vibrator.vibrate(mls);
                    }
                } else {
                    etname.setError("Вы не ввели имя");
                    vibrator.vibrate(mls);
                }
            } else {
                etEmail.setError("Некорректно введена почта ");
                vibrator.vibrate(mls);
            }
        } else {
            etUserName.setError("Ваш никнейм не может быть пустым");
        }
    }

    static boolean checkEmail(EditText email) {
        String emailSt = email.getText().toString();
        if (!emailSt.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailSt).matches()) {
            return true;
        } else {
            return false;
        }
    }

    public void checkUserEmail() {
        String email = etEmail.getText().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(email);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    etUserName.setError("Пользователь с такой почтой уже существует");
                    etUserName.requestFocus();

                } else {

                    database = FirebaseDatabase.getInstance();
                    DatabaseReference reference;
                    reference = database.getReference("users");

                    String email = etEmail.getText().toString();
                    String name = etname.getText().toString();
                    String password = etPassword.getText().toString();
                    String userName = etUserName.getText().toString();


                    HelperClass helperClass = new HelperClass(email, name, password, userName);

                    reference.child(userName).setValue(helperClass);
                    etUserName.setError(null);
                    startActivity(new Intent(Registration.this, List.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void checkUserName() {
        String username = etUserName.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("userName").equalTo(username);


        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    etUserName.setError("Пользователь с таким Никнеймом уже существует");
                    etUserName.requestFocus();

                } else {

                    checkUserEmail();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}