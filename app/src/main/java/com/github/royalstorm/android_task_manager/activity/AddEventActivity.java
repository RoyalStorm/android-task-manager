package com.github.royalstorm.android_task_manager.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.fragment.ui.DatePickerFragment;
import com.github.royalstorm.android_task_manager.fragment.ui.TimePickerFragment;
import com.github.royalstorm.android_task_manager.service.MockUpEventService;

import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private MockUpEventService mockUpEventService = new MockUpEventService();

    private TextView eventBeginDate;
    private TextView eventEndDate;
    private TextView eventBeginTime;
    private TextView eventEndTime;

    private EditText eventName;
    private EditText eventDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

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
            Toast.makeText(this, "Заголовок не может быть пустым", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();

        String beginTime = getIntent().getExtras().get("beginTime").toString();

        int day = getIntent().getExtras().getInt("day");
        int month = getIntent().getExtras().getInt("month");
        int year = getIntent().getExtras().getInt("year");

        mockUpEventService.create(
                new Event(
                        MockUpEventService.getCounter(),
                        "Me",
                        this.eventName.getText().toString(),
                        day + "/" + month + "/" + year,
                        beginTime,
                        "...")
        );

        setResult(RESULT_OK, intent);

        finish();
    }

    private void setEventBeginDateListener() {
        eventBeginDate = findViewById(R.id.eventDateBegin);
        eventBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment beginDatePicker = new DatePickerFragment();
                beginDatePicker.show(getSupportFragmentManager(), "Begin date picker");
            }
        });
    }

    private void setEventEndDateListener() {
        eventEndDate = findViewById(R.id.eventDateEnd);
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
        menuInflater.inflate(R.menu.add_event_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveEvent:
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
    }
}
