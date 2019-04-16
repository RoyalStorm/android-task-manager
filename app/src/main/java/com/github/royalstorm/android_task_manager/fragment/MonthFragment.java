package com.github.royalstorm.android_task_manager.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.EventDay;
import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.service.MockUpTaskService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MonthFragment extends Fragment {
    private com.applandeo.materialcalendarview.CalendarView calendar;

    private MockUpTaskService mockUpTaskService = new MockUpTaskService();

    GregorianCalendar currentCalendar = new GregorianCalendar();

    private List<EventDay> events = new ArrayList<>();
    private List<Task> tasks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        calendar = view.findViewById(R.id.calendar);

        tasks = getCurrentTasks(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH));

        for (Task task: tasks) {
            for (int i = task.getBeginDay(); i <= task.getEndDay(); i++) {
                GregorianCalendar currentEventCalendar = new GregorianCalendar(task.getBeginYear(), task.getBeginMonth(), i);
                events.add(new EventDay(currentEventCalendar, R.drawable.ic_star));
            }
        }

        calendar.setEvents(events);

        calendar.setOnDayClickListener(eventDay -> {
            Bundle bundle = new Bundle();

            bundle.putInt("day", eventDay.getCalendar().get(Calendar.DAY_OF_MONTH));
            bundle.putInt("month", eventDay.getCalendar().get(Calendar.MONTH));
            bundle.putInt("year", eventDay.getCalendar().get(Calendar.YEAR));

            DayFragment dayFragment = new DayFragment();
            dayFragment.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                    dayFragment).commit();
        });

        return view;
    }

    private List<Task> getCurrentTasks(int year, int month) {
        return mockUpTaskService.findByYearAndMonth(year, month);
    }
}
