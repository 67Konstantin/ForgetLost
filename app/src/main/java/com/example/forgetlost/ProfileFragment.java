package com.example.forgetlost;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment {
    Button btSingOut;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    FrameLayout flImageProfile;
    ImageView ivImageProfile;
    TextView tvNameProfile, tvEmailProfile, tvSettingsProfile, tvMyRecords, tvAboutApp;
    String uid;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        {
            user = firebaseAuth.getCurrentUser();
            flImageProfile = view.findViewById(R.id.flImageProfile);
            ivImageProfile = view.findViewById(R.id.ivImageProfile);
            tvNameProfile = view.findViewById(R.id.tvNameProfile);
            tvEmailProfile = view.findViewById(R.id.tvEmailProfile);
            tvSettingsProfile = view.findViewById(R.id.tvSettingsProfile);
            tvAboutApp = view.findViewById(R.id.tvAboutApp);
            tvMyRecords = view.findViewById(R.id.tvMyRecords);
            btSingOut = view.findViewById(R.id.button);
            uid = user.getUid();
        }
        btSingOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), Registration.class));
            }
        });
        tvSettingsProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),SettingsProfile.class));
            }
        });
        tvMyRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyRecords.class));
            }
        });
        return view;

    }
}