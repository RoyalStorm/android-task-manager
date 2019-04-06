package com.github.royalstorm.android_task_manager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.activity.AddTaskActivity;
import com.github.royalstorm.android_task_manager.adapter.HoursAdapter;
import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.fragment.ui.dialog.SelectDayDialog;
import com.github.royalstorm.android_task_manager.service.MockUpTaskService;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DayFragment extends Fragment {
    private TextView prevDay;
    private TextView currentDay;
    private TextView nextDay;

    private ListView hoursList;

    private HoursAdapter hoursAdapter;

    private int day;
    private int month;
    private int year;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day, container, false);

        setDatesFields(view);
        setCurrentDayListener(view);

        return view;
    }

    private void setDatesFields(View view) {
        currentDay = view.findViewById(R.id.current_day);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM (E)", Locale.getDefault());
        simpleDateFormat.setCalendar(new GregorianCalendar());
        String date = simpleDateFormat.format(new GregorianCalendar().getTime());

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            day = bundle.getInt("day");
            month = bundle.getInt("month");
            year = bundle.getInt("year");

            date = simpleDateFormat.format(new GregorianCalendar(year, month, day).getTime());
        }

        currentDay.setText(date);
    }

    private void setCurrentDayListener(View view) {
        currentDay = view.findViewById(R.id.current_day);
        currentDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void openDialog() {
        SelectDayDialog selectDayDialog = new SelectDayDialog();
        selectDayDialog.show(getFragmentManager(), "Select day dialog");
    }

    private void createEvent(View itemClicked) {
        Intent intent = new Intent(getActivity(), AddTaskActivity.class);

        ConstraintLayout item = (ConstraintLayout) itemClicked;
        TextView hour = (TextView) item.getViewById(R.id.hour);
        String beginTime = hour.getText().toString();

        // Position is equals selected time
        intent.putExtra("beginTime", beginTime);
        intent.putExtra("day", day);
        intent.putExtra("month", month);
        intent.putExtra("year", year);

        startActivity(intent);
    }

    private void showHours(View view) {
        hoursList = view.findViewById(R.id.hoursList);

        String[] hours = getResources().getStringArray(R.array.hours);
        List<Task> events = getCurrentEvents(day + "/" + month + "/" + year);

        hoursAdapter = new HoursAdapter(getContext(), R.layout.hour_list_item, hours, events);

        hoursList.setAdapter(hoursAdapter);
        hoursList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View itemClicked,
                                    int position,
                                    long id) {
                createEvent(itemClicked);
            }
        });
    }

    private List<Task> getCurrentEvents(String date) {
        MockUpTaskService mockUpEventService = new MockUpTaskService();

        return mockUpEventService.findByDate(date);
    }

    @Override
    public void onResume() {
        super.onResume();
        showHours(view);
    }
}
