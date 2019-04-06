package com.github.royalstorm.android_task_manager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DayFragment extends Fragment implements SelectDayDialog.SelectDayDialogListener {
    private GregorianCalendar gregorianCalendar;

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
        setPrevDayListener(view);
        setNextDayListener(view);

        return view;
    }

    private void setDatesFields(View view) {
        currentDay = view.findViewById(R.id.current_day);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            day = bundle.getInt("day");
            month = bundle.getInt("month");
            year = bundle.getInt("year");
        } else {
            day = new GregorianCalendar().get(Calendar.DAY_OF_WEEK);
            month = new GregorianCalendar().get(Calendar.MONTH);
            year = new GregorianCalendar().get(Calendar.YEAR);
        }

        gregorianCalendar = new GregorianCalendar(year, month, day);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM (E)", Locale.getDefault());

        String date = simpleDateFormat.format(gregorianCalendar.getTime());
        currentDay.setText(date);
    }

    private void setCurrentDayListener(View view) {
        currentDay = view.findViewById(R.id.current_day);
        currentDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDayDialog selectDayDialog = new SelectDayDialog();

                Bundle bundle = new Bundle();
                bundle.putInt("day", day);
                bundle.putInt("month", month);
                bundle.putInt("year", year);

                selectDayDialog.setTargetFragment(DayFragment.this, 1);
                selectDayDialog.setArguments(bundle);
                selectDayDialog.show(getFragmentManager(), "Select day dialog");
            }
        });
    }

    private void setPrevDayListener(View view) {
        prevDay = view.findViewById(R.id.prev_day);
        prevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);

                day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
                month = gregorianCalendar.get(Calendar.MONTH);
                year = gregorianCalendar.get(Calendar.YEAR);

                Bundle bundle = new Bundle();
                bundle.putInt("day", day);
                bundle.putInt("month", month);
                bundle.putInt("year", year);

                DayFragment dayFragment = new DayFragment();
                dayFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                        dayFragment).commit();
            }
        });
    }

    private void setNextDayListener(View view) {
        nextDay = view.findViewById(R.id.next_day);
        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

                day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
                month = gregorianCalendar.get(Calendar.MONTH);
                year = gregorianCalendar.get(Calendar.YEAR);

                Bundle bundle = new Bundle();
                bundle.putInt("day", day);
                bundle.putInt("month", month);
                bundle.putInt("year", year);

                DayFragment dayFragment = new DayFragment();
                dayFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                        dayFragment).commit();
            }
        });
    }

    private void createEvent(View itemClicked) {
        Intent intent = new Intent(getActivity(), AddTaskActivity.class);

        ConstraintLayout item = (ConstraintLayout) itemClicked;
        TextView hour = (TextView) item.getViewById(R.id.hour);
        String beginTime = hour.getText().toString();

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

    @Override
    public void setDay(int year, int month, int day) {
        Bundle bundle = new Bundle();
        bundle.putInt("day", day);
        bundle.putInt("month", month);
        bundle.putInt("year", year);

        DayFragment dayFragment = new DayFragment();
        dayFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                dayFragment).commit();
    }
}
