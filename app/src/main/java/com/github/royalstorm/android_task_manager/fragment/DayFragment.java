package com.github.royalstorm.android_task_manager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.activity.AddTaskActivity;
import com.github.royalstorm.android_task_manager.activity.EditTaskActivity;
import com.github.royalstorm.android_task_manager.adapter.EventAdapter;
import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.dto.EventInstanceResponse;
import com.github.royalstorm.android_task_manager.fragment.ui.dialog.SelectDayDialog;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayFragment extends Fragment implements SelectDayDialog.SelectDayDialogListener {
    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    private GregorianCalendar gregorianCalendar;

    private TextView prevDay;
    private TextView currentDay;
    private TextView nextDay;

    private int day;
    private int month;
    private int year;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        setDate(view);
        setCurrentDayListener(view);
        setPrevDayListener(view);
        setNextDayListener(view);

        FloatingActionButton fab = view.findViewById(R.id.add_event);
        fab.setOnClickListener(v -> createTask());

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventAdapter = new EventAdapter();

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

        currentDay.setText(new SimpleDateFormat("d MMMM (E) yyyy", Locale.getDefault()).format(gregorianCalendar.getTime()));
    }

    private void setCurrentDayListener(View view) {
        currentDay = view.findViewById(R.id.current_day);
        currentDay.setOnClickListener(v -> {
            SelectDayDialog selectDayDialog = new SelectDayDialog();

            Bundle bundle = new Bundle();
            bundle.putInt("day", day);
            bundle.putInt("month", month);
            bundle.putInt("year", year);

            selectDayDialog.setTargetFragment(DayFragment.this, 1);
            selectDayDialog.setArguments(bundle);
            selectDayDialog.show(getFragmentManager(), "Select day dialog");
        });
    }

    private void setPrevDayListener(View view) {
        prevDay = view.findViewById(R.id.prev_day);
        prevDay.setOnClickListener(v -> {
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
        });
    }

    private void setNextDayListener(View view) {
        nextDay = view.findViewById(R.id.next_day);
        nextDay.setOnClickListener(v -> {
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
        startActivity(intent);
    }

    private void getEvents(GregorianCalendar from, GregorianCalendar to) {
        retrofitClient.getEventRepository().getInstancesByInterval(from.getTimeInMillis(), to.getTimeInMillis()).enqueue(new Callback<EventInstanceResponse>() {
            @Override
            public void onResponse(Call<EventInstanceResponse> call, Response<EventInstanceResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        eventAdapter.setItems(Arrays.asList(response.body().getData()));
                        recyclerView.setAdapter(eventAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<EventInstanceResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getEvents(
                new GregorianCalendar(year, month, day, 0, 0),
                new GregorianCalendar(year, month, day, 23, 59)
        );
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
