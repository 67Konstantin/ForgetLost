package com.example.forgetlost;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.forgetlost.databinding.ActivityListActivtyBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

public class List extends AppCompatActivity {
    ImageView photoThing;
    ActivityListActivtyBinding binding;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Uri filePath;
    FirebaseAuth firebaseAuth;

    FirebaseDatabase dataBase;
    DatabaseReference reference;
    StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseUser user;
    String[] areas;
    AutoCompleteTextView autoCompleteTextView;
    Button btPublishNewPost;
    Spinner spinner1;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(List.this, Registration.class));
        }
        if (!firebaseAuth.getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(List.this, Verification.class));
        }
        binding = ActivityListActivtyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new LostThingsFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new LostThingsFragment());
                    break;
                case R.id.shorts:
                    replaceFragment(new GiftFragment());
                    break;
                case R.id.subscriptions:
                    replaceFragment(new MessageFragment());
                    break;
                case R.id.library:
                    replaceFragment(new ProfileFragment());
                    break;
            }

            return true;
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);
        areas = getResources().getStringArray(R.array.areas);
        autoCompleteTextView = dialog.findViewById(R.id.actv);
        btPublishNewPost = dialog.findViewById(R.id.btPublishNewPost);
        ImageView dismiss = dialog.findViewById(R.id.dismiss);
        ImageView nahodka = dialog.findViewById(R.id.nahodka);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, areas);
        autoCompleteTextView.setAdapter(adapter);
        photoThing = dialog.findViewById(R.id.photoThing);
        spinner1 = dialog.findViewById(R.id.spinner);
        EditText name1 = dialog.findViewById(R.id.nameThing), describing1 = dialog.findViewById(R.id.describingNew), conditions1 = dialog.findViewById(R.id.conditionsThing), area1 = dialog.findViewById(R.id.actv);


        {
            name1.setText("Кошелёк");
            describing1.setText("Ту должно быть описание");
            conditions1.setText("Тут должно быть условие");
            area1.setText("67 Смоленская область");
            // btPublishNewPost.setEnabled(false);
        }
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!(name1.getText().toString().trim().equals("") && describing1.getText().toString().trim().equals("") && conditions1.getText().toString().trim().equals("") && area1.getText().toString().trim().equals(""))) {
                    btPublishNewPost.setEnabled(true);

                } else btPublishNewPost.setEnabled(false);
            }
        };
        name1.addTextChangedListener(textWatcher);
        describing1.addTextChangedListener(textWatcher);
        conditions1.addTextChangedListener(textWatcher);
        area1.addTextChangedListener(textWatcher);


        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        photoThing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }
            }
        });
        btPublishNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Arrays.asList(areas).contains(area1.getText().toString())) {
                    String name = name1.getText().toString();
                    String describing = describing1.getText().toString();
                    String conditions = conditions1.getText().toString();
                    String area = area1.getText().toString();
                    Long data1 = System.currentTimeMillis();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", new Locale("ru"));
                    Date resultdate = new Date(data1);
                    String data = sdf.format(resultdate);
                    dataBase = FirebaseDatabase.getInstance();
                    uid = FirebaseAuth.getInstance().getUid();
                    reference = dataBase.getReference("things");

                    String r = UUID.randomUUID().toString();
                    StorageReference ref = storageReference.child("images/" + r);
                    HelperClassThings helperClassThings = new HelperClassThings(name, describing, conditions, area, data, "images/" + r);
                    addLostThing(helperClassThings, ref);
                } else area1.setError("Неверно введена область");
            }
        });

        @SuppressLint("ResourceType") ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.color_spiner, new String[]{"Находка", "Подарок"});
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner1.getSelectedItem().toString().equals("Находка")) {
                    nahodka.setImageResource(R.drawable.finding);
                } else {
                    nahodka.setImageResource(R.drawable.icon_gift);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (spinner1.getSelectedItem().toString().equals("Находка")) {
                    nahodka.setImageResource(R.drawable.finding);
                } else {
                    nahodka.setImageResource(R.drawable.icon_gift);
                }
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void addLostThing(HelperClassThings helperClassThings, StorageReference ref) {
        String id = reference.push().getKey();
        reference.child(spinner1.getSelectedItem().toString()).child(user.getUid()).child(id).setValue(helperClassThings);
        if (filePath != null) {
            ref.putFile(filePath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(List.this, "Готово", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(List.this, "Не получилось", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            filePath = data.getData();
            photoThing.setImageURI(filePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(List.this, "Необходимо предоставить доступ к галлереи", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}