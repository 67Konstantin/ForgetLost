package com.example.forgetlost;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class List extends AppCompatActivity {
    ImageView photoThing;
    ActivityListActivtyBinding binding;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Uri imageUri;
    FirebaseAuth firebaseAuth;

    FirebaseDatabase dataBase;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("things");
    StorageReference reference = FirebaseStorage.getInstance().getReference();

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
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
        Spinner spinner = dialog.findViewById(R.id.spinner);

//        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,R.array.areas,R.layout.color_spiner);
//        spinner.setAdapter(adapter1);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);
        String[] areas = getResources().getStringArray(R.array.areas);
        AutoCompleteTextView autoCompleteTextView = dialog.findViewById(R.id.actv);
        Button btPublishNewPost = dialog.findViewById(R.id.btPublishNewPost);

        ImageView nahodka = dialog.findViewById(R.id.nahodka);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, areas);
        autoCompleteTextView.setAdapter(adapter);
        photoThing = dialog.findViewById(R.id.photoThing);
        ImageView dismiss = dialog.findViewById(R.id.dismiss);

        Spinner spinner1 = dialog.findViewById(R.id.spinner);
        ImageView image1 = dialog.findViewById(R.id.photoThing);

        EditText name1 = dialog.findViewById(R.id.nameThing), describing1 = dialog.findViewById(R.id.describingNew), conditions1 = dialog.findViewById(R.id.conditionsThing), area1 = dialog.findViewById(R.id.actv);
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


                String name = name1.getText().toString();
                String describing = describing1.getText().toString();
                String conditions = conditions1.getText().toString();
                String area = area1.getText().toString();
                String spinner = spinner1.getSelectedItem().toString();
                dataBase = FirebaseDatabase.getInstance();
                uid = FirebaseAuth.getInstance().getUid();
            }
        });

        @SuppressLint("ResourceType") ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.color_spiner, new String[]{"Находка", "Подарок"});
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter( adapter1);
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

    private String getFileExtensions(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
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
            imageUri = data.getData();
            photoThing.setImageURI(imageUri);
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