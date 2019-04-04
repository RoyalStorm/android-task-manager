package com.github.royalstorm.android_task_manager.activity;

import android.app.DatePickerDialog;
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

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.fragment.ui.DatePickerFragment;
import com.github.royalstorm.android_task_manager.fragment.ui.TimePickerFragment;
import com.github.royalstorm.android_task_manager.service.MockUpTaskService;

import java.text.DateFormat;
import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private MockUpTaskService mockUpEventService = new MockUpTaskService();

    private TextView eventBeginDate;
    private TextView eventEndDate;
    private TextView eventBeginTime;
    private TextView eventEndTime;

    private EditText eventName;
    private EditText eventDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Новое событие");

        setEventBeginDateListener();
        setEventEndDateListener();

        setEventBeginTime();
        setEventEndTime();

        eventName = findViewById(R.id.eventName);
        eventDetails = findViewById(R.id.eventDetails);
    }

    private void createEvent() {
        if (eventName.getText().toString().trim().isEmpty()) {
            Snackbar.make(getWindow().getDecorView().
                    getRootView(), "Заголовок не может быть пустым", Snackbar.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();

        String beginTime = getIntent().getExtras().get("beginTime").toString();

        int day = getIntent().getExtras().getInt("day");
        int month = getIntent().getExtras().getInt("month");
        int year = getIntent().getExtras().getInt("year");

        mockUpEventService.create(
                new Task(
                        MockUpTaskService.getCounter(),
                        "Me",
                        this.eventName.getText().toString(),
                        eventDetails.getText().toString(),
                        "...",
                        "...",
                        "...",
                        "...")
        );

        setResult(RESULT_OK, intent);

        finish();
    }

    private void setEventBeginDateListener() {
        eventBeginDate = findViewById(R.id.eventBeginDate);
        eventBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment beginDatePicker = new DatePickerFragment();
                beginDatePicker.show(getSupportFragmentManager(), "Begin date picker");
            }
        });
    }

    private void setEventEndDateListener() {
        eventEndDate = findViewById(R.id.eventEndDate);
        eventEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment endDatePicker = new DatePickerFragment();
                endDatePicker.show(getSupportFragmentManager(), "End date picker");
            }
        });
    }

    private void setEventBeginTime() {
        eventBeginTime = findViewById(R.id.eventBeginTime);
        eventBeginTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment beginTimePicker = new TimePickerFragment();
                beginTimePicker.show(getSupportFragmentManager(), "Begin time picker");
            }
        });
    }

    private void setEventEndTime() {
        eventEndTime = findViewById(R.id.eventEndTime);
        eventEndTime.setOnClickListener(new View.OnClickListener() {
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
                createEvent();
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

        eventBeginDate.setText(DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()));
    }
}
