package com.github.royalstorm.android_task_manager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.activity.EventsActivity;

import java.util.Calendar;

public class DayFragment extends Fragment {

    TextView currentDay;
    TextView currentDate;

    private ListView hoursList;

    private ArrayAdapter<String> adapter;

    private static final SparseArray<String> daysOfWeek = new SparseArray<>();

    static {
        daysOfWeek.put(Calendar.MONDAY, "Понедельник");
        daysOfWeek.put(Calendar.TUESDAY, "Вторник");
        daysOfWeek.put(Calendar.WEDNESDAY, "Среда");
        daysOfWeek.put(Calendar.THURSDAY, "Четверг");
        daysOfWeek.put(Calendar.FRIDAY, "Пятница");
        daysOfWeek.put(Calendar.SATURDAY, "Суббота");
        daysOfWeek.put(Calendar.SUNDAY, "Воскресенье");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        setDatesFields(view);

        hoursList = view.findViewById(R.id.hoursList);
        showHours();
        hoursList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View itemClicked,
                                    int position,
                                    long id) {
                createEvent(position);
            }
        });

        return view;
    }

    private void setDatesFields(View view) {
        currentDay = view.findViewById(R.id.currentDay);
        currentDate = view.findViewById(R.id.currentDate);

        Calendar calendar = Calendar.getInstance();

        currentDay.setText(daysOfWeek.get(calendar.get(Calendar.DAY_OF_WEEK)));
    }

    private void createEvent(int position) {
        Intent intent = new Intent(getActivity(), EventsActivity.class);

        // Position is equals selected time
        intent.putExtra("beginTime", position);

        startActivity(intent);
    }

    private void showHours() {
        String[] hours = getResources().getStringArray(R.array.hours);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, hours);

        hoursList.setAdapter(adapter);
    }
}
