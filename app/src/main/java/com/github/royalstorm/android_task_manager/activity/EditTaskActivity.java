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

public class EditTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private MockUpTaskService mockUpTaskService = new MockUpTaskService();

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy (E)", Locale.getDefault());

    private GregorianCalendar begin;
    private GregorianCalendar end;

    private Task task;

    private EditText taskName;
    private EditText taskDetails;

    private TextView taskBeginDate;
    private TextView taskEndDate;
    private TextView taskBeginTime;
    private TextView taskEndTime;

    private DialogFragment picker;

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
        setTitle("Редактирование события");

        Bundle bundle = getIntent().getExtras();
        task = (Task) bundle.getSerializable(Task.class.getSimpleName());

        findComponents();
        initFields();

        setTaskBeginDateListener();
        setTaskBeginTime();

        setTaskEndDateListener();
        setTaskEndTime();
    }

    private void findComponents() {
        taskName = findViewById(R.id.task_name);
        taskDetails = findViewById(R.id.task_details);
        taskBeginDate = findViewById(R.id.task_begin_date);
        taskEndDate = findViewById(R.id.task_end_date);
        taskBeginTime = findViewById(R.id.task_begin_time);
        taskEndTime = findViewById(R.id.task_end_time);
    }

    private void initFields() {
        taskName.setText(task.getName());
        taskDetails.setText(task.getDetails());

        begin = new GregorianCalendar(task.getBeginYear(), task.getBeginMonth(), task.getBeginDay(), task.getBeginHour(), task.getBeginMinute());
        end = new GregorianCalendar(task.getEndYear(), task.getEndMonth(), task.getEndDay(), task.getEndHour(), task.getEndMinute());

        taskBeginDate.setText(simpleDateFormat.format(begin.getTime()));
        taskEndDate.setText(simpleDateFormat.format(end.getTime()));

        taskBeginTime.setText(getTimeFormat(begin.getTime().getHours(), begin.getTime().getMinutes()));
        taskEndTime.setText(getTimeFormat(end.getTime().getHours(), end.getTime().getMinutes()));
    }

    private void deleteTask(int id) {
        mockUpTaskService.delete(id);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void editTask(Task task) {
        if (taskName.getText().toString().trim().isEmpty()) {
            Snackbar.make(getWindow().getDecorView().
                    getRootView(), "Заголовок не может быть пустым", Snackbar.LENGTH_LONG).show();
            return;
        } else {
            if (begin.after(end)) {
                Snackbar.make(getWindow().getDecorView().
                        getRootView(), "Событие не может завершиться раньше, чем начаться", Snackbar.LENGTH_LONG).show();
                return;
            }
        }

        updateTask();
        mockUpTaskService.update(task.getId(), task);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateTask() {
        task.setName(taskName.getText().toString());
        task.setDetails(taskDetails.getText().toString());
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
        menuInflater.inflate(R.menu.edit_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_event:
                editTask(task);
                return true;

            case R.id.delete_event:
                deleteTask(task.getId());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (IS_BEGIN_DATE) {
            task.setBeginYear(year);
            task.setBeginMonth(month);
            task.setBeginDay(dayOfMonth);

            begin.set(Calendar.YEAR, year);
            begin.set(Calendar.MONTH, month);
            begin.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String date = simpleDateFormat.format(begin.getTime());

            taskBeginDate.setText(date);
        } else {
            task.setEndYear(year);
            task.setEndMonth(month);
            task.setEndDay(dayOfMonth);

            end.set(Calendar.YEAR, year);
            end.set(Calendar.MONTH, month);
            end.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String date = simpleDateFormat.format(end.getTime());

            taskEndDate.setText(date);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (IS_BEGIN_TIME) {
            begin.set(Calendar.HOUR, hourOfDay);
            begin.set(Calendar.MINUTE, minute);

            task.setBeginHour(hourOfDay);
            task.setBeginMinute(minute);

            taskBeginTime.setText(getTimeFormat(hourOfDay, minute));
        } else {
            end.set(Calendar.HOUR, hourOfDay);
            end.set(Calendar.MINUTE, minute);

            task.setEndHour(hourOfDay);
            task.setEndMinute(minute);

            taskEndTime.setText(getTimeFormat(hourOfDay, minute));
        }
    }

    private String getTimeFormat(int hourOfDay, int minute) {
        return hourOfDay + ":" + (minute < 10 ? ("0" + minute) : minute);
    }
}
