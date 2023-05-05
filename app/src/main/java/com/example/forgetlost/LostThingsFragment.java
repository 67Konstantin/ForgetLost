package com.example.forgetlost;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class LostThingsFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;


    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    FirebaseListAdapter mAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_lost_things, container, false);


        return view;
    }



}