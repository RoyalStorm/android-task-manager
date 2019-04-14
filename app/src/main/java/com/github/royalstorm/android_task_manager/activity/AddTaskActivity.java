package com.github.royalstorm.android_task_manager.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.fragment.ui.DatePickerFragment;
import com.github.royalstorm.android_task_manager.fragment.ui.TimePickerFragment;
import com.github.royalstorm.android_task_manager.service.MockUpTaskService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private MockUpTaskService mockUpEventService = new MockUpTaskService();

    private GregorianCalendar gregorianCalendar;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy (E)", Locale.getDefault());

    private DialogFragment picker;

    private Task task;

    private TextView taskBeginDate;
    private TextView taskEndDate;
    private TextView taskBeginTime;
    private TextView taskEndTime;

    private EditText taskName;
    private EditText taskDetails;

    private boolean IS_BEGIN_DATE = true;
    private boolean IS_BEGIN_TIME = true;

    private View.OnClickListener dateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.task_begin_date:
                    IS_BEGIN_DATE = true;
                    break;
                case R.id.task_end_date:
                    IS_BEGIN_DATE = false;
                    break;
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable(Task.class.getSimpleName(), task);
            bundle.putBoolean("IS_BEGIN_DATE", IS_BEGIN_DATE);
            picker = new DatePickerFragment();
            picker.setArguments(bundle);
            picker.show(getSupportFragmentManager(), "Date picker");
        }
    };

    private View.OnClickListener timeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.task_begin_time:
                    IS_BEGIN_TIME = true;
                    break;
                case R.id.task_end_time:
                    IS_BEGIN_TIME = false;
                    break;
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable(Task.class.getSimpleName(), task);
            bundle.putBoolean("IS_BEGIN_TIME", IS_BEGIN_TIME);
            picker = new TimePickerFragment();
            picker.setArguments(bundle);
            picker.show(getSupportFragmentManager(), "Time picker");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Новое событие");

        initDateFields();

        setTaskBeginDateListener();
        setTaskBeginTime();

        setTaskEndDateListener();
        setTaskEndTime();

        taskName = findViewById(R.id.task_name);
        taskDetails = findViewById(R.id.task_details);
    }

    private void initDateFields() {
        taskBeginDate = findViewById(R.id.task_begin_date);
        taskEndDate = findViewById(R.id.task_end_date);
        taskBeginTime = findViewById(R.id.task_begin_time);
        taskEndTime = findViewById(R.id.task_end_time);

        Bundle bundle = getIntent().getExtras();
        task = (Task) bundle.getSerializable(Task.class.getSimpleName());

        gregorianCalendar = new GregorianCalendar(task.getBeginYear(), task.getBeginMonth(), task.getBeginDay(), task.getBeginHour(), task.getBeginMinute());

        String taskDate = simpleDateFormat.format(gregorianCalendar.getTime());
        taskBeginDate.setText(taskDate);
        taskEndDate.setText(taskDate);

        gregorianCalendar.add(Calendar.HOUR, 1);

        task.setBeginHour(gregorianCalendar.getTime().getHours());
        task.setBeginMinute(0);
        task.setEndHour(gregorianCalendar.getTime().getHours());
        task.setEndMinute(0);

        taskBeginTime.setText(gregorianCalendar.getTime().getHours() + ":00");
        taskEndTime.setText(gregorianCalendar.getTime().getHours() + ":00");
    }

    private void createTask() {
        if (taskName.getText().toString().trim().isEmpty()) {
            Snackbar.make(getWindow().getDecorView().
                    getRootView(), "Заголовок не может быть пустым", Snackbar.LENGTH_LONG).show();
            return;
        } else {
            GregorianCalendar begin = new GregorianCalendar(task.getBeginYear(), task.getBeginMonth(), task.getBeginDay(), task.getBeginHour(), task.getBeginMinute());
            GregorianCalendar end = new GregorianCalendar(task.getEndYear(), task.getEndMonth(), task.getEndDay(), task.getEndHour(), task.getEndMinute());

            if (begin.after(end)) {
                Snackbar.make(getWindow().getDecorView().
                        getRootView(), "Событие не может завершиться раньше, чем начаться", Snackbar.LENGTH_LONG).show();
                return;
            }
        }

        Intent intent = new Intent();

        //Set other fields and add to mock up
        task.setId(MockUpTaskService.getCounter());
        task.setOwner("Me");
        task.setName(taskName.getText().toString());
        task.setDetails(taskDetails.getText().toString());

        mockUpEventService.create(task);

        setResult(RESULT_OK, intent);
        finish();
    }

    private void setTaskBeginDateListener() {
        taskBeginDate.setOnClickListener(dateListener);
    }

    private void setTaskEndDateListener() {
        taskEndDate.setOnClickListener(dateListener);
    }

    private void setTaskBeginTime() {
        taskBeginTime.setOnClickListener(timeListener);
    }

    private void setTaskEndTime() {
        taskEndTime.setOnClickListener(timeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_event:
                createTask();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        gregorianCalendar.set(Calendar.YEAR, year);
        gregorianCalendar.set(Calendar.MONTH, month);
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String date = simpleDateFormat.format(gregorianCalendar.getTime());

        if (IS_BEGIN_DATE) {
            task.setBeginYear(year);
            task.setBeginMonth(month);
            task.setBeginDay(dayOfMonth);

            taskBeginDate.setText(date);
        } else {
            task.setEndYear(year);
            task.setEndMonth(month);
            task.setEndDay(dayOfMonth);

            taskEndDate.setText(date);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (IS_BEGIN_TIME) {
            task.setBeginHour(hourOfDay);
            task.setBeginMinute(minute);

            taskBeginTime.setText(hourOfDay + ":" + minute);
        } else {
            task.setEndHour(hourOfDay);
            task.setEndMinute(minute);

            taskEndTime.setText(hourOfDay + ":" + minute);
        }
    }
}
