package com.example.forgetlost.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.forgetlost.ALodingDialog;
import com.example.forgetlost.R;
import com.example.forgetlost.helperClasses.HelperClassThings;
import com.example.forgetlost.helperClasses.HelperClassUsers;
import com.example.forgetlost.helperClasses.MyAdapterThings;
import com.example.forgetlost.helperClasses.MyAdapterUsers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsersMessagesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public UsersMessagesFragment() {
    }

    public static UsersMessagesFragment newInstance(String param1, String param2) {
        UsersMessagesFragment fragment = new UsersMessagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    List<HelperClassUsers> dataList;
    MyAdapterUsers adapter;

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    SearchView searchView;
    private ALodingDialog aLodingDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_messages, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUsersMessages);
        searchView = view.findViewById(R.id.searchViewUsers);
        searchView.clearFocus();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        aLodingDialog = new ALodingDialog(getActivity());
        aLodingDialog.show();
        Handler handler = new Handler();
        Runnable runnable = () -> aLodingDialog.cancel();
        handler.postDelayed(runnable,400000);
        dataList = new ArrayList<>();
        adapter = new MyAdapterUsers(getActivity(), dataList);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        HelperClassUsers helperClassUsers = ds.getValue(HelperClassUsers.class);
                        dataList.add(helperClassUsers);
                    }
                    adapter.notifyDataSetChanged();
                    aLodingDialog.cancel();
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                aLodingDialog.cancel();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
        return view;
    }
    public void searchList(String text) {
        ArrayList<HelperClassUsers> searchList = new ArrayList<>();
        for (HelperClassUsers dataClass : dataList) {
            if (dataClass.getName().toLowerCase().contains(text.toLowerCase()) || dataClass.getEmail().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }
}