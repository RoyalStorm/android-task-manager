package com.github.royalstorm.android_task_manager.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;

public class RulesManagementFragment extends Fragment {

    private RetrofitClient retrofitClient;

    private FirebaseAuth firebaseAuth;
    private String userToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rules_management, container, false);

        retrofitClient = RetrofitClient.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();

        return view;
    }
}
