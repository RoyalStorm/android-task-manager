package com.github.royalstorm.android_task_manager.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.royalstorm.android_task_manager.R;

public class DayFragment extends Fragment {

    private ListView hoursList;

    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day,
                container, false);

        hoursList = view.findViewById(R.id.hoursList);
        showHours();

        return view;
    }

    private void showHours() {
        String[] hours = getResources().getStringArray(R.array.hours);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, hours);

        hoursList.setAdapter(adapter);
    }
}
