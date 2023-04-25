package com.example.forgetlost;


import android.app.ListActivity;
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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    TextView tvEmailErr, tvPasswordErr, tvPasswordReturnErr, tvNameErr;
    EditText etEmail, etPassword, etUser_name, etReturn_password;
    Button btReg;
    CheckBox checkBox;
    Vibrator vibrator;
    int mls = 100;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        {
            etEmail = findViewById(R.id.editText_gmail);
            etUser_name = findViewById(R.id.editText_name);
            etPassword = findViewById(R.id.editText_password);
            etReturn_password = findViewById(R.id.editText_return_password);
            checkBox = findViewById(R.id.checkBox);
            btReg = findViewById(R.id.btRegisterEnd);
            tvEmailErr = findViewById(R.id.tvEmailERROR);
            tvPasswordErr = findViewById(R.id.tvPasswordERROR);
            tvPasswordReturnErr = findViewById(R.id.tvPasswordReturnERROR);
            tvNameErr = findViewById(R.id.tvNameERROR);
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
        if (checkEmail(etEmail)) {
            tvEmailErr.setText("");
            if (etUser_name.getText().toString().length() != 0) {
                tvNameErr.setText("");
                if (etPassword.getText().toString().length() >= 8) {
                    tvPasswordErr.setText("");
                    if (!etPassword.getText().toString().matches("(.*) (.*)")) {
                        tvPasswordErr.setText("");
                        if (etPassword.getText().toString().equals(etReturn_password.getText().toString())) {
                            tvPasswordReturnErr.setText("");
                            if (checkBox.isChecked()) {

                                database = FirebaseDatabase.getInstance();
                                reference = database.getReference("users");

                                String email = etEmail.getText().toString();
                                String name = etUser_name.getText().toString();
                                String password = etPassword.getText().toString();
                                HelperClass helperClass = new HelperClass(email, name, password);

                                reference.child(name).setValue(helperClass);

                                Toast.makeText(this, "бобра", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(Registration.this, List.class));

                            } else {
                                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                                checkBox.setAnimation(shake);
                                checkBox.setDrawingCacheBackgroundColor(Color.RED);
                            }

                        } else {
                            etReturn_password.setError("Пароли не совпадают");
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
                etUser_name.setError("Вы не ввели имя");
                vibrator.vibrate(mls);
            }
        } else {
            etEmail.setError("Некорректно введена почта ");
            vibrator.vibrate(mls);
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


}