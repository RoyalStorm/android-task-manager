package com.github.royalstorm.android_task_manager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.activity.AddTaskActivity;
import com.github.royalstorm.android_task_manager.activity.EditTaskActivity;
import com.github.royalstorm.android_task_manager.adapter.TasksAdapter;
import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.dto.EventInstanceResponse;
import com.github.royalstorm.android_task_manager.dto.EventResponse;
import com.github.royalstorm.android_task_manager.fragment.ui.dialog.SelectDayDialog;
import com.github.royalstorm.android_task_manager.service.MockUpTaskService;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayFragment extends Fragment implements SelectDayDialog.SelectDayDialogListener {
    private MockUpTaskService mockUpTaskService = new MockUpTaskService();

    private EventResponse eventResponse;

    private GregorianCalendar gregorianCalendar;

    private TextView prevDay;
    private TextView currentDay;
    private TextView nextDay;

    private ListView tasksList;

    private TasksAdapter tasksAdapter;

    private int day;
    private int month;
    private int year;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day, container, false);

        setDate(view);
        setCurrentDayListener(view);
        setPrevDayListener(view);
        setNextDayListener(view);

        FloatingActionButton fab = view.findViewById(R.id.add_event);
        fab.setOnClickListener(view -> createTask());

        return view;
    }

    private void setDate(View view) {
        currentDay = view.findViewById(R.id.current_day);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            day = bundle.getInt("day");
            month = bundle.getInt("month");
            year = bundle.getInt("year");
        } else {
            day = new GregorianCalendar().get(Calendar.DAY_OF_MONTH);
            month = new GregorianCalendar().get(Calendar.MONTH);
            year = new GregorianCalendar().get(Calendar.YEAR);
        }

        gregorianCalendar = new GregorianCalendar(year, month, day);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM (E) yyyy г.", Locale.getDefault());

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

    private void createTask() {
        Intent intent = new Intent(getActivity(), AddTaskActivity.class);
        Task task = new Task();
        task.setBeginMinute(new GregorianCalendar().getTime().getMinutes());
        task.setBeginHour(new GregorianCalendar().getTime().getHours());
        task.setBeginDay(day);
        task.setBeginMonth(month);
        task.setBeginYear(year);

        task.setEndDay(day);
        task.setEndMonth(month);
        task.setEndYear(year);

        intent.putExtra(Task.class.getSimpleName(), task);

        startActivity(intent);
    }

    private void editTask(int id) {
        Intent intent = new Intent(getActivity(), EditTaskActivity.class);
        Task task = mockUpTaskService.findById(id);
        intent.putExtra(Task.class.getSimpleName(), task);
        startActivity(intent);
    }

    private void getCurrentEvents(Long from, Long to) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        retrofitClient.getEventRepository().getInstancesByInterval(from, to).enqueue(new Callback<EventInstanceResponse>() {
            @Override
            public void onResponse(Call<EventInstanceResponse> call, Response<EventInstanceResponse> response) {
                if (response.isSuccessful()) {
                    //eventResponse = response.body();
                }
            }

            @Override
            public void onFailure(Call<EventInstanceResponse> call, Throwable t) {

            }
        });
    }

    private void showTasks(View view) {
        getCurrentEvents(
                new GregorianCalendar(year, month, day, 0, 0).getTimeInMillis(),
                new GregorianCalendar(year, month, day, 23, 59).getTimeInMillis());

        tasksList = view.findViewById(R.id.tasks_list);
        //tasksAdapter = new TasksAdapter(getContext(), R.layout.tasks_list_item, currentTasks);
        //tasksList.setAdapter(tasksAdapter);
        /*tasksList.setOnItemClickListener((parent, view1, position, id) -> {
            Task task = (Task) parent.getAdapter().getItem(position);
            editTask(task.getId());
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        showTasks(view);
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

    private GregorianCalendar timestampToDate(Long millis) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(millis);

        return calendar;
    }
}
