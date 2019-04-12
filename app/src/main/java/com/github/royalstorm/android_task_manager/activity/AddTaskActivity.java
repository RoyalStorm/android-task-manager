package com.github.royalstorm.android_task_manager.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private int beginMinute;
    private int beginHour;
    private int beginDay;
    private int beginMonth;
    private int beginYear;

    private int endMinute;
    private int endHour;
    private int endDay;
    private int endMonth;
    private int endYear;

    private TextView taskBeginDate;
    private TextView taskEndDate;
    private TextView taskBeginTime;
    private TextView taskEndTime;

    private EditText taskName;
    private EditText taskDetails;

    /*private TimePickerDialog.OnTimeSetListener showTimePicker = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            if (view.getId() == R.id.task_begin_time)
                Log.d("_______________","begin");
            else if (view.getId() == R.id.task_end_time)
                Log.d("____________________", "end");
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Новое событие");

        setTaskBeginDateListener();
        setTaskBeginTime();

        setTaskEndDateListener();
        setTaskEndTime();

        taskName = findViewById(R.id.task_name);
        taskDetails = findViewById(R.id.task_details);
    }

    private void createTask() {
        if (taskName.getText().toString().trim().isEmpty()) {
            Snackbar.make(getWindow().getDecorView().
                    getRootView(), "Заголовок не может быть пустым", Snackbar.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();

        mockUpEventService.create(
                new Task(
                        MockUpTaskService.getCounter(),
                        "Me",
                        this.taskName.getText().toString(),
                        taskDetails.getText().toString(),
                        beginMinute,
                        beginHour,
                        beginDay,
                        beginMonth,
                        beginYear,
                        endMinute,
                        endHour,
                        endDay,
                        endMonth,
                        endYear)
        );

        setResult(RESULT_OK, intent);
        finish();
    }

    private void setTaskBeginDateListener() {
        taskBeginDate = findViewById(R.id.task_begin_date);
        taskBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment beginDatePicker = new DatePickerFragment();
                beginDatePicker.show(getSupportFragmentManager(), "Begin date picker");
            }
        });
    }

    private void setTaskEndDateListener() {
        taskEndDate = findViewById(R.id.task_end_date);
        taskEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment endDatePicker = new DatePickerFragment();
                endDatePicker.show(getSupportFragmentManager(), "End date picker");
            }
        });
    }

    private void setTaskBeginTime() {
        taskBeginTime = findViewById(R.id.task_begin_time);
        taskBeginTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment beginTimePicker = new TimePickerFragment();
                beginTimePicker.show(getSupportFragmentManager(), "Begin time picker");
            }
        });
    }

    private void setTaskEndTime() {
        taskEndTime = findViewById(R.id.task_end_time);
        taskEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment endTimePicker = new TimePickerFragment();
                endTimePicker.show(getSupportFragmentManager(), "End time picker");
            }
        });
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy (E)", Locale.getDefault());

        String date = simpleDateFormat.format(calendar.getTime());

        taskBeginDate.setText(date);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.getId() == R.id.task_begin_time)
        Log.d("_______________","begin");
            else if (view.getId() == R.id.task_end_time)
            Log.d("____________________", "end");
        taskBeginTime.setText(hourOfDay + ":" + minute);
    }
}
