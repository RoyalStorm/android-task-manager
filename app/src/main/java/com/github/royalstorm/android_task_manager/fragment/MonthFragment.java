package com.github.royalstorm.android_task_manager.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.github.royalstorm.android_task_manager.R;

public class MonthFragment extends Fragment {

    NavigationView navigationView;

    CalendarView calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        calendar = view.findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(
                new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        navigationView = view.findViewById(R.id.nav_view);

                        getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                                new DayFragment()).commit();
                        // navigationView.setCheckedItem(R.id.nav_day);
                    }
                }
        );

        return view;
    }
}
