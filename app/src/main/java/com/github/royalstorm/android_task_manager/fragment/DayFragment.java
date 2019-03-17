package com.github.royalstorm.android_task_manager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.activity.AddEventActivity;

public class DayFragment extends Fragment {

    private ListView hoursList;

    private ArrayAdapter<String> adapter;

    //TODO: send time in AddEventActivity
    private static String time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day,
                container, false);

        hoursList = view.findViewById(R.id.hoursList);
        hoursList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View itemClicked,
                                    int position,
                                    long id) {
                time = adapter.getItem(position);

                createEvent();
            }
        });

        showHours();

        return view;
    }

    public void createEvent() {
        Intent intent = new Intent(getActivity(), AddEventActivity.class);

        startActivityForResult(intent, 1);
    }

    private void showHours() {
        String[] hours = getResources().getStringArray(R.array.hours);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, hours);

        hoursList.setAdapter(adapter);
    }
}
