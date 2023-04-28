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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Registration extends AppCompatActivity {
    LinearLayout linearLayout;
    TextView tv2;
    EditText etEmail, etPassword, etname;
    Button btReg;
    CheckBox checkBox;
    Vibrator vibrator;
    int mls = 100, x = 0;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    String uid;
    View layer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        {
            etEmail = findViewById(R.id.editText_gmail);
            etname = findViewById(R.id.editText_name);
            etPassword = findViewById(R.id.editText_password);
            checkBox = findViewById(R.id.checkBox);
            btReg = findViewById(R.id.btRegisterEnd);
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            linearLayout = findViewById(R.id.lineal);
            tv2 = findViewById(R.id.tv2);
            LayoutInflater inflater = getLayoutInflater();
            layer1 = inflater.inflate(R.layout.layer1, null);
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

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(Registration.this, List.class));
        }
    }

    public void SingInGoogle(View view) {
        startActivity(new Intent(Registration.this, Login.class));
    }

    public void RegClick(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (checkEmail(etEmail)) {
            etEmail.setError(null);
            if (etname.getText().toString().length() != 0) {
                etname.setError(null);
                if (password.length() >= 8) {
                    etPassword.setError(null);
                    if (!password.matches("(.*) (.*)")) {
                        etPassword.setError(null);
                        if (checkBox.isChecked()) {
                            if (x == 0) {
                                linearLayout.addView(layer1);
                                setMargins(tv2, 18, 300, 18, 10);


                                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Registration.this, "Пользователь с этой почтой уже существует и вы ввели правильный пароль", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Registration.this, List.class));
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {


                                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(Registration.this, "Письмо было отправлено на вашу почту", Toast.LENGTH_SHORT).show();
                                                                btReg.setText("Проверить Верификацию");
                                                                x = 1;
                                                            } else {
                                                                Toast.makeText(Registration.this, "Не удалось отправить письмо", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(Registration.this, "Пользователь с этой почтой уже зарегестрирован", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            } else if (x == 1) {
                                FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Registration.this, "Вы успешно подтвердили почту", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Registration.this, List.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Registration.this, "Вы не подтвердили почту", Toast.LENGTH_SHORT).show();

                                    }
                                });

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

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}

class TimeClass extends Thread {
    private boolean isWork;

    private TimeClass() {
        isWork = true;
    }

    @Override
    public void run() {
        int time = 59;
        while (time >= 0 && isWork) {

            int finalTime = time;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                        }
                    });
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time--;
            if (time == 1) {
                //если время кончилось, то отправляем код еще раз
                //отправим запрос на почту для получения кода и обнулим счетчик


                //
                time = 59;
            }
        }


    }

    public void runOnUiThread(Runnable action) {
        throw new RuntimeException("Stub!");
    }

}