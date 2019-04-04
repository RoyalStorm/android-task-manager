package com.github.royalstorm.android_task_manager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
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
import com.github.royalstorm.android_task_manager.service.MockUpTaskService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DayFragment extends Fragment {

    private TextView currentDay;
    private TextView currentDate;

    private ListView hoursList;

    private HoursAdapter hoursAdapter;

    private static final SparseArray<String> daysOfWeek = new SparseArray<>();

    private int day;
    private int month;
    private int year;

    private View view;

    static {
        daysOfWeek.put(Calendar.MONDAY, "понедельник");
        daysOfWeek.put(Calendar.TUESDAY, "вторник");
        daysOfWeek.put(Calendar.WEDNESDAY, "среда");
        daysOfWeek.put(Calendar.THURSDAY, "четверг");
        daysOfWeek.put(Calendar.FRIDAY, "пятница");
        daysOfWeek.put(Calendar.SATURDAY, "суббота");
        daysOfWeek.put(Calendar.SUNDAY, "воскресенье");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day, container, false);

        setDatesFields(view);

        return view;
    }

    private void setDatesFields(View view) {
        Bundle bundle = this.getArguments();

        Calendar calendar = Calendar.getInstance();

        currentDay = view.findViewById(R.id.currentDay);
        currentDate = view.findViewById(R.id.currentDate);

        if (bundle == null) {
            currentDay.setText(daysOfWeek.get(calendar.get(Calendar.DAY_OF_WEEK)));

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            currentDate.setText(dateFormat.format(calendar.getTime()));
        } else {
            bundle = this.getArguments();

            day = bundle.getInt("day");
            month = bundle.getInt("month");
            year = bundle.getInt("year");

            String dayOfWeek = bundle.getString("currentDay");

            currentDay.setText(dayOfWeek);
            currentDate.setText(day + "/" + month + "/" + year);
        }
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
