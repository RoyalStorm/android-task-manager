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

public class RulesSharingFragment extends Fragment {
    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events_fragment, container, false);
        super.onCreate(savedInstanceState);

        return view;
    }
}
