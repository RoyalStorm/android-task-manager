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
import com.github.royalstorm.android_task_manager.service.TaskService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MonthFragment extends Fragment {
    private com.applandeo.materialcalendarview.CalendarView calendar;

    private MockUpTaskService mockUpTaskService = new MockUpTaskService();

    private List<EventDay> events = new ArrayList<>();
    private List<Task> tasks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        calendar = view.findViewById(R.id.calendar);

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

    private void showTasks() {
        tasks = mockUpTaskService.findAll();

        for (Task task : tasks) {
            GregorianCalendar beginEventCalendar = new GregorianCalendar(task.getBeginYear(), task.getBeginMonth(), task.getBeginDay());
            GregorianCalendar currentEventCalendar = beginEventCalendar;
            GregorianCalendar endEventCalendar = new GregorianCalendar(task.getEndYear(), task.getEndMonth(), task.getEndDay());

            while (!(currentEventCalendar.before(beginEventCalendar) || currentEventCalendar.after(endEventCalendar))) {
                events.add(new EventDay(new GregorianCalendar(currentEventCalendar.get(Calendar.YEAR), currentEventCalendar.get(Calendar.MONTH), currentEventCalendar.get(Calendar.DAY_OF_MONTH)), R.drawable.ic_star));
                currentEventCalendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        calendar.setEvents(events);
    }

    @Override
    public void onResume() {
        super.onResume();
        showTasks();

        TaskService taskService = new TaskService();
    }
}
