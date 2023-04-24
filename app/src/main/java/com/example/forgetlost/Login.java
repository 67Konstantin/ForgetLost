package com.example.forgetlost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    EditText et_email_login, et_password_login;
    Button btLoginEnd;
    TextView tvPasswordERROR_login, tvEmailERROR_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        {
            et_email_login = findViewById(R.id.editText_gmail_login);
            et_password_login = findViewById(R.id.et_password_login);
            btLoginEnd = findViewById(R.id.btLoginEnd);
            tvPasswordERROR_login = findViewById(R.id.tvPasswordERROR_login);
            tvEmailERROR_login = findViewById(R.id.tvEmailERROR_login);
            btLoginEnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Registration.checkEmail(et_email_login)) {
                        tvEmailERROR_login.setText("");
                        if (!validateEmail() | !validatePassword()) {


                            startActivity(new Intent(Login.this,List.class));
                        } else {

                        }
                    } else {
                        et_email_login.setError("Неверно введена почта");
                    }
                }
            });
        }
    }

    public void GoToReg(View view) {
        startActivity(new Intent(Login.this, Registration.class));
    }


    public boolean validateEmail() {
        if (Registration.checkEmail(et_email_login)) {
            et_email_login.setError(null);
            return true;
        } else {
            et_email_login.setError("Неправильно введена почта");
            return false;
        }
    }

    public boolean validatePassword() {
        String val = et_password_login.getText().toString();
        if (val.isEmpty()) {
            et_password_login.setError("Поле ввода не может быть пустым");
            return false;
        } else {
            et_password_login.setError(null);
            return true;
        }
    }
}